[AWS console]: http://console.aws.amazon.com
[SBT Assembly]: http://github.com/sbt/sbt-assembly

## Merging pull requests on AWS Lambda with GitHub-SNS notifications

The following Scala code builds a Java JAR file that can run on AWS
Lambda to automatically integrate pull requests for a project.

### Overview

A Scala program that is tightly coupled to the following libraries:

- The Java library, JGit, by Eclipse can clone Git repositories,
checkout branches, merge them and push them.
- The Java library, JSch, from JCraft provides SSH support for JGit
including support for user and host key verification.
- Use of the GitHub API is with a Scala library, github-api, by SHUNJI
Konishi of Codecheck.
- Connect to GitHub's API over HTTP with a Java library, Async HTTP
Client, by Ning.
- JSON serialization with the Scala library, json4s, by Ivan Porto
Carrero and KAZUHIRO Sera.
- The AWS Lambda Java libraries by Amazon AWS provide the ability to run
the JAR in their *serverless* Java 8 runtime.
- JAR produced using the SBT plugin, SBT Assembly, by Eugene Yokota.
- JVM file system housekeeping provided by the sbt.io Scala library,
from SBT team at Lightbend, Inc.
- Logging provided by SLF4J of QoS.ch and Typesafe Scala Logging by
Lightbend, Inc.

Steps in detail:

- Receive event from GitHub by way of Amazon SNS notification
- Load config file from `application.conf`
- Verify repo in SNS is same repo in config file
- Find name of base branch in config file to merge on to
- Find all pull requests in the repo for the base branch
- Use GitHub token in config file to call GitHub API
- Checkout base branch with Git and SSH
- Use SSH keys and known_hosts specified in config file
- Use SSH keys and known_hosts included in JAR file
- Create integration branch specified in config file with Git
- Merge pull requests on to integration branch with Git
- Force push to GitHub using Git and SSH
- If merge succeeds, return list of merged branches
- If merge fails, don't push, and notify failure in SNS

### Testing locally

This code is designed to run at AWS Lambda.  This makes it difficult
to test outside of AWS Lambda.  The app runs in Lambda with input from
SNS based on GitHub notifications and is called in AWS Lambda in the
peculiar way that Lambda operates.

The `LambdaApp` trait is defined to enforce a type signature for an
AWS Lambda functions, and can help with testing locally by providing
mock data inputs to run against.  However, certain activities are not
mocked, but instead operate against live systems: The application will
request data from GitHub's API and conduct Git operations against the
Git repo configured in `application.conf`.  Currently, these services
are not mocked in any tests.

Some of the steps for testing locally are the same as getting the
application to run in AWS Lambda.

- Rename the `application.conf.template` file in `src/main/resources`
to `application.conf`.  - Generate an SSH key with an empty passphrase

```bash
$ ssh-keygen -t rsa -f src/main/resources/ssh/id_rsa
```

- With the files `id_rsa` and `id_rsa.pub` in the `src/main/resources/ssh`
directory.  Specify the names of these files in the `application.conf`
file.

- Specify the name of the Git repository and the Git branch you want to
update with auto-merged PRs in the `application.conf` file.

- Add the public key to ~~the repository~~ your user account in GitHub
as ~~a deploy~~ an SSH key ~~with *write access*~~

 - Visit ~~http://github.com/my/repo/settings/keys~~
https://github.com/settings/ssh

- Create a GitHub access token in your user settings at

 - http://github.com/settings/tokens/new

 - Click on **Generate new token**

 - Provide a **Token description**, such as **AWS Lambda**

 - Select the **repo** scope for access

 - Click **Generate token**

 - Copy the personal access token

- Then type `run` in SBT

```
> run
[info] Running prs.Main
START RequestId: dc8beb69-1858-41f0-884c-9058930ea98f Version: $LATEST
END RequestId: dc8beb69-1858-41f0-884c-9058930ea98f
REPORT RequestId: dc8beb69-1858-41f0-884c-9058930ea98f  Duration: 209.349397 ms  Memory Size: 2047 MB  Max Memory Used: 518 MB
[
  "username/feature1",
  "username/hotfix1"
]
[success] Total time: 2 s, completed Oct 16, 2016 10:20:10 PM
```

