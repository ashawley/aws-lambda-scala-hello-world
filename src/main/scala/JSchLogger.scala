package prs

import com.typesafe.scalalogging
import com.jcraft.jsch

class JSchLogger(levelEnabled: Int) extends jsch.Logger {

  lazy val logger = scalalogging.slf4j.Logger(
    org.slf4j.LoggerFactory.getLogger(classOf[jsch.JSch])
  )

  def isEnabled(level: Int): Boolean =
    levelEnabled <= level

  def log(level: Int, message: String): Unit = {
    level match {
      case jsch.Logger.DEBUG => logger.debug(message)
      case jsch.Logger.ERROR => logger.error(message)
      case jsch.Logger.FATAL => logger.trace(message)
      case jsch.Logger.INFO  => logger.info(message)
      case jsch.Logger.WARN  => logger.warn(message)
    }
  }
}
