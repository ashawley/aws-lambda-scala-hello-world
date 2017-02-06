package example

import scala.collection.JavaConverters._

/***
 * AWS Lambda interface for Java
 */
trait LambdaApp {

  /***
   * Handlers
   */
  def handler(): java.util.List[String]

  def cleanUp() = {}

  /***
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
      result = handler()
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
  }

  def escString(s: String) =
    "\"" + s.replaceAll("\"", "\\\"") + "\""
}
