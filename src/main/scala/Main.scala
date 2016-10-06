package example

import scala.collection.JavaConverters._

object Main extends LambdaApp  {

  def handler()  = {

    println("Hello Cloudwatch")

    List("Hello Lambda").toSeq.asJava
  }
}
