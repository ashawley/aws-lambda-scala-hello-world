scalaVersion  := "2.11.8"

name := "aws-gh-prs"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "io.verizon.knobs" %% "typesafe" % "3.12.27a",
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
  "com.ning" % "async-http-client" % "1.9.21",
  "io.code-check" %% "github-api" % "0.2.0-SNAPSHOT",
  "org.json4s"    %% "json4s-native" % "3.5.0",
  "org.eclipse.jgit" % "org.eclipse.jgit" % "4.5.0.201609210915-r",
  // "com.jcraft" % "jsch" % "0.1.53",
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
  // "org.slf4j" % "slf4j-log4j12" % "1.7.22",
  "org.slf4j" % "slf4j-api" % "1.7.22",
  "org.scala-sbt" %% "io" % "1.0.0-M7"
)

mergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "prs"
  )
