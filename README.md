## AWS Lambda and SNS notifications with Scala

The smallest example of using Scala with AWS Lambda and Simple
Notification Service (SNS)

Create a build.sbt with the following:

```scala
scalaVersion  := "2.12.0-RC1"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-lambda-java-events" % "1.3.0"
)
```

Create source file, `Main.scala`, that processes an `SNSEvent`, and
returns a list.

```scala
package example

import com.amazonaws.services.lambda.runtime.events.SNSEvent

import scala.collection.JavaConverters._

import com.amazonaws.services.lambda.runtime.events.SNSEvent

object Main extends LambdaApp  {

  def handler(e: SNSEvent)  = {

    val rs = for {
      r <- safeList(e.getRecords)
    } yield {
      r.getSNS.getMessage
    }
    rs.asJava
  }
}
```

The `LambdaApp` trait is defined to enforce the type signature for AWS
Lambda functions.  It can also help with testing the function locally.

```
> run
[info] Running example.Main
START RequestId: bf128863-6af3-498a-8e6c-9ac7ad285740 Version: $LATEST
END RequestId: bf128863-6af3-498a-8e6c-9ac7ad285740
REPORT RequestId: bf128863-6af3-498a-8e6c-9ac7ad285740 Duration: 2.048983 ms  Memory Size: 2042 MB  Max Memory Used: 715 MB
[
  "Hello from SNS!"
]
[success] Total time: 4 s, completed Oct 12, 2016 10:06:12 AM
```

To run on AWS, run the task provided by
[SBT Assembly](http://github.com/sbt/sbt-assembly),

```
> assembly
[info] Including from cache: aws-java-sdk-sns-1.11.0.jar
[info] Including from cache: aws-java-sdk-cognitoidentity-1.11.0.jar
[info] Including from cache: jackson-databind-2.5.3.jar
[info] Including from cache: aws-java-sdk-sqs-1.11.0.jar
[info] Including from cache: aws-java-sdk-kinesis-1.11.0.jar
[info] Including from cache: jackson-annotations-2.5.0.jar
[info] Including from cache: jackson-core-2.5.3.jar
[info] Including from cache: jackson-dataformat-cbor-2.5.3.jar
[info] Including from cache: aws-java-sdk-dynamodb-1.11.0.jar
[info] Including from cache: aws-lambda-java-events-1.3.0.jar
[info] Including from cache: joda-time-2.8.1.jar
[info] Including from cache: aws-lambda-java-core-1.1.0.jar
[info] Including from cache: aws-java-sdk-s3-1.11.0.jar
[info] Including from cache: aws-java-sdk-kms-1.11.0.jar
[info] Including from cache: httpcore-4.4.4.jar
[info] Including from cache: httpclient-4.5.2.jar
[info] Including from cache: aws-java-sdk-core-1.11.0.jar
[info] Including from cache: commons-logging-1.1.3.jar
[info] Including from cache: commons-codec-1.9.jar
[info] Including from cache: scala-library-2.12.0-RC1.jar
[info] Checking every *.class/*.jar file's SHA-1.
[info] Merging files...
[info] SHA-1: 502aa723ff628dd6568883c13ec47cdaf93a4b71
[info] Packaging target/scala-2.12.1/aws-lambda-scala-hello-world-assembly-0.1-SNAPSHOT.jar ...
[info] Done packaging.
[success] Total time: 40 s, completed Oct 12, 2016 10:08:24 AM
```

### Uploading to AWS Lambda

1. From the [AWS console](https://console.aws.amazon.com), click on
"Configure function" from the Lambda service.

1. For name, give "ScalaHelloWorld".  Leave the description blank.

1. Choose "Java 8" as the runtime.

1. Upload the JAR file created by SBT assembly at
`target/scala-x.y.z/package-0.1-SNAPSHOT.jar`.

1. Enter the handler as, `example.Main::handler`

1. Choose "Create new role from template(s)".

1. Enter "LambdaTestRole" as the role name.

1. Leave "Policy templates" blank.

1. Reduce the memory to 128 MB, and leave the timeout at 15 seconds.

1. Keep the setting for VPC to none.

1. Click "Next".

1. Click "Create function"

1. Click "Actions" and then "Configure test event"

1. From the "Sample event template" drop-down, choose "SNS"

1. Click "Save and Test"

### References

- https://aws.amazon.com/blogs/compute/writing-aws-lambda-functions-in-scala/
- https://aws.amazon.com/blogs/compute/dynamic-github-actions-with-aws-lambda/
