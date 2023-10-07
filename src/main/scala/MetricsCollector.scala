import scala.concurrent.Future
import scala.sys.process._
import scala.concurrent.ExecutionContext.Implicits.global


object MetricsCollector {
  def collectSystemMetrics(): Future[String] = Future {
    // Run the 'top' command and capture its output
    // I have a Mac so to test I used the -l argument in testing instead of the -b
    val output = Process("top -n 1 -b").!!.trim
    output
  }
}
