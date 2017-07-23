package quotes

import javax.ws.rs.Path

import akka.http.scaladsl.server.{Directive0, Route}
import akka.http.scaladsl.server.Directives._
import io.swagger.annotations._
import models.{DistanceResult, QuoteResult}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent.ExecutionContext.Implicits.global

@Api(value = "/quote", produces = "application/json")
@Path("/quote")
class QuotesService {
  val quotesController = QuotesControllerSingleton

  val routes: Route = {
    logRequestResult("quote-service") {
      pathPrefix("quote") {
        quoteRoute
      }
    }
  }

  @ApiOperation(httpMethod = "GET", response = classOf[DistanceResult], value = "Returns a quote")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "origin", value = "Origin address", required = true, dataType = "string", paramType = "query"),
    new ApiImplicitParam(name = "destination", value = "Destination address", required = true, dataType = "string", paramType = "query")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Returns a quote", response = classOf[QuoteResult]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def quoteRoute = {
    cors() {
      get {
        parameters('origin, 'destination) { (origin, destination) =>
          complete {
            quotesController.buildQuoteResponse(origin, destination)
          }
        }
      }
    }
  }
}