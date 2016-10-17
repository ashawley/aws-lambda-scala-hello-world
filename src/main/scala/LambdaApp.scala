package example

import com.amazonaws.services.lambda.runtime.events.SNSEvent

import scala.collection.JavaConverters._

/***
 * AWS Lambda interface for Java
 */
trait LambdaApp {

  /***
   * Handlers
   */
  def handler(e: SNSEvent): java.util.List[String]

  def cleanUp() = {}

  def pullRequestEvent: String =
    """{
      |  "action": "opened",
      |  "number": 1,
      |  "pull_request": {
      |    "url": "https://api.github.com/repos/baxterthehacker/public-repo/pulls/1",
      |    "id": 34778301,
      |    "html_url": "https://github.com/baxterthehacker/public-repo/pull/1",
      |    "diff_url": "https://github.com/baxterthehacker/public-repo/pull/1.diff",
      |    "patch_url": "https://github.com/baxterthehacker/public-repo/pull/1.patch",
      |    "issue_url": "https://api.github.com/repos/baxterthehacker/public-repo/issues/1",
      |    "number": 1,
      |    "state": "open",
      |    "locked": false,
      |    "title": "Update the README with new information",
      |    "user": {
      |      "login": "baxterthehacker",
      |      "id": 6752317,
      |      "avatar_url": "https://avatars.githubusercontent.com/u/6752317?v=3",
      |      "gravatar_id": "",
      |      "url": "https://api.github.com/users/baxterthehacker",
      |      "html_url": "https://github.com/baxterthehacker",
      |      "followers_url": "https://api.github.com/users/baxterthehacker/followers",
      |      "following_url": "https://api.github.com/users/baxterthehacker/following{/other_user}",
      |      "gists_url": "https://api.github.com/users/baxterthehacker/gists{/gist_id}",
      |      "starred_url": "https://api.github.com/users/baxterthehacker/starred{/owner}{/repo}",
      |      "subscriptions_url": "https://api.github.com/users/baxterthehacker/subscriptions",
      |      "organizations_url": "https://api.github.com/users/baxterthehacker/orgs",
      |      "repos_url": "https://api.github.com/users/baxterthehacker/repos",
      |      "events_url": "https://api.github.com/users/baxterthehacker/events{/privacy}",
      |      "received_events_url": "https://api.github.com/users/baxterthehacker/received_events",
      |      "type": "User",
      |      "site_admin": false
      |    },
      |    "body": "This is a pretty simple change that we need to pull into master.",
      |    "created_at": "2015-05-05T23:40:27Z",
      |    "updated_at": "2015-05-05T23:40:27Z",
      |    "closed_at": null,
      |    "merged_at": null,
      |    "merge_commit_sha": null,
      |    "assignee": null,
      |    "milestone": null,
      |    "commits_url": "https://api.github.com/repos/baxterthehacker/public-repo/pulls/1/commits",
      |    "review_comments_url": "https://api.github.com/repos/baxterthehacker/public-repo/pulls/1/comments",
      |    "review_comment_url": "https://api.github.com/repos/baxterthehacker/public-repo/pulls/comments{/number}",
      |    "comments_url": "https://api.github.com/repos/baxterthehacker/public-repo/issues/1/comments",
      |    "statuses_url": "https://api.github.com/repos/baxterthehacker/public-repo/statuses/0d1a26e67d8f5eaf1f6ba5c57fc3c7d91ac0fd1c",
      |    "head": {
      |      "label": "baxterthehacker:changes",
      |      "ref": "changes",
      |      "sha": "0d1a26e67d8f5eaf1f6ba5c57fc3c7d91ac0fd1c",
      |      "user": {
      |        "login": "baxterthehacker",
      |        "id": 6752317,
      |        "avatar_url": "https://avatars.githubusercontent.com/u/6752317?v=3",
      |        "gravatar_id": "",
      |        "url": "https://api.github.com/users/baxterthehacker",
      |        "html_url": "https://github.com/baxterthehacker",
      |        "followers_url": "https://api.github.com/users/baxterthehacker/followers",
      |        "following_url": "https://api.github.com/users/baxterthehacker/following{/other_user}",
      |        "gists_url": "https://api.github.com/users/baxterthehacker/gists{/gist_id}",
      |        "starred_url": "https://api.github.com/users/baxterthehacker/starred{/owner}{/repo}",
      |        "subscriptions_url": "https://api.github.com/users/baxterthehacker/subscriptions",
      |        "organizations_url": "https://api.github.com/users/baxterthehacker/orgs",
      |        "repos_url": "https://api.github.com/users/baxterthehacker/repos",
      |        "events_url": "https://api.github.com/users/baxterthehacker/events{/privacy}",
      |        "received_events_url": "https://api.github.com/users/baxterthehacker/received_events",
      |        "type": "User",
      |        "site_admin": false
      |      },
      |      "repo": {
      |        "id": 35129377,
      |        "name": "public-repo",
      |        "full_name": "baxterthehacker/public-repo",
      |        "owner": {
      |          "login": "baxterthehacker",
      |          "id": 6752317,
      |          "avatar_url": "https://avatars.githubusercontent.com/u/6752317?v=3",
      |          "gravatar_id": "",
      |          "url": "https://api.github.com/users/baxterthehacker",
      |          "html_url": "https://github.com/baxterthehacker",
      |          "followers_url": "https://api.github.com/users/baxterthehacker/followers",
      |          "following_url": "https://api.github.com/users/baxterthehacker/following{/other_user}",
      |          "gists_url": "https://api.github.com/users/baxterthehacker/gists{/gist_id}",
      |          "starred_url": "https://api.github.com/users/baxterthehacker/starred{/owner}{/repo}",
      |          "subscriptions_url": "https://api.github.com/users/baxterthehacker/subscriptions",
      |          "organizations_url": "https://api.github.com/users/baxterthehacker/orgs",
      |          "repos_url": "https://api.github.com/users/baxterthehacker/repos",
      |          "events_url": "https://api.github.com/users/baxterthehacker/events{/privacy}",
      |          "received_events_url": "https://api.github.com/users/baxterthehacker/received_events",
      |          "type": "User",
      |          "site_admin": false
      |        },
      |        "private": false,
      |        "html_url": "https://github.com/baxterthehacker/public-repo",
      |        "description": "",
      |        "fork": false,
      |        "url": "https://api.github.com/repos/baxterthehacker/public-repo",
      |        "forks_url": "https://api.github.com/repos/baxterthehacker/public-repo/forks",
      |        "keys_url": "https://api.github.com/repos/baxterthehacker/public-repo/keys{/key_id}",
      |        "collaborators_url": "https://api.github.com/repos/baxterthehacker/public-repo/collaborators{/collaborator}",
      |        "teams_url": "https://api.github.com/repos/baxterthehacker/public-repo/teams",
      |        "hooks_url": "https://api.github.com/repos/baxterthehacker/public-repo/hooks",
      |        "issue_events_url": "https://api.github.com/repos/baxterthehacker/public-repo/issues/events{/number}",
      |        "events_url": "https://api.github.com/repos/baxterthehacker/public-repo/events",
      |        "assignees_url": "https://api.github.com/repos/baxterthehacker/public-repo/assignees{/user}",
      |        "branches_url": "https://api.github.com/repos/baxterthehacker/public-repo/branches{/branch}",
      |        "tags_url": "https://api.github.com/repos/baxterthehacker/public-repo/tags",
      |        "blobs_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/blobs{/sha}",
      |        "git_tags_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/tags{/sha}",
      |        "git_refs_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/refs{/sha}",
      |        "trees_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/trees{/sha}",
      |        "statuses_url": "https://api.github.com/repos/baxterthehacker/public-repo/statuses/{sha}",
      |        "languages_url": "https://api.github.com/repos/baxterthehacker/public-repo/languages",
      |        "stargazers_url": "https://api.github.com/repos/baxterthehacker/public-repo/stargazers",
      |        "contributors_url": "https://api.github.com/repos/baxterthehacker/public-repo/contributors",
      |        "subscribers_url": "https://api.github.com/repos/baxterthehacker/public-repo/subscribers",
      |        "subscription_url": "https://api.github.com/repos/baxterthehacker/public-repo/subscription",
      |        "commits_url": "https://api.github.com/repos/baxterthehacker/public-repo/commits{/sha}",
      |        "git_commits_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/commits{/sha}",
      |        "comments_url": "https://api.github.com/repos/baxterthehacker/public-repo/comments{/number}",
      |        "issue_comment_url": "https://api.github.com/repos/baxterthehacker/public-repo/issues/comments{/number}",
      |        "contents_url": "https://api.github.com/repos/baxterthehacker/public-repo/contents/{+path}",
      |        "compare_url": "https://api.github.com/repos/baxterthehacker/public-repo/compare/{base}...{head}",
      |        "merges_url": "https://api.github.com/repos/baxterthehacker/public-repo/merges",
      |        "archive_url": "https://api.github.com/repos/baxterthehacker/public-repo/{archive_format}{/ref}",
      |        "downloads_url": "https://api.github.com/repos/baxterthehacker/public-repo/downloads",
      |        "issues_url": "https://api.github.com/repos/baxterthehacker/public-repo/issues{/number}",
      |        "pulls_url": "https://api.github.com/repos/baxterthehacker/public-repo/pulls{/number}",
      |        "milestones_url": "https://api.github.com/repos/baxterthehacker/public-repo/milestones{/number}",
      |        "notifications_url": "https://api.github.com/repos/baxterthehacker/public-repo/notifications{?since,all,participating}",
      |        "labels_url": "https://api.github.com/repos/baxterthehacker/public-repo/labels{/name}",
      |        "releases_url": "https://api.github.com/repos/baxterthehacker/public-repo/releases{/id}",
      |        "created_at": "2015-05-05T23:40:12Z",
      |        "updated_at": "2015-05-05T23:40:12Z",
      |        "pushed_at": "2015-05-05T23:40:26Z",
      |        "git_url": "git://github.com/baxterthehacker/public-repo.git",
      |        "ssh_url": "git@github.com:baxterthehacker/public-repo.git",
      |        "clone_url": "https://github.com/baxterthehacker/public-repo.git",
      |        "svn_url": "https://github.com/baxterthehacker/public-repo",
      |        "homepage": null,
      |        "size": 0,
      |        "stargazers_count": 0,
      |        "watchers_count": 0,
      |        "language": null,
      |        "has_issues": true,
      |        "has_downloads": true,
      |        "has_wiki": true,
      |        "has_pages": true,
      |        "forks_count": 0,
      |        "mirror_url": null,
      |        "open_issues_count": 1,
      |        "forks": 0,
      |        "open_issues": 1,
      |        "watchers": 0,
      |        "default_branch": "master"
      |      }
      |    },
      |    "base": {
      |      "label": "baxterthehacker:master",
      |      "ref": "master",
      |      "sha": "9049f1265b7d61be4a8904a9a27120d2064dab3b",
      |      "user": {
      |        "login": "baxterthehacker",
      |        "id": 6752317,
      |        "avatar_url": "https://avatars.githubusercontent.com/u/6752317?v=3",
      |        "gravatar_id": "",
      |        "url": "https://api.github.com/users/baxterthehacker",
      |        "html_url": "https://github.com/baxterthehacker",
      |        "followers_url": "https://api.github.com/users/baxterthehacker/followers",
      |        "following_url": "https://api.github.com/users/baxterthehacker/following{/other_user}",
      |        "gists_url": "https://api.github.com/users/baxterthehacker/gists{/gist_id}",
      |        "starred_url": "https://api.github.com/users/baxterthehacker/starred{/owner}{/repo}",
      |        "subscriptions_url": "https://api.github.com/users/baxterthehacker/subscriptions",
      |        "organizations_url": "https://api.github.com/users/baxterthehacker/orgs",
      |        "repos_url": "https://api.github.com/users/baxterthehacker/repos",
      |        "events_url": "https://api.github.com/users/baxterthehacker/events{/privacy}",
      |        "received_events_url": "https://api.github.com/users/baxterthehacker/received_events",
      |        "type": "User",
      |        "site_admin": false
      |      },
      |      "repo": {
      |        "id": 35129377,
      |        "name": "public-repo",
      |        "full_name": "baxterthehacker/public-repo",
      |        "owner": {
      |          "login": "baxterthehacker",
      |          "id": 6752317,
      |          "avatar_url": "https://avatars.githubusercontent.com/u/6752317?v=3",
      |          "gravatar_id": "",
      |          "url": "https://api.github.com/users/baxterthehacker",
      |          "html_url": "https://github.com/baxterthehacker",
      |          "followers_url": "https://api.github.com/users/baxterthehacker/followers",
      |          "following_url": "https://api.github.com/users/baxterthehacker/following{/other_user}",
      |          "gists_url": "https://api.github.com/users/baxterthehacker/gists{/gist_id}",
      |          "starred_url": "https://api.github.com/users/baxterthehacker/starred{/owner}{/repo}",
      |          "subscriptions_url": "https://api.github.com/users/baxterthehacker/subscriptions",
      |          "organizations_url": "https://api.github.com/users/baxterthehacker/orgs",
      |          "repos_url": "https://api.github.com/users/baxterthehacker/repos",
      |          "events_url": "https://api.github.com/users/baxterthehacker/events{/privacy}",
      |          "received_events_url": "https://api.github.com/users/baxterthehacker/received_events",
      |          "type": "User",
      |          "site_admin": false
      |        },
      |        "private": false,
      |        "html_url": "https://github.com/baxterthehacker/public-repo",
      |        "description": "",
      |        "fork": false,
      |        "url": "https://api.github.com/repos/baxterthehacker/public-repo",
      |        "forks_url": "https://api.github.com/repos/baxterthehacker/public-repo/forks",
      |        "keys_url": "https://api.github.com/repos/baxterthehacker/public-repo/keys{/key_id}",
      |        "collaborators_url": "https://api.github.com/repos/baxterthehacker/public-repo/collaborators{/collaborator}",
      |        "teams_url": "https://api.github.com/repos/baxterthehacker/public-repo/teams",
      |        "hooks_url": "https://api.github.com/repos/baxterthehacker/public-repo/hooks",
      |        "issue_events_url": "https://api.github.com/repos/baxterthehacker/public-repo/issues/events{/number}",
      |        "events_url": "https://api.github.com/repos/baxterthehacker/public-repo/events",
      |        "assignees_url": "https://api.github.com/repos/baxterthehacker/public-repo/assignees{/user}",
      |        "branches_url": "https://api.github.com/repos/baxterthehacker/public-repo/branches{/branch}",
      |        "tags_url": "https://api.github.com/repos/baxterthehacker/public-repo/tags",
      |        "blobs_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/blobs{/sha}",
      |        "git_tags_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/tags{/sha}",
      |        "git_refs_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/refs{/sha}",
      |        "trees_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/trees{/sha}",
      |        "statuses_url": "https://api.github.com/repos/baxterthehacker/public-repo/statuses/{sha}",
      |        "languages_url": "https://api.github.com/repos/baxterthehacker/public-repo/languages",
      |        "stargazers_url": "https://api.github.com/repos/baxterthehacker/public-repo/stargazers",
      |        "contributors_url": "https://api.github.com/repos/baxterthehacker/public-repo/contributors",
      |        "subscribers_url": "https://api.github.com/repos/baxterthehacker/public-repo/subscribers",
      |        "subscription_url": "https://api.github.com/repos/baxterthehacker/public-repo/subscription",
      |        "commits_url": "https://api.github.com/repos/baxterthehacker/public-repo/commits{/sha}",
      |        "git_commits_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/commits{/sha}",
      |        "comments_url": "https://api.github.com/repos/baxterthehacker/public-repo/comments{/number}",
      |        "issue_comment_url": "https://api.github.com/repos/baxterthehacker/public-repo/issues/comments{/number}",
      |        "contents_url": "https://api.github.com/repos/baxterthehacker/public-repo/contents/{+path}",
      |        "compare_url": "https://api.github.com/repos/baxterthehacker/public-repo/compare/{base}...{head}",
      |        "merges_url": "https://api.github.com/repos/baxterthehacker/public-repo/merges",
      |        "archive_url": "https://api.github.com/repos/baxterthehacker/public-repo/{archive_format}{/ref}",
      |        "downloads_url": "https://api.github.com/repos/baxterthehacker/public-repo/downloads",
      |        "issues_url": "https://api.github.com/repos/baxterthehacker/public-repo/issues{/number}",
      |        "pulls_url": "https://api.github.com/repos/baxterthehacker/public-repo/pulls{/number}",
      |        "milestones_url": "https://api.github.com/repos/baxterthehacker/public-repo/milestones{/number}",
      |        "notifications_url": "https://api.github.com/repos/baxterthehacker/public-repo/notifications{?since,all,participating}",
      |        "labels_url": "https://api.github.com/repos/baxterthehacker/public-repo/labels{/name}",
      |        "releases_url": "https://api.github.com/repos/baxterthehacker/public-repo/releases{/id}",
      |        "created_at": "2015-05-05T23:40:12Z",
      |        "updated_at": "2015-05-05T23:40:12Z",
      |        "pushed_at": "2015-05-05T23:40:26Z",
      |        "git_url": "git://github.com/baxterthehacker/public-repo.git",
      |        "ssh_url": "git@github.com:baxterthehacker/public-repo.git",
      |        "clone_url": "https://github.com/baxterthehacker/public-repo.git",
      |        "svn_url": "https://github.com/baxterthehacker/public-repo",
      |        "homepage": null,
      |        "size": 0,
      |        "stargazers_count": 0,
      |        "watchers_count": 0,
      |        "language": null,
      |        "has_issues": true,
      |        "has_downloads": true,
      |        "has_wiki": true,
      |        "has_pages": true,
      |        "forks_count": 0,
      |        "mirror_url": null,
      |        "open_issues_count": 1,
      |        "forks": 0,
      |        "open_issues": 1,
      |        "watchers": 0,
      |        "default_branch": "master"
      |      }
      |    },
      |    "_links": {
      |      "self": {
      |        "href": "https://api.github.com/repos/baxterthehacker/public-repo/pulls/1"
      |      },
      |      "html": {
      |        "href": "https://github.com/baxterthehacker/public-repo/pull/1"
      |      },
      |      "issue": {
      |        "href": "https://api.github.com/repos/baxterthehacker/public-repo/issues/1"
      |      },
      |      "comments": {
      |        "href": "https://api.github.com/repos/baxterthehacker/public-repo/issues/1/comments"
      |      },
      |      "review_comments": {
      |        "href": "https://api.github.com/repos/baxterthehacker/public-repo/pulls/1/comments"
      |      },
      |      "review_comment": {
      |        "href": "https://api.github.com/repos/baxterthehacker/public-repo/pulls/comments{/number}"
      |      },
      |      "commits": {
      |        "href": "https://api.github.com/repos/baxterthehacker/public-repo/pulls/1/commits"
      |      },
      |      "statuses": {
      |        "href": "https://api.github.com/repos/baxterthehacker/public-repo/statuses/0d1a26e67d8f5eaf1f6ba5c57fc3c7d91ac0fd1c"
      |      }
      |    },
      |    "merged": false,
      |    "mergeable": null,
      |    "mergeable_state": "unknown",
      |    "merged_by": null,
      |    "comments": 0,
      |    "review_comments": 0,
      |    "commits": 1,
      |    "additions": 1,
      |    "deletions": 1,
      |    "changed_files": 1
      |  },
      |  "repository": {
      |    "id": 35129377,
      |    "name": "public-repo",
      |    "full_name": "baxterthehacker/public-repo",
      |    "owner": {
      |      "login": "baxterthehacker",
      |      "id": 6752317,
      |      "avatar_url": "https://avatars.githubusercontent.com/u/6752317?v=3",
      |      "gravatar_id": "",
      |      "url": "https://api.github.com/users/baxterthehacker",
      |      "html_url": "https://github.com/baxterthehacker",
      |      "followers_url": "https://api.github.com/users/baxterthehacker/followers",
      |      "following_url": "https://api.github.com/users/baxterthehacker/following{/other_user}",
      |      "gists_url": "https://api.github.com/users/baxterthehacker/gists{/gist_id}",
      |      "starred_url": "https://api.github.com/users/baxterthehacker/starred{/owner}{/repo}",
      |      "subscriptions_url": "https://api.github.com/users/baxterthehacker/subscriptions",
      |      "organizations_url": "https://api.github.com/users/baxterthehacker/orgs",
      |      "repos_url": "https://api.github.com/users/baxterthehacker/repos",
      |      "events_url": "https://api.github.com/users/baxterthehacker/events{/privacy}",
      |      "received_events_url": "https://api.github.com/users/baxterthehacker/received_events",
      |      "type": "User",
      |      "site_admin": false
      |    },
      |    "private": false,
      |    "html_url": "https://github.com/baxterthehacker/public-repo",
      |    "description": "",
      |    "fork": false,
      |    "url": "https://api.github.com/repos/baxterthehacker/public-repo",
      |    "forks_url": "https://api.github.com/repos/baxterthehacker/public-repo/forks",
      |    "keys_url": "https://api.github.com/repos/baxterthehacker/public-repo/keys{/key_id}",
      |    "collaborators_url": "https://api.github.com/repos/baxterthehacker/public-repo/collaborators{/collaborator}",
      |    "teams_url": "https://api.github.com/repos/baxterthehacker/public-repo/teams",
      |    "hooks_url": "https://api.github.com/repos/baxterthehacker/public-repo/hooks",
      |    "issue_events_url": "https://api.github.com/repos/baxterthehacker/public-repo/issues/events{/number}",
      |    "events_url": "https://api.github.com/repos/baxterthehacker/public-repo/events",
      |    "assignees_url": "https://api.github.com/repos/baxterthehacker/public-repo/assignees{/user}",
      |    "branches_url": "https://api.github.com/repos/baxterthehacker/public-repo/branches{/branch}",
      |    "tags_url": "https://api.github.com/repos/baxterthehacker/public-repo/tags",
      |    "blobs_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/blobs{/sha}",
      |    "git_tags_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/tags{/sha}",
      |    "git_refs_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/refs{/sha}",
      |    "trees_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/trees{/sha}",
      |    "statuses_url": "https://api.github.com/repos/baxterthehacker/public-repo/statuses/{sha}",
      |    "languages_url": "https://api.github.com/repos/baxterthehacker/public-repo/languages",
      |    "stargazers_url": "https://api.github.com/repos/baxterthehacker/public-repo/stargazers",
      |    "contributors_url": "https://api.github.com/repos/baxterthehacker/public-repo/contributors",
      |    "subscribers_url": "https://api.github.com/repos/baxterthehacker/public-repo/subscribers",
      |    "subscription_url": "https://api.github.com/repos/baxterthehacker/public-repo/subscription",
      |    "commits_url": "https://api.github.com/repos/baxterthehacker/public-repo/commits{/sha}",
      |    "git_commits_url": "https://api.github.com/repos/baxterthehacker/public-repo/git/commits{/sha}",
      |    "comments_url": "https://api.github.com/repos/baxterthehacker/public-repo/comments{/number}",
      |    "issue_comment_url": "https://api.github.com/repos/baxterthehacker/public-repo/issues/comments{/number}",
      |    "contents_url": "https://api.github.com/repos/baxterthehacker/public-repo/contents/{+path}",
      |    "compare_url": "https://api.github.com/repos/baxterthehacker/public-repo/compare/{base}...{head}",
      |    "merges_url": "https://api.github.com/repos/baxterthehacker/public-repo/merges",
      |    "archive_url": "https://api.github.com/repos/baxterthehacker/public-repo/{archive_format}{/ref}",
      |    "downloads_url": "https://api.github.com/repos/baxterthehacker/public-repo/downloads",
      |    "issues_url": "https://api.github.com/repos/baxterthehacker/public-repo/issues{/number}",
      |    "pulls_url": "https://api.github.com/repos/baxterthehacker/public-repo/pulls{/number}",
      |    "milestones_url": "https://api.github.com/repos/baxterthehacker/public-repo/milestones{/number}",
      |    "notifications_url": "https://api.github.com/repos/baxterthehacker/public-repo/notifications{?since,all,participating}",
      |    "labels_url": "https://api.github.com/repos/baxterthehacker/public-repo/labels{/name}",
      |    "releases_url": "https://api.github.com/repos/baxterthehacker/public-repo/releases{/id}",
      |    "created_at": "2015-05-05T23:40:12Z",
      |    "updated_at": "2015-05-05T23:40:12Z",
      |    "pushed_at": "2015-05-05T23:40:26Z",
      |    "git_url": "git://github.com/baxterthehacker/public-repo.git",
      |    "ssh_url": "git@github.com:baxterthehacker/public-repo.git",
      |    "clone_url": "https://github.com/baxterthehacker/public-repo.git",
      |    "svn_url": "https://github.com/baxterthehacker/public-repo",
      |    "homepage": null,
      |    "size": 0,
      |    "stargazers_count": 0,
      |    "watchers_count": 0,
      |    "language": null,
      |    "has_issues": true,
      |    "has_downloads": true,
      |    "has_wiki": true,
      |    "has_pages": true,
      |    "forks_count": 0,
      |    "mirror_url": null,
      |    "open_issues_count": 1,
      |    "forks": 0,
      |    "open_issues": 1,
      |    "watchers": 0,
      |    "default_branch": "master"
      |  },
      |  "sender": {
      |    "login": "baxterthehacker",
      |    "id": 6752317,
      |    "avatar_url": "https://avatars.githubusercontent.com/u/6752317?v=3",
      |    "gravatar_id": "",
      |    "url": "https://api.github.com/users/baxterthehacker",
      |    "html_url": "https://github.com/baxterthehacker",
      |    "followers_url": "https://api.github.com/users/baxterthehacker/followers",
      |    "following_url": "https://api.github.com/users/baxterthehacker/following{/other_user}",
      |    "gists_url": "https://api.github.com/users/baxterthehacker/gists{/gist_id}",
      |    "starred_url": "https://api.github.com/users/baxterthehacker/starred{/owner}{/repo}",
      |    "subscriptions_url": "https://api.github.com/users/baxterthehacker/subscriptions",
      |    "organizations_url": "https://api.github.com/users/baxterthehacker/orgs",
      |    "repos_url": "https://api.github.com/users/baxterthehacker/repos",
      |    "events_url": "https://api.github.com/users/baxterthehacker/events{/privacy}",
      |    "received_events_url": "https://api.github.com/users/baxterthehacker/received_events",
      |    "type": "User",
      |    "site_admin": false
      |  }
      |}""".stripMargin

