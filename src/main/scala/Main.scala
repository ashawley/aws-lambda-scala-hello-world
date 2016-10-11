package example

import com.amazonaws.services.lambda.runtime.events.SNSEvent

import scala.collection.JavaConverters._

object Main extends LambdaApp  {

  def safeList[A](xs: java.util.List[A]) =
    Option(xs).map(_.asScala).getOrElse(List.empty[A])

  def handler(e: SNSEvent)  = {

    val rs = for {
      r <- safeList(e.getRecords)
    } yield {
      r.getSNS.getMessage
    }
    rs.asJava
  }
}
