package example

import codecheck.github.events.GitHubEvent

import com.amazonaws.services.lambda.runtime.events.SNSEvent

import org.json4s._
import org.json4s.native.JsonMethods._

import scala.collection.JavaConverters._

object Main extends LambdaApp  {

  def safeList[A](xs: java.util.List[A]) =
    Option(xs).map(_.asScala).getOrElse(List.empty[A])

  def handler(e: SNSEvent)  = {

    val rs = for {
      r <- safeList(e.getRecords)
      (key, attribute) <- r.getSNS.getMessageAttributes.asScala
      if key == "X-GitHub-Event"
    } yield {
      GitHubEvent(attribute.getValue, parse(r.getSNS.getMessage))
    }

    rs.map(_.name).asJava
  }
}