### Building the JAR

To run this app on AWS Lambda, run the task provided by [SBT
Assembly], to build a JAR file.

```
> assembly
[info] Including: json4s-ext_2.11-3.4.2.jar
[info] Including from cache: async-http-client-1.9.21.jar
[info] Including from cache: aws-java-sdk-s3-1.11.0.jar
[info] Including from cache: aws-java-sdk-kms-1.11.0.jar
[info] Including from cache: netty-3.10.1.Final.jar
[info] Including from cache: aws-java-sdk-core-1.11.0.jar
[info] Including from cache: commons-logging-1.1.3.jar
[info] Including: joda-time-2.9.4.jar
[info] Including: github-api_2.11.jar
[info] Including from cache: httpclient-4.5.2.jar
[info] Including from cache: httpcore-4.4.4.jar
[info] Including from cache: commons-codec-1.9.jar
[info] Including from cache: jackson-dataformat-cbor-2.5.3.jar
[info] Including from cache: aws-java-sdk-sns-1.11.0.jar
[info] Including from cache: aws-java-sdk-sqs-1.11.0.jar
[info] Including from cache: scala-library-2.11.8.jar
[info] Including: typesafe_2.11-3.12.27a.jar
[info] Including from cache: aws-java-sdk-cognitoidentity-1.11.0.jar
[info] Including: core_2.11-3.12.27a.jar
[info] Including from cache: aws-java-sdk-kinesis-1.11.0.jar
[info] Including from cache: aws-java-sdk-dynamodb-1.11.0.jar
[info] Including: scodec-bits_2.11-1.0.12.jar
[info] Including: parser_2.11-0.3.3.jar
[info] Including: json4s-jackson_2.11-3.4.2.jar
[info] Including: json4s-core_2.11-3.4.2.jar
[info] Including: scalaz-stream_2.11-0.8.1a.jar
[info] Including: joda-convert-1.8.1.jar
[info] Including from cache: logback-classic-1.1.3.jar
[info] Including from cache: logback-core-1.1.3.jar
[info] Including from cache: scopt_2.11-3.3.0.jar
[info] Including from cache: json4s-native_2.11-3.4.0.jar
[info] Including: json4s-ast_2.11-3.4.2.jar
[info] Including from cache: org.eclipse.jgit-4.5.0.201609210915-r.jar
[info] Including from cache: jsch-0.1.53.jar
[info] Including: json4s-scalap_2.11-3.4.2.jar
[info] Including from cache: JavaEWAH-0.7.9.jar
[info] Including from cache: slf4j-nop-1.7.21.jar
[info] Including from cache: slf4j-api-1.7.21.jar
[info] Including: io_2.11-1.0.0-M7.jar
[info] Including: config-1.2.1.jar
[info] Including: scalaz-core_2.11-7.2.7.jar
[info] Including from cache: aws-lambda-java-core-1.1.0.jar
[info] Including from cache: aws-lambda-java-events-1.3.0.jar
[info] Including from cache: paranamer-2.8.jar
[info] Including from cache: scala-xml_2.11-1.0.5.jar
[info] Including: jackson-databind-2.6.7.jar
[info] Including: scalaz-concurrent_2.11-7.2.2.jar
[info] Including: scalaz-effect_2.11-7.2.2.jar
[info] Including from cache: jackson-annotations-2.6.0.jar
[info] Including: jackson-core-2.6.7.jar
[info] Checking every *.class/*.jar file's SHA-1.
[info] Merging files...
[warn] Merging 'META-INF/DEPENDENCIES' with strategy 'discard'
[warn] Merging 'META-INF/ECLIPSE_.RSA' with strategy 'discard'
[warn] Merging 'META-INF/ECLIPSE_.SF' with strategy 'discard'
[warn] Merging 'META-INF/LICENSE' with strategy 'discard'
[warn] Merging 'META-INF/LICENSE.txt' with strategy 'discard'
[warn] Merging 'META-INF/LICENSE_commons-codec-1.9.txt' with strategy 'discard'
[warn] Merging 'META-INF/LICENSE_commons-logging-1.1.3.txt' with strategy 'discard'
[warn] Merging 'META-INF/LICENSE_httpclient-4.5.2' with strategy 'discard'
[warn] Merging 'META-INF/LICENSE_httpcore-4.4.4' with strategy 'discard'
[warn] Merging 'META-INF/LICENSE_jackson-annotations-2.6.0' with strategy 'discard'
[warn] Merging 'META-INF/LICENSE_jackson-dataformat-cbor-2.5.3' with strategy 'discard'
[warn] Merging 'META-INF/LICENSE_netty-3.10.1.Final.txt' with strategy 'discard'
[warn] Merging 'META-INF/MANIFEST.MF' with strategy 'discard'
[warn] Merging 'META-INF/NOTICE' with strategy 'discard'
[warn] Merging 'META-INF/NOTICE.txt' with strategy 'discard'
[warn] Merging 'META-INF/NOTICE_commons-codec-1.9.txt' with strategy 'discard'
[warn] Merging 'META-INF/NOTICE_commons-logging-1.1.3.txt' with strategy 'discard'
[warn] Merging 'META-INF/NOTICE_httpclient-4.5.2' with strategy 'discard'
[warn] Merging 'META-INF/NOTICE_httpcore-4.4.4' with strategy 'discard'
[warn] Merging 'META-INF/NOTICE_netty-3.10.1.Final.txt' with strategy 'discard'
[warn] Merging 'META-INF/license_netty-3.10.1.Final/LICENSE.base64.txt' with strategy 'discard'
[warn] Merging 'META-INF/license_netty-3.10.1.Final/LICENSE.bouncycastle.txt' with strategy 'discard'
[warn] Merging 'META-INF/license_netty-3.10.1.Final/LICENSE.commons-logging.txt' with strategy 'discard'
[warn] Merging 'META-INF/license_netty-3.10.1.Final/LICENSE.felix.txt' with strategy 'discard'
[warn] Merging 'META-INF/license_netty-3.10.1.Final/LICENSE.jboss-logging.txt' with strategy 'discard'
[warn] Merging 'META-INF/license_netty-3.10.1.Final/LICENSE.jsr166y.txt' with strategy 'discard'
[warn] Merging 'META-INF/license_netty-3.10.1.Final/LICENSE.jzlib.txt' with strategy 'discard'
[warn] Merging 'META-INF/license_netty-3.10.1.Final/LICENSE.log4j.txt' with strategy 'discard'
[warn] Merging 'META-INF/license_netty-3.10.1.Final/LICENSE.protobuf.txt' with strategy 'discard'
[warn] Merging 'META-INF/license_netty-3.10.1.Final/LICENSE.slf4j.txt' with strategy 'discard'
[warn] Merging 'META-INF/license_netty-3.10.1.Final/LICENSE.webbit.txt' with strategy 'discard'
[warn] Merging 'META-INF/maven/ch.qos.logback/logback-classic/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/ch.qos.logback/logback-classic/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/ch.qos.logback/logback-core/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/ch.qos.logback/logback-core/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-cognitoidentity/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-cognitoidentity/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-core/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-core/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-dynamodb/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-dynamodb/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-kinesis/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-kinesis/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-kms/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-kms/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-s3/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-s3/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-sns/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-sns/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-sqs/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-java-sdk-sqs/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-lambda-java-core/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-lambda-java-core/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-lambda-java-events/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.amazonaws/aws-lambda-java-events/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.fasterxml.jackson.core/jackson-annotations/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.fasterxml.jackson.core/jackson-annotations/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.fasterxml.jackson.core/jackson-core/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.fasterxml.jackson.core/jackson-core/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.fasterxml.jackson.core/jackson-databind/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.fasterxml.jackson.core/jackson-databind/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.fasterxml.jackson.dataformat/jackson-dataformat-cbor/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.fasterxml.jackson.dataformat/jackson-dataformat-cbor/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.googlecode.javaewah/JavaEWAH/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.googlecode.javaewah/JavaEWAH/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.jcraft/jsch/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.jcraft/jsch/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.ning/async-http-client/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.ning/async-http-client/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.thoughtworks.paranamer/paranamer/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/com.thoughtworks.paranamer/paranamer/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/commons-codec/commons-codec/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/commons-codec/commons-codec/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/commons-logging/commons-logging/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/commons-logging/commons-logging/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/io.netty/netty/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/io.netty/netty/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/joda-time/joda-time/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/joda-time/joda-time/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.apache.httpcomponents/httpclient/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.apache.httpcomponents/httpclient/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.apache.httpcomponents/httpcore/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.apache.httpcomponents/httpcore/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.eclipse.jgit/org.eclipse.jgit/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.eclipse.jgit/org.eclipse.jgit/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.joda/joda-convert/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.joda/joda-convert/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.slf4j/slf4j-api/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.slf4j/slf4j-api/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.slf4j/slf4j-nop/pom.properties' with strategy 'discard'
[warn] Merging 'META-INF/maven/org.slf4j/slf4j-nop/pom.xml' with strategy 'discard'
[warn] Merging 'META-INF/services/com.fasterxml.jackson.core.JsonFactory' with strategy 'discard'
[warn] Merging 'META-INF/services/com.fasterxml.jackson.core.ObjectCodec' with strategy 'discard'
[warn] Merging 'org/slf4j/impl/StaticLoggerBinder.class' with strategy 'first'
[warn] Merging 'org/slf4j/impl/StaticMDCBinder.class' with strategy 'first'
[warn] Merging 'org/slf4j/impl/StaticMarkerBinder.class' with strategy 'first'
[warn] Strategy 'discard' was applied to 93 files
[warn] Strategy 'first' was applied to 3 files
[info] SHA-1: 0dfd2a6c4c869f16d0dba605948c5755296742c0
[info] Packaging target/scala-2.11/aws-gh-prs-assembly-0.1-SNAPSHOT.jar ...
[info] Done packaging.
[success] Total time: 63 s, completed Dec 14, 2016 9:29:40 AM
```

