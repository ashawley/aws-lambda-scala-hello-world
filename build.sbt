scalaVersion  := "2.11.11"

name := "aws-gh-prs"

version := "0.2-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.8.9" % "test",
  "org.specs2" %% "specs2-mock" % "3.8.9" % "test",
  "org.specs2" %% "specs2-scalacheck" % "3.8.9" % "test",
  "io.verizon.knobs" %% "typesafe" % "4.0.31-scalaz-7.2",
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
  "com.amazonaws" % "aws-lambda-java-log4j" % "1.0.0",
  "com.ning" % "async-http-client" % "1.9.40",
  "io.code-check" %% "github-api" % "0.2.0",
  "org.json4s"    %% "json4s-native" % "3.5.2",
  "org.eclipse.jgit" % "org.eclipse.jgit" % "4.7.0.201704051617-r",
  "org.eclipse.jgit" % "org.eclipse.jgit.junit" % "4.7.0.201704051617-r" % "test",
  // "com.jcraft" % "jsch" % "0.1.54",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.slf4j" % "slf4j-log4j12" % "1.7.25",
  "org.slf4j" % "slf4j-nop" % "1.7.25" % "test", // See also [1] below
  "org.scala-sbt" %% "io" % "1.0.0-M11"
)

// 1. Favor slf4j-nop for Test over slf4j-log4j12 for Runtime
dependencyClasspath in Test := {
  (dependencyClasspath in Test).value.filter {
    _.get(moduleID.key) exists (_.name != "slf4j-log4j12")
  }
}

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
