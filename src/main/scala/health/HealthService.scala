package health

import javax.ws.rs.Path

import akka.http.scaladsl.model.{HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import clients.GooglePlacesClientSingleton

class HealthService {
  val client = GooglePlacesClientSingleton

  val routes: Route = {
    logRequestResult("distance-service") {
      pathPrefix("health") {
        googleRoute
      }
    }
  }

  def googleRoute: Route = {
    get {
      complete {
        HttpResponse(entity = "googleApiKey present: " + client.apiKey.isEmpty)
      }
    }
  }
}