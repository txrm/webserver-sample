import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._

import sys.process._
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("MetricsServer")


    val metricsData = mutable.Map[String, Long]()

    // Routes
    val route = path("metrics") {
      get {
        onComplete(collectSystemMetrics()) {
          case Success(metrics) =>
            metricsData.put("systemMetrics", System.currentTimeMillis())
            complete(HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, metrics)))
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


  private def collectSystemMetrics(): scala.concurrent.Future[String] = scala.concurrent.Future {
    try {
      val cpuUsage = "top -b -n 1 | grep Cpu | awk '{print $2}'".!!.trim
      val memoryUsage = "free | grep Mem | awk '{print $3}'".!!.trim

      // Create JSON string with the collected metrics
      val metric =
        s"""{
          "cpuUsage": "$cpuUsage",
          "memoryUsage": "$memoryUsage"
        }"""
      metric
    } catch {
      case ex: Exception => throw new RuntimeException("Error collecting system metrics", ex)
    }
  }
}
