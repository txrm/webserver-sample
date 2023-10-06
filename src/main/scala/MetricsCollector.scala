import scala.concurrent.Future
import scala.sys.process._
import scala.concurrent.ExecutionContext.Implicits.global

object MetricsCollector {
  def collectSystemMetrics(): Future[String] = Future {
    try {
      val output = new StringBuilder

      val process = Process("top -b -n 1").run(ProcessLogger(line => output.append(line).append("\n")))

      val exitCode = process.exitValue()

      if (exitCode == 0) {
        // Create JSON string with the collected metrics
        val metric =
          s"""{
            "topOutput": "${output.toString.trim}"
          }"""
        metric
      } else {
        throw new RuntimeException(s"Error running 'top' command (exit code: $exitCode)")
      }
    } catch {
      case ex: Exception => throw new RuntimeException("Error getting system metrics, check OS", ex)
    }
  }
}
