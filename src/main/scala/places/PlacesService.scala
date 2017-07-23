package places

import javax.ws.rs.Path

import akka.http.scaladsl.server.{Directive0, Route}
import akka.http.scaladsl.server.Directives._
import io.swagger.annotations._
import models.DistanceResult
import scala.concurrent.ExecutionContext.Implicits.global

@Api(value = "/places", produces = "application/json")
@Path("/places")
class PlacesService {
  val placesController: PlacesController = PlacesControllerSingleton

  val routes: Route = {
    logRequestResult("distance-service") {
      pathPrefix("places") {
        distanceRoute ~ addressRoute
      }
    }
  }

  @Path("/distance")
  @ApiOperation(httpMethod = "GET", response = classOf[DistanceResult], value = "Returns a distance in miles")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "origin", value = "Origin address", required = true, dataType = "string", paramType = "query"),
    new ApiImplicitParam(name = "destination", value = "Destination address", required = true, dataType = "string", paramType = "query")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Return distance in miles", response = classOf[DistanceResult]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def distanceRoute = {
    pathPrefix("distance") {
      get {
        parameters('origin, 'destination) { (origin, destination) =>
          complete {
            placesController.getDistanceResult(origin, destination)
          }
        }
      }
    }
  }

  @Path("/address")
  @ApiOperation(httpMethod = "GET", response = classOf[String], value = "Returns a formatted address")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "address", value = "Address", required = true, dataType = "string", paramType = "query")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Return a formatted address", response = classOf[String]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def addressRoute = {
    pathPrefix("address") {
      get {
        parameters('address) { address =>
          complete {
            placesController.getAddress(address)
          }
        }
      }
    }
  }
}
