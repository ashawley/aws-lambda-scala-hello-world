scalaVersion  := "2.11.12"

name := "aws-gh-prs"

version := "0.4-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.9.5" % "test",
  "org.specs2" %% "specs2-mock" % "3.9.5" % "test",
  "org.specs2" %% "specs2-scalacheck" % "3.9.5" % "test",
  "io.verizon.knobs" %% "typesafe" % "4.0.31-scalaz-7.2",
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
  "com.amazonaws" % "aws-lambda-java-events" % "2.0.2",
  "com.amazonaws" % "aws-lambda-java-log4j" % "1.0.0",
  "com.ning" % "async-http-client" % "1.9.40",
  "io.code-check" %% "github-api" % "0.2.0",
  "org.json4s"    %% "json4s-native" % "3.5.3",
  "org.eclipse.jgit" % "org.eclipse.jgit" % "4.10.0.201712302008-r",
  "org.eclipse.jgit" % "org.eclipse.jgit.junit" % "4.10.0.201712302008-r" % "test",
  // "com.jcraft" % "jsch" % "0.1.54",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "org.slf4j" % "slf4j-log4j12" % "1.7.25",
  "org.slf4j" % "slf4j-nop" % "1.7.25" % "test", // See also [1] below
  "org.scala-sbt" %% "io" % "1.1.4"
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
