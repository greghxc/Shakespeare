package places

import clients.{GooglePlacesClient, GooglePlacesClientSingleton}
import com.google.maps.model.DistanceMatrix
import models.{AddressResult, DistanceMatrixResult, DistanceResult}

import scala.concurrent.{ExecutionContext, Future}
import scala.math.BigDecimal.RoundingMode

object PlacesControllerSingleton extends PlacesController(GooglePlacesClientSingleton)

class PlacesController(placesClient: GooglePlacesClient) {

  def getDistanceResult(origin: String, destination: String)(implicit ec: ExecutionContext): Future[DistanceResult] = {
    val eventualAddresses = placesClient.getAddresses(origin, destination)
    val eventualMatrix = placesClient.getDistanceMatrix(origin, destination)

    val metersInMiles: BigDecimal = 1609.34

    for {
      matrix <- eventualMatrix
      distance = getAverageDistance(matrix)
      addresses <- eventualAddresses
      origin = addresses(0)
      destination = addresses(1)
      miles: BigDecimal = (distance / metersInMiles).setScale(2, RoundingMode.FLOOR)
      minutes: Int = getMinutes(matrix)
      result = DistanceResult(distance.toLong, miles, minutes, Some(origin), Some(destination))
    } yield result
  }

  def getAverageDistance(matrix: DistanceMatrixResult): BigDecimal = {
    val distanceForward: BigInt = matrix.forwardDistance.inMeters
    val distanceReverse: BigInt = matrix.reverseDistance.inMeters
    BigDecimal((distanceForward + distanceReverse) / 2)
  }

  def getMinutes(matrix: DistanceMatrixResult): Int = {
    val minuteInSeconds: Int = 60
    val secondsForward: BigInt = matrix.duration.inSeconds
    (secondsForward / minuteInSeconds).toInt
  }

  def getAddress(address: String)(implicit ec: ExecutionContext): Future[AddressResult] = {
    placesClient.getAddress(address)
  }
}
