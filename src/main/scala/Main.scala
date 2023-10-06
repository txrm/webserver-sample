import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._


object Main {
  def main(args: Array[String]): Unit = {
    implicit val actorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "akka-http")

    val route = get {
      path("hello") {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "Hello from AKka server"))
      }
    }

    Http().newServerAt("localhost", 12345).bind(route)
  }
}