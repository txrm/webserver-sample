import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("MetricsServer")

    val metricsData = mutable.Map[String, Long]()

    // Defining endpoint at root path
    val route = pathSingleSlash {
      get {
        onComplete(MetricsCollector.collectSystemMetrics()) {
          case Success(metrics) =>
            val prettifiedMetrics = MetricsVisualization.generateMetricsPage(metrics)
            metricsData.put("systemMetrics", System.currentTimeMillis())
            complete(HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, prettifiedMetrics)))
          case Failure(ex) =>
            complete(HttpResponse(StatusCodes.InternalServerError, entity = s"Error retrieving metrics: ${ex.getMessage}"))
        }
      }
    }

    val bindingFuture = Http().newServerAt("localhost", 8000).bind(route)

    bindingFuture
      .onComplete {
        case Success(binding) =>
          println(s"Server online at http://localhost:8000/")
        case Failure(ex) =>
          println(s"Server could not bind to port 8000: ${ex.getMessage}")
          system.terminate()
      }
  }
}
