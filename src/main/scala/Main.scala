package example

import scala.collection.JavaConverters._

import org.eclipse.jgit.api.Git

import sbt.io.IO
import sbt.io.Path

object Main extends LambdaApp  {

  val gitPath =Path("/tmp/aws-lambda-scala-hello-world")

  def handler()  = {

    val git: Unit = Git.cloneRepository()
      .setURI("https://github.com/ashawley/aws-lambda-scala-hello-world.git")
      .setDirectory(gitPath.asFile)
      .call()

    List("Hello Lambda").toSeq.asJava
  }

  /**
    * Used by LambdaApp when run locally repeatedly
    */
  override def cleanUp() = {
    IO.delete(gitPath.asFile)
  }

}
