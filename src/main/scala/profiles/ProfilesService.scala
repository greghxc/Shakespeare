package profiles

import javax.ws.rs.Path

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.swagger.annotations.{Api, _}
import models.{FuelSurchargeProfile, QuoteProfile}

@Api(value = "/profiles", produces = "application/json")
@Path("/profiles")
class ProfilesService {
  val profilesController: ProfilesController = ProfilesControllerSingleton

  val routes: Route = {
    logRequestResult("profiles-service") {
      pathPrefix("profiles") {
        quoteRoute ~ surchargeRoute
      }
    }
  }

  @Path("/quote")
  @ApiOperation(httpMethod = "GET", response = classOf[Seq[QuoteProfile]], value = "Returns active quote profiles")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Return active quote profiles", response = classOf[Seq[QuoteProfile]]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def quoteRoute = {
    pathPrefix("quote") {
      get {
        complete {
          profilesController.getQuoteProfiles()
        }
      }
    }
  }

  @Path("/surcharge")
  @ApiOperation(httpMethod = "GET", response = classOf[FuelSurchargeProfile], value = "Returns active surcharge profile")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Return active surcharge profile", response = classOf[FuelSurchargeProfile]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def surchargeRoute = {
    pathPrefix("surcharge") {
      get {
        complete {
          profilesController.getSurchargeProfile()
        }
      }
    }
  }
}