### Testing at AWS

- Follow the instructions for testing locally, see above

- Create a new AWS SNS topic

 - From the [AWS console], click on the **SNS** service

 - Click on **Create a new Topic**

 - Enter a topic name, such as **GitHub-My-Repo-Testing**

 - Enter a display name, such as **GitHub**

 - Click on **Create topic**

 - Copy the **ARN** for the SNS topic you created

- Create a new AWS IAM policy

 - From the [AWS console], click on the **IAM** service

 - Click on **Policies**

 - Click on **Create Policy**

 - Click **Policy Generator**

 - Choose **Allow**

 - Choose **Amazon SNS** from **AWS Service**

 - Paste the **ARN** from SNS earlier

 - Click **Add Statement**

 - Click on **Next Step**

 - Provide a **Policy Name**, such as **SNSGitHubMyRepoPublish**

 - The policy produced should be

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Stmt1481659429000",
            "Effect": "Allow",
            "Action": [
                "sns:Publish"
            ],
            "Resource": [
                "arn:aws:sns:us-east-1:087683607067:GitHub-My-Repo-Testing"
            ]
        }
    ]
}
```

 - Click on **Create Policy**

- Create a new user in IAM

 - From IAM, click on **Users**

 - Click **Add user**

 - Enter a user name, such as **sns-github-my-repo**

 - Select **Programmatic access** as the **access type**

 - Choose **Attach existing policies directly**

 - Choose the policy you created earlier

 - Click on **Next: Review**

 - Click **Create user**

 - Copy the **Access Key ID** and the **Secret access key**

- Connect the GitHub repo to the new SNS topic

 - In GitHub, visit your repo's integration settings

   - http://github.com/my/repo/settings/installations

 - Add **Amazon SNS** as a service

 - For **Aws key**, enter the **Access key** from earlier

 - In the **Sns topic** enter the **ARN** from earlier

 - For the **Aws secret**, enter the **Secret access key**

 - Click **Add service**

- Add the app to Lambda as a JAR

 - Follow the instructions below, see "Uploading to AWS Lambda"

### Uploading to AWS Lambda

- From the [AWS console], click on **Configure function** from the
Lambda service

- Click **Create a Lambda Function**

- Don't select a blue print

- Click on **Configure triggers**

- Add an **SNS** trigger

- Select the SNS topic you created earlier

- Click the **Enable trigger** checkbox

- Click **Next**

- Provide a **Name** for the function, such as **GithubMyRepoTest**

- Leave the description blank

- Set the **Runtime** to **Java 8**

- For **Function package**, click the **Upload**

- Upload the JAR file created by SBT assembly at
`target/scala-2.11/aws-gh-prs-assembly-0.1-SNAPSHOT.jar`

- Enter the handler as, `prs.Main::handler`

- Choose **Create new role from template(s)**

- Enter **LambdaTestRole** as the role name

- Leave **Policy templates** blank

- Reduce the memory to **384 MB**, and leave the timeout at *815 seconds**

- Keep the setting for **VPC** to none

- Click **Next**

- Click **Create function**

- Click **Actions** and then **Configure test event**

- From the **Sample event template** drop-down, choose **SNS**

- Click **Save and Test**

- The test will succeed but only return the empty array, `[]`

- To change the test to one that is closer to a GitHub event

 - Insert a minimal string of json in the SNS **Message**
```
    "Message": "{\"action\":\"opened\",\"number\":1,\"pull_request\":{\"state\":\"open\",\"head\":{\"ref\":\"changes\",\"repo\":{\"name\":\"public-repo\",\"owner\":{\"login\":\"baxterthehacker\"}}},\"base\":{\"ref\":\"master\",\"repo\":{\"name\":\"public-repo\",\"owner\":{\"login\":\"baxterthehacker\"}}}}}"
