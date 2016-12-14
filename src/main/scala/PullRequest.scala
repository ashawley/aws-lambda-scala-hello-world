package prs

case class GitPullRequest(
  base: GitBranch,
  head: GitBranch
)

case class GitBranch(
  owner: String,
  repo: String,
  branch: String
) {
  override def toString = s"$repoFullName:$branch"
  lazy val repoFullName = s"$owner/$repo"
  lazy val remote = s"$owner/$branch"
  lazy val label = s"$owner:$branch"
}
