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

    // val event = new S3Event(new java.util.ArrayList())

    println(s"START RequestId: $uuid Version: $version")

    val t0 = System.nanoTime
    val result = handler()
    val t1 = System.nanoTime
    val duration = (t1 - t0) / 1000.0

    val env = Runtime.getRuntime
    val memorySize = env.totalMemory / 1024 / 1024
    val memoryUsed = (env.totalMemory - env.freeMemory) / 1024 / 1024

    println(s"END RequestId: $uuid  Duration: ${duration} ms  Memory Size: ${memorySize} MB  Max Memory Used: ${memoryUsed} MB")

    println("[")
    println("  " + result.asScala.map(escString(_)).mkString(",\n  "))
    println("]")

    cleanUp()
  }

  def escString(s: String) =
    "\"" + s.replaceAll("\"", "\\\"") + "\""
}