  // {
  //   "Records": [
  //     {
  //       "EventVersion": "1.0",
  //       "EventSubscriptionArn": "arn:aws:sns:EXAMPLE",
  //       "EventSource": "aws:sns",
  //       "Sns": {
  //         "SignatureVersion": "1",
  //         "Timestamp": "1970-01-01T00:00:00.000Z",
  //         "Signature": "EXAMPLE",
  //         "SigningCertUrl": "EXAMPLE",
  //         "MessageId": "95df01b4-ee98-5cb9-9903-4c221d41eb5e",
  //         "Message": "Hello from SNS!",
  //         "MessageAttributes": {
  //           "X-Github-Event": {
  //             "Type": "String",
  //             "Value": "pull_request"
  //           }
  //         },
  //         "Type": "Notification",
  //         "UnsubscribeUrl": "EXAMPLE",
  //         "TopicArn": "arn:aws:sns:EXAMPLE",
  //         "Subject": "TestInvoke"
  //       }
  //     }
  //   ]
  // }
  val snsEvent = {
    val m = new SNSEvent.MessageAttribute
    m.setType("String")
    m.setValue("pull_request")
    val s = new SNSEvent.SNS
    s.setSignatureVersion("1")
    s.setTimestamp(new org.joda.time.DateTime("1970-01-01T00:00:00.000Z"))
    s.setSignature("EXAMPLE")
    s.setSigningCertUrl("EXAMPLE")
    s.setMessageId("95df01b4-ee98-5cb9-9903-4c221d41eb5e")
    s.setMessage(pullRequestEvent)
    s.setMessageAttributes(
      Map("X-GitHub-Event" -> m).asJava)
    s.setType("Notification")
    s.setUnsubscribeUrl("EXAMPLE")
    s.setTopicArn("arn:aws:sns:EXAMPLE")
    s.setSubject("TestInvoke")
    val r = new SNSEvent.SNSRecord
    r.setEventVersion("1.0")
    r.setEventSubscriptionArn("arn:aws:sns:EXAMPLE")
    r.setEventSource("aws:sns")
    r.setSns(s)
    val e = new SNSEvent
    e.setRecords(List(r).asJava)
    e
  }

