scalaVersion  := "2.11.8"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
  "io.code-check" %% "github-api" % "0.2.0-SNAPSHOT",
  "org.json4s"    %% "json4s-native" % "3.4.0"
)
