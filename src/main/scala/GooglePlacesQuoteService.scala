import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import health.HealthService
import places.PlacesService
import profiles.ProfilesService
import quotes.QuotesService
import swagger.{SwaggerDocsService, SwaggerUIService}

object GooglePlacesQuoteService extends App with Directives {
  implicit val system = ActorSystem()
  val config = ConfigFactory.load()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  val logger = Logging(system, getClass)

  val placesService: PlacesService = new PlacesService
  val quotesService: QuotesService = new QuotesService
  val profilesService: ProfilesService = new ProfilesService
  val swaggerDocService: SwaggerDocsService = new SwaggerDocsService(system)
  val swaggerUIService: SwaggerUIService = new SwaggerUIService
  val healthService: HealthService = new HealthService

  val routes = placesService.routes ~ quotesService.routes ~ profilesService.routes ~ swaggerDocService.routes ~ swaggerUIService.routes ~ healthService.routes

  Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
}