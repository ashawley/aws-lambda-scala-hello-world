package prs

import knobs.Typesafe

import com.typesafe.scalalogging

import org.eclipse.jgit

import com.jcraft.jsch

import codecheck.github

import com.ning.http.client.AsyncHttpClient

import com.amazonaws.services.lambda.runtime.events.SNSEvent

import org.json4s._
import org.json4s.native.JsonMethods._

import scala.concurrent.Await
import scala.concurrent.duration._

import scala.collection.JavaConverters._

object Main extends LambdaApp with scalalogging.StrictLogging {

  val config = Typesafe.config.unsafePerformSync

  val tmpRoot = sbt.io.IO.createTemporaryDirectory

  val sshConfig = SshConfig(
    knownHostsFileName = config.require[String]("ssh.known-hosts-file"),
    privateKey         = config.require[String]("ssh.private-key-name"),
    publicKey          = config.require[String]("ssh.public-key-name"),
    passphrase         = config.require[String]("ssh.passphrase")
  )

  // Validate these:
  val repoConfig = GitBranch(
    owner  = config.require[String]("git.owner"),
    repo   = config.require[String]("git.repo"),
    branch = config.require[String]("git.base"),
    sha = "HEAD" // Dummy value
  )

  val gitUser = config.require[String]("git.user.name")
  val gitEmail = config.require[String]("git.user.email")

  lazy val sshKey = scala.io.Source.fromInputStream(
    getClass.
      getClassLoader.
      getResourceAsStream(sshConfig.privateKey)).mkString

  lazy val sshPubKey = scala.io.Source.fromInputStream(
    getClass.
      getClassLoader.
      getResourceAsStream(sshConfig.publicKey)).mkString
  
  val knownHosts: java.io.InputStream =
    getClass.getClassLoader.getResourceAsStream(sshConfig.knownHostsFileName)

  val integrationBranch = config.require[String]("git.onto")

  val githubToken = config.require[String]("github.token")

  val client = new github.transport.asynchttp19.AsyncHttp19Transport(new AsyncHttpClient())
  val githubApi = new github.api.GitHubAPI(githubToken, client)

  val sshSessionFactory = new jgit.transport.JschConfigSessionFactory {

    def configure(
      host: jgit.transport.OpenSshConfig.Host, session: jsch.Session
    ): Unit = {
      // session.setConfig("StrictHostKeyChecking", "no")
      // com.jcraft.jsch.JSch.setConfig("StrictHostKeyChecking", "no")
      // com.jcraft.jsch.JSch.setLogger(new JSchLogger(jsch.Logger.DEBUG))
    }

    override def createDefaultJSch(fs: jgit.util.FS): com.jcraft.jsch.JSch = {
      val defaultJSch = super.createDefaultJSch(fs)
      defaultJSch.addIdentity(
        sshConfig.privateKey,
        sshKey.getBytes, sshPubKey.getBytes,
        sshConfig.passphrase.getBytes
      )
      defaultJSch.setKnownHosts(knownHosts)
      defaultJSch
    }
  }

  val transportConfigCallback = new jgit.api.TransportConfigCallback {
    def configure(transport: jgit.transport.Transport): Unit = transport match {
      case sshTransport: jgit.transport.SshTransport =>
        sshTransport.setSshSessionFactory(sshSessionFactory)
    }
  }

  def safeList[A](xs: java.util.List[A]) =
    Option(xs).map(_.asScala).getOrElse(List.empty[A])

