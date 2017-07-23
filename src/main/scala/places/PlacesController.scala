package places

import clients.{GooglePlacesClient, GooglePlacesClientSingleton}
import models.{AddressResult, DistanceResult}

import scala.concurrent.{ExecutionContext, Future}
import scala.math.BigDecimal.RoundingMode

object PlacesControllerSingleton extends PlacesController(GooglePlacesClientSingleton)

class PlacesController(placesClient: GooglePlacesClient) {

  def getDistanceResult(origin: String, destination: String)(implicit ec: ExecutionContext): Future[DistanceResult] = {
    val eventualOrigin = placesClient.getAddress(origin)
    val eventualDestination = placesClient.getAddress(destination)
    val eventualDistanceTo = placesClient.getDistance(origin, destination)
    val eventualDistanceFrom = placesClient.getDistance(destination, origin)
    val metersInMiles: BigDecimal = 1609.34
    for {
      distanceTo <- eventualDistanceTo
      distanceFrom <- eventualDistanceFrom
      distance = BigDecimal((distanceTo.inMeters + distanceFrom.inMeters) / 2.0)
      origin <- eventualOrigin
      destination <- eventualDestination
      miles: BigDecimal = (distance / metersInMiles).setScale(2, RoundingMode.FLOOR)
      result = DistanceResult(distance.toLong, miles, Some(origin), Some(destination))
    } yield result
  }

  def getAddress(address: String)(implicit ec: ExecutionContext): Future[AddressResult] = {
    placesClient.getAddress(address)
  }
}