```
 - Set the **MessageAttributes** to an **X-GitHub-Event**
```
    "MessageAttributes": {
      "X-GitHub-Event": {
        "Type": "String",
        "Value": "pull_request"
      }
    }
```
### Warranty

**Buyer beware**: This application will overwrite Git branches.
Should the merge complete successfully, the application does a forced
push to a branch of a remote Git repo.  The branch is the one that is
configured in `application.conf`.  A forced push could result in data
loss.  The SSH ~~deploy~~ user key added to GitHub will have access to
all the repos the user is configured for.  However, by the nature of
private repos in GitHub, write access to a root repo in GitHub will
provide the same access to forked repos.  This is advantageous since
the program requires read-access to forks.  Public repos provide
read-access, by default, ~~without a deploy key~~ for any GitHub user.
Since this program requires write-access to the repo configured in
`application.conf`, the ~~deploy~~ user key will have write-access to
~~forked private ~~ all repos that user has access to.

 The code will verify that the Git repo specified in
`application.conf`, including the branch to monitor for pull requests.

The application as written doesn't catch any exceptions.  There's no
need for the program to recover given it is an internal task that runs
in AWS Lambda, a serverless environment.

The SSH key used to pull from private repos or push to repositories,
is included in the JAR file.  This is a security concern, since the
SSH key will have write privileges to remote Git repositories at
GitHub.

Currently, if there is an error then it results in an exception being
thrown and execution being halted.

Some of the failure conditions that should be caught with a friendly
error message, include

- Unable to read or write to temporary directory on filesystem
- Conf file missing from JAR
- Conf file missing directives
- SNS Event json is malformed
- GitHub json is malformed
- SSH keys missing from the JAR
- SSH hosts key file, `known_hosts`, is missing from JAR
- SSH host key verification fails
- Git client is broken or unavailable
- Git server is broken or unavailable
- A Git remote doesn't exist
- Base branch doesn't exist
- HTTP is broken or unavailable
- GitHub API is broken or unavailable
- Configuring Git remotes fails
- Git fetching fails
- Checking out remote branch fails
- Git merge fails
- Git push to remote fails

### References

- http://aws.amazon.com/blogs/compute/writing-aws-lambda-functions-in-scala/
- http://aws.amazon.com/blogs/compute/dynamic-github-actions-with-aws-lambda/
- http://eclipse.org/jgit/
- http://github.com/code-check/github-api-scala/
- http://www.jcraft.com/jsch/
- http://github.com/ashawley/aws-lambda-scala-hello-world