  def handler(e: SNSEvent)  = {

    // Provided by sbt-buildinfo plugin
    logger.info(s"Starting ${BuildInfo.name} ${BuildInfo.version}")

    logger.info("Inspecting SNS notification...")
    val events = for {
      r <- safeList(e.getRecords)
      (key, attribute) <- r.getSNS.getMessageAttributes.asScala
      if key == "X-Github-Event" && attribute.getValue == "pull_request"
    } yield {
      github.events.GitHubEvent("pull_request", parse(r.getSNS.getMessage))
    }

    logger.info(s"Processed ${events.size} event(s)")

    val pullRequests = events.collect {
      case e: github.events.PullRequestEvent =>
        GitPullRequest(
          GitBranch(
            e.pull_request.base.repo.owner.login,
            e.pull_request.base.repo.name,
            e.pull_request.base.ref,
            "HEAD"
          ),
          GitBranch(
            e.pull_request.head.repo.owner.login,
            e.pull_request.head.repo.name,
            e.pull_request.head.ref,
            e.pull_request.head.sha
          ),
          e.pull_request.head.sha
        )
    }

    pullRequests.foreach { pr =>
      if (pr.base == repoConfig) {
        logger.info(s"Pull request was ${pr.base.remote}..${pr.head.remote}")
      } else {
        logger.error(s"Expected $repoConfig but got ${pr.base}")
      }
    }

    val merges = for {
      pr <- pullRequests if pr.base == repoConfig
    } yield {
      val merge = mergeFor(pr)
      try {
        doMerge(merge)
      } catch {
        case e: Throwable => {

          logger.error(s"Caught error ${e.getMessage}")

          logger.info(s"Setting status on pull requests...")

          for {
            br <- merge.branches
          } yield {
            val status = github.models.StatusInput(
              github.models.StatusState.error,
              description = Some(e.getMessage),
              context = Some("continuous-integration")
            )
            githubApi.createStatus(repoConfig.owner, repoConfig.repo, br.sha, status)
          }

          s"Failed to merge: ${e.getMessage}"
        }
      }
    }
    client.close
    merges.asJava
  }

  def mergeFor(pr: GitPullRequest) = {
    logger.info(s"Querying GitHub for open pull requests...")
    val listFilter = github.models.PullRequestListOption(
      base      = Some(pr.base.branch),
      sort      = github.models.IssueSort.created,
      direction = github.models.SortDirection.asc
    )
    val pullRequests =
      Await.result(
        githubApi.listPullRequests(repoConfig.owner, repoConfig.repo, listFilter),
        Duration.Inf
      )
    val branchesToMerge = for {
      pullRequest <- pullRequests
    } yield {
      GitBranch(
        pullRequest.head.repo.owner.login,
        pullRequest.head.repo.name,
        pullRequest.head.ref,
        pullRequest.head.sha
      )
    }

    logger.info(s"Found ${branchesToMerge.size} pull request(s)")

    GitMerge(
      repoConfig,
      repoConfig.copy(branch = integrationBranch),
      branchesToMerge
    )
  }

  def gitURI(br: GitBranch) =
    s"git@github.com:${br.owner}/${br.repo}.git"

