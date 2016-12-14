package prs

case class GitMerge(
  from: GitBranch,
  onto: GitBranch,
  branches: List[GitBranch]
)
