package swagger

import akka.http.scaladsl.server.Directives._

class SwaggerUIService {
  val routes =
    logRequestResult("swagger-docs") {
      path("swagger") { getFromResource("swagger/index.html") } ~
        getFromResourceDirectory("swagger")
    }
}