  def doMerge(m: GitMerge) = {

    val upstream = gitURI(m.from)

    val gitPath = sbt.io.Path(tmpRoot) / m.from.repo

    logger.info(s"Working directory is $tmpRoot")

    logger.info(s"Cloning $upstream...")

    val git = jgit.api.Git.cloneRepository()
      .setDirectory(gitPath)
      .setURI(upstream)
      .setRemote(m.from.owner)
      .setBranch(m.from.branch)
      .setCloneAllBranches(false)
      .setTransportConfigCallback(transportConfigCallback)
      .call()

    val repo = git.getRepository

    logger.info(s"Configuring repository...")

    val gitConfig = repo.getConfig

    gitConfig.setString("user", null, "name", gitUser)
    gitConfig.setString("user", null, "email", gitEmail)

    logger.info(s"Set user to $gitUser")
    logger.info(s"Set email to $gitEmail")

    logger.info(s"Configuring remotes...")

    val remotes = for {
      br <- m.branches.groupBy(_.repoFullName).values.map(_.head)
      if br.owner != m.from.owner
    } yield {

      val status = github.models.StatusInput(
        github.models.StatusState.pending,
        description = Some("Continuously integrating..."),
        context = Some("continuous-integration")
      )
      githubApi.createStatus(repoConfig.owner, repoConfig.repo, br.sha, status)

      val repoURI = gitURI(br)
      val refSpec =
        s"+refs/heads/*:refs/remotes/${br.owner}/*"

      logger.info(s"Remote ${br.owner} at $repoURI")

      val remoteConfig = new jgit.transport.RemoteConfig(gitConfig, br.owner)
      remoteConfig.addURI(new jgit.transport.URIish(repoURI))
      remoteConfig.addFetchRefSpec(new jgit.transport.RefSpec(refSpec))
      remoteConfig.update(gitConfig)
      remoteConfig
    }

    gitConfig.save()

    logger.info(s"Configured ${remotes.size} remote(s)")

    val fetches = for {
      br <- m.branches
      if br.owner != m.from.owner
    } yield {

      logger.info(s"Fetching ${br.label}...")

      val refSpec =
        s"refs/heads/${br.branch}:refs/remotes/${br.owner}/${br.branch}"

      git.fetch()
        .setRemote(br.owner)
        .setRefSpecs(new jgit.transport.RefSpec(refSpec))
        .setCheckFetchedObjects(true)
        .setTransportConfigCallback(transportConfigCallback)
        .call()
    }

    logger.info(s"Checking out ${m.from.owner}/${m.from.branch}...")

    val checkout = git.checkout()
      .setCreateBranch(false)
      .setName(m.from.branch)
      .setStartPoint(s"${m.from.owner}/${m.from.branch}")
      .call()

    logger.info(s"Creating branch $integrationBranch...")

    val branch = git.checkout()
      .setCreateBranch(true)
      .setName(integrationBranch)
      .call()

    val merges = for {
      br <- m.branches
    } yield {

      logger.info(s"Merging ${br.remote}...")

      git.merge()
        .include(repo.exactRef(s"refs/remotes/${br.remote}"))
        .call()
    }

    if (merges.isEmpty) {
      logger.info(s"Nothing to merge...")
    }

    // See if merge above failed
    merges.zip(m.branches).find {
      case (mergeResult, _) => {
        !mergeResult.getMergeStatus.isSuccessful
      }
    } match {

      // Don't push, but set to status to fail

      case Some((mergeFailure, failBranch)) => {
        logger.error(s"Merge failed so $integrationBranch will not be pushed...")
        val desc = s"Failed to merge ${failBranch.label}: ${mergeFailure.getMergeStatus}"
        logger.error(desc)
        for {
          br <- m.branches
        } yield {
          val status = github.models.StatusInput(
            github.models.StatusState.failure,
            description = Some(desc),
            context = Some("continuous-integration")
          )
          logger.info(s"Setting status of ${br.label} to $desc")
          githubApi.createStatus(repoConfig.owner, repoConfig.repo, br.sha, status)
          
        }
        desc
      }
      case None => {
    
        // Push if merge above succeeded

        logger.info(s"Pushing $integrationBranch...")

        val push = git.push()
          .setForce(true)
          .setRemote(m.from.owner)
          .setRefSpecs(new jgit.transport.RefSpec(integrationBranch))
          .setTransportConfigCallback(transportConfigCallback)
          .call()

        for {
          br <- m.branches
        } yield {
          val status = github.models.StatusInput(
            github.models.StatusState.success,
            description = Some("Continuous integration succeeded"),
            context = Some("continuous-integration")
          )
          githubApi.createStatus(repoConfig.owner, repoConfig.repo, br.sha, status)
        }

        val branchNames = m.branches.map { br =>
          s"'${br.owner}/${br.branch}'"
        } mkString(", ")
        s"Merged branch(es) $branchNames into $integrationBranch"
      }
    }
  }

  /**
    * Used by LambdaApp when run locally repeatedly
    */
  override def cleanUp() = {
    sbt.io.IO.delete(tmpRoot)
  }

}
