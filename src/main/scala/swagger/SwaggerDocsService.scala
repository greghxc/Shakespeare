package swagger

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.github.swagger.akka.{HasActorSystem, SwaggerHttpService}
import com.github.swagger.akka.model.Info
import places.PlacesService
import profiles.ProfilesService
import quotes.QuotesService

import scala.reflect.runtime.universe._
import scala.util.Properties

class SwaggerDocsService(system: ActorSystem) extends SwaggerHttpService with HasActorSystem {
  override implicit val actorSystem: ActorSystem = system
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override val apiTypes = Seq(typeOf[PlacesService], typeOf[ProfilesService], typeOf[QuotesService])
  override val host = Properties.envOrElse("swaggerHost", "localhost:9000")
  override val basePath = "/"
  override val apiDocsPath = "api-docs"
  override val info = Info()
}