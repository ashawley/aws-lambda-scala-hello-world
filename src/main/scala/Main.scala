package example

import scala.collection.JavaConverters._

import com.typesafe.scalalogging

object Main extends LambdaApp with scalalogging.StrictLogging {

  def handler()  = {

    logger.info("Hello Cloudwatch")

    List("Hello Lambda").toSeq.asJava
  }
}
