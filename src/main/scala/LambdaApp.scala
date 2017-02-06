package prs

import com.amazonaws.services.lambda.runtime.events.SNSEvent

import scala.collection.JavaConverters._

/**
 * AWS Lambda interface for Java
 */
trait LambdaApp {

  /**
   * Handlers
   */
  def handler(e: SNSEvent): java.util.List[String]

  def cleanUp() = {}

  /**
   * Trimmed down example from
   * http://developer.github.com/v3/activity/events/types/#pullrequestevent
   */
  def pullRequestEvent: String =
    """{
      |  "action": "opened",
      |  "number": 1,
      |  "pull_request": {
      |    "state": "open",
      |    "head": {
      |      "ref": "changes",
      |      "sha": "0d1a26e67d8f5eaf1f6ba5c57fc3c7d91ac0fd1c",
      |      "repo": {
      |        "name": "public-repo",
      |        "owner": {
      |          "login": "baxterthehacker"
      |        }
      |      }
      |    },
      |    "base": {
      |      "ref": "master",
      |      "repo": {
      |        "name": "public-repo",
      |        "owner": {
      |          "login": "baxterthehacker"
      |        }
      |      }
      |    }
      |  }
      |}""".stripMargin

  // {
  //   "Records": [
  //     {
  //       "EventVersion": "1.0",
  //       "EventSubscriptionArn": "arn:aws:sns:EXAMPLE",
  //       "EventSource": "aws:sns",
  //       "Sns": {
  //         "SignatureVersion": "1",
  //         "Timestamp": "1970-01-01T00:00:00.000Z",
  //         "Signature": "EXAMPLE",
  //         "SigningCertUrl": "EXAMPLE",
  //         "MessageId": "95df01b4-ee98-5cb9-9903-4c221d41eb5e",
  //         "Message": "{\"action\":\"opened\",\"number\":1,\"pull_request\":{...}}",
  //         "MessageAttributes": {
  //           "X-Github-Event": {
  //             "Type": "String",
  //             "Value": "pull_request"
  //           }
  //         },
  //         "Type": "Notification",
  //         "UnsubscribeUrl": "EXAMPLE",
  //         "TopicArn": "arn:aws:sns:EXAMPLE",
  //         "Subject": "TestInvoke"
  //       }
  //     }
  //   ]
  // }
  val snsEvent = {
    val m = new SNSEvent.MessageAttribute
    m.setType("String")
    m.setValue("pull_request")
    val s = new SNSEvent.SNS
    s.setSignatureVersion("1")
    s.setTimestamp(new org.joda.time.DateTime("1970-01-01T00:00:00.000Z"))
    s.setSignature("EXAMPLE")
    s.setSigningCertUrl("EXAMPLE")
    s.setMessageId("95df01b4-ee98-5cb9-9903-4c221d41eb5e")
    s.setMessage(pullRequestEvent)
    s.setMessageAttributes(
      Map("X-Github-Event" -> m).asJava)
    s.setType("Notification")
    s.setUnsubscribeUrl("EXAMPLE")
    s.setTopicArn("arn:aws:sns:EXAMPLE")
    s.setSubject("TestInvoke")
    val r = new SNSEvent.SNSRecord
    r.setEventVersion("1.0")
    r.setEventSubscriptionArn("arn:aws:sns:EXAMPLE")
    r.setEventSource("aws:sns")
    r.setSns(s)
    val e = new SNSEvent
    e.setRecords(List(r).asJava)
    e
  }

  /**
   * Driver for testing handler locally
   */ 
  def main(args: Array[String]): Unit = {
    val uuid = java.util.UUID.randomUUID
    val version = "$LATEST"
    org.apache.log4j.MDC.put("AWSRequestId", uuid.toString)

    println(s"START RequestId: $uuid Version: $version")

    val t0 = System.nanoTime
    var result: java.util.List[String] = new java.util.ArrayList[String]()
    try {
      result = handler(snsEvent)
    } catch {
      case e: Throwable => e.printStackTrace
    } finally {
      cleanUp()
    }
    println(s"END RequestId: $uuid")

    val t1 = System.nanoTime
    val duration = (t1 - t0) / 1e6
    val billedDuration = scala.math.ceil(duration / 100d).toLong * 100

    val env = Runtime.getRuntime
    val memorySize = env.totalMemory / 1024 / 1024
    val memoryUsed = (env.totalMemory - env.freeMemory) / 1024 / 1024

    println(s"""REPORT RequestId: $uuid
                 |  Duration: ${duration} ms
                 |  Billed Duration: ${billedDuration} ms
                 |  Memory Size: ${memorySize} MB
                 |  Max Memory Used: ${memoryUsed} MB""".stripMargin
      .replaceAll("\n", ""))

    println("[")
    println("  " + result.asScala.map(escString(_)).mkString(",\n  "))
    println("]")

    cleanUp()
  }

  def escString(s: String) =
    "\"" + s.replaceAll("\"", "\\\"") + "\""
}
