## AWS Lambda Scala Hello World with Logging

The smallest example of using Scala on AWS Lambda with logging.

Create a build.sbt with the following:

```scala
scalaVersion  := "2.12.1"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-lambda-java-log4j" % "1.0.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.slf4j" % "slf4j-log4j12" % "1.7.22"
)
```

Create source file, `Main.scala`, that writes to standard out, and
returns a result.

```scala
package example

import scala.collection.JavaConverters._

import com.typesafe.scalalogging

object Main extends LambdaApp with scalalogging.StrictLogging {

  def handler()  = {

    logger.info("Hello Cloudwatch")

    List("Hello Lambda").toSeq.asJava
  }
}
```

The `LambdaApp` trait is defined to enforce the type signature for AWS
Lambda functions.  It can also help with testing the function locally.

```
> run
[info] Running example.Main
START RequestId: 82c9bda8-a14e-4436-8805-f6a15f2aa238 Version: $LATEST
15:53:35.958 82c9bda8-a14e-4436-8805-f6a15f2aa238 [run-main-0] INFO  example.Main$:11 - Hello Cloudwatch
END RequestId: 82c9bda8-a14e-4436-8805-f6a15f2aa238
REPORT RequestId: 82c9bda8-a14e-4436-8805-f6a15f2aa238  Duration: 133.473084 ms  Memory Size: 119 MB  Max Memory Used: 23 MB
[
  "Hello Lambda"
]
```

To run on AWS, run the task provided by
[SBT Assembly](http://github.com/sbt/sbt-assembly),

```
> assembly
[info] Including: aws-lambda-java-log4j-1.0.0.jar
[info] Including: scala-logging_2.12-3.5.0.jar
[info] Including: slf4j-log4j12-1.7.22.jar
[info] Including: slf4j-api-1.7.22.jar
[info] Including: log4j-1.2.17.jar
[info] Including: aws-lambda-java-core-1.1.0.jar
[info] Including: scala-reflect-2.12.1.jar
[info] Including: scala-library-2.12.1.jar
[info] Checking every *.class/*.jar file's SHA-1.
[info] Merging files...
[info] SHA-1: 169142dab8b01e507e8e312a95b335a21cae9c8f
[info] Packaging target/scala-2.12.1/aws-lambda-scala-hello-world-assembly-0.1-SNAPSHOT.jar ...
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
- http://docs.aws.amazon.com/lambda/latest/dg/java-logging.html