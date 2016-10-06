## AWS Lambda Scala Hello World

The smallest example of using Scala with AWS Lambda.

Create a build.sbt with the following:

```scala
scalaVersion  := "2.12.0-RC1"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"
)
```

Create source file, `Main.scala`, that writes to standard out, and
returns a result.

```scala
package example

import scala.collection.JavaConverters._

object Main extends LambdaApp  {

  def handler()  = {

    println("Hello Cloudwatch")

    List("Hello Lambda").toSeq.asJava
  }
}
```

The `LambdaApp` trait is defined to enforce the type signature for AWS
Lambda functions.  It can also help with testing the function locally.

```
> run
[info] Running com.middil.Main 
Hello Cloudwatch
Hello Lambda
```

To run on AWS, run the task provided by
[SBT Assembly](http://github.com/sbt/sbt-assembly),

```
> assembly
[info] Including from cache: ion-java-1.0.1.jar
[info] Including from cache: aws-java-sdk-core-1.11.40.jar
[info] Including from cache: httpclient-4.5.2.jar
[info] Including from cache: commons-logging-1.1.3.jar
[info] Including from cache: httpcore-4.4.4.jar
[info] Including from cache: commons-codec-1.9.jar
[info] Including from cache: aws-lambda-java-events-1.0.0.jar
[info] Including from cache: joda-time-2.8.1.jar
[info] Including from cache: jmespath-java-1.0.jar
[info] Including from cache: aws-java-sdk-s3-1.11.40.jar
[info] Including from cache: jackson-databind-2.6.6.jar
[info] Including from cache: jackson-annotations-2.6.0.jar
[info] Including from cache: aws-java-sdk-sns-1.11.40.jar
[info] Including from cache: aws-java-sdk-kms-1.11.40.jar
[info] Including from cache: jackson-core-2.6.6.jar
[info] Including from cache: aws-lambda-java-core-1.0.0.jar
[info] Including from cache: jackson-dataformat-cbor-2.6.6.jar
[info] Including from cache: aws-java-sdk-sqs-1.11.40.jar
[info] Including from cache: scala-library-2.12.0-RC1.jar
[info] Including from cache: aws-java-sdk-cognitoidentity-1.11.40.jar
[info] Including from cache: aws-java-sdk-kinesis-1.11.40.jar
[info] Checking every *.class/*.jar file's SHA-1.
[info] Merging files...
[info] SHA-1: 169142dab8b01e507e8e312a95b335a21cae9c8f
[info] Packaging target/scala-2.12.0-RC1/scala-hello-world-assembly-0.1-SNAPSHOT.jar ...
[info] Done packaging.
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

1. Click "Test"

### References

- https://aws.amazon.com/blogs/compute/writing-aws-lambda-functions-in-scala/
