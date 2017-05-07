package prs

case class GitPullRequest(
  base: GitBranch,
  head: GitBranch,
  sha: String
)

case class GitBranch(
  owner: String,
  repo: String,
  branch: String,
  sha: String
) {
  override def toString = s"$repoFullName:$branch"
  lazy val repoFullName = s"$owner/$repo"
  lazy val remote = s"$owner/$branch"
  lazy val label = s"$owner:$branch"
  override def equals(o: Any) = o match {
    // Only compare owner, repo and branch.  Ignore sha.
    case that: GitBranch => this.toString == that.toString
    case _ => false
  }
}
