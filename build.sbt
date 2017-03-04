scalaVersion  := "2.11.8"

name := "aws-gh-prs"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.8.8" % "test",
  "org.specs2" %% "specs2-mock" % "3.8.8" % "test",
  "org.specs2" %% "specs2-scalacheck" % "3.8.8" % "test",
  "io.verizon.knobs" %% "typesafe" % "3.12.27a",
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
  "com.amazonaws" % "aws-lambda-java-log4j" % "1.0.0",
  "com.ning" % "async-http-client" % "1.9.21",
  "io.code-check" %% "github-api" % "0.2.0-SNAPSHOT",
  "org.json4s"    %% "json4s-native" % "3.5.0",
  "org.eclipse.jgit" % "org.eclipse.jgit" % "4.6.0.201612231935-r",
  "org.eclipse.jgit" % "org.eclipse.jgit.junit" % "4.6.0.201612231935-r" % "test",
  // "com.jcraft" % "jsch" % "0.1.53",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.slf4j" % "slf4j-log4j12" % "1.7.22",
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