  /***
   * Driver for testing handler locally
   */ 
  def main(args: Array[String]): Unit = {
    val uuid = java.util.UUID.randomUUID
    val version = "$LATEST"

    println(s"START RequestId: $uuid Version: $version")

    val t0 = System.nanoTime
    var result: java.util.List[String] = new java.util.ArrayList[String]()
    try {
      result = handler(snsEvent)
    } catch {
      case e: Throwable => e.printStackTrace
    } finally {
      cleanUp()
    }
    println(s"END RequestId: $uuid")

    val t1 = System.nanoTime
    val duration = (t1 - t0) / 1e6
    val billedDuration = scala.math.ceil(duration / 100d).toLong * 100

    val env = Runtime.getRuntime
    val memorySize = env.totalMemory / 1024 / 1024
    val memoryUsed = (env.totalMemory - env.freeMemory) / 1024 / 1024

    println(s"""REPORT RequestId: $uuid
                 |  Duration: ${duration} ms
                 |  Billed Duration: ${billedDuration} ms
                 |  Memory Size: ${memorySize} MB
                 |  Max Memory Used: ${memoryUsed} MB""".stripMargin
      .replaceAll("\n", ""))

    println("[")
    println("  " + result.asScala.map(escString(_)).mkString(",\n  "))
    println("]")
  }

  def escString(s: String) =
    "\"" + s.replaceAll("\"", "\\\"") + "\""
}
