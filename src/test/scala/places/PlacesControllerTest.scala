package places

import clients.GooglePlacesClient
import com.github.javafaker.Faker
import com.google.maps.model.{Distance, DistanceMatrix, Duration}
import models.{AddressResult, DistanceMatrixResult, DistanceResult}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FunSpec, Matchers}
import test.AnySingleton
import org.mockito.Mockito.when

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.math.BigDecimal.RoundingMode

class PlacesControllerTest extends FunSpec with Matchers with MockitoSugar with ScalaFutures {
  val any = AnySingleton
  val faker = new Faker()

  val expectedOrigin: String = faker.address().fullAddress()
  val expectedDestination: String = faker.address().fullAddress()
  val expectedAddressResult: AddressResult = any.addressResult
  val expectedDistance: Distance = any.distance
  val expectedOtherDistance: Distance = any.distance
  val expectedDuration: Duration = any.duration
  val expectedMinutes: Int = (expectedDuration.inSeconds / 60).toInt

  val mockPlacesClient: GooglePlacesClient = mock[GooglePlacesClient]
  val controller = new PlacesController(mockPlacesClient)

  describe("#getDistanceResult") {
    it("returns expected distance result") {
      val expectedDistanceMatrixResult: DistanceMatrixResult =
        DistanceMatrixResult(expectedDistance, expectedDistance, expectedDuration)

      val expectedDistanceResult =
        new DistanceResult(
          expectedDistance.inMeters,
          metersToMiles(expectedDistance.inMeters),
          expectedMinutes,
          Some(expectedAddressResult),
          Some(expectedAddressResult)
        )

      when(mockPlacesClient.getAddresses(expectedOrigin, expectedDestination))
        .thenReturn(Future.successful(Seq(expectedAddressResult, expectedAddressResult)))
      when(mockPlacesClient.getDistanceMatrix(expectedOrigin, expectedDestination))
        .thenReturn(Future.successful(expectedDistanceMatrixResult))

      controller.getDistanceResult(expectedOrigin, expectedDestination)
        .futureValue shouldEqual (expectedDistanceResult)
    }

    it("calculates same distance both ways") {
      val expectedDistanceMatrixResult: DistanceMatrixResult =
        DistanceMatrixResult(expectedDistance, expectedOtherDistance, expectedDuration)

      val expectedOtherDistanceMatrixResult: DistanceMatrixResult =
        DistanceMatrixResult(expectedOtherDistance, expectedDistance, expectedDuration)

      when(mockPlacesClient.getAddresses(expectedOrigin, expectedDestination))
        .thenReturn(Future.successful(Seq(expectedAddressResult, expectedAddressResult)))
      when(mockPlacesClient.getAddresses(expectedDestination, expectedOrigin))
        .thenReturn(Future.successful(Seq(expectedAddressResult, expectedAddressResult)))
      when(mockPlacesClient.getDistanceMatrix(expectedOrigin, expectedDestination))
        .thenReturn(Future.successful(expectedDistanceMatrixResult))
      when(mockPlacesClient.getDistanceMatrix(expectedDestination, expectedOrigin))
        .thenReturn(Future.successful(expectedOtherDistanceMatrixResult))

      controller.getDistanceResult(expectedOrigin, expectedDestination)
        .futureValue shouldEqual controller.getDistanceResult(expectedDestination, expectedOrigin).futureValue
    }
  }

  def metersToMiles(meters: BigDecimal): BigDecimal = {
    val metersInMiles: BigDecimal = 1609.34
    (meters / metersInMiles).setScale(2, RoundingMode.FLOOR)
  }
}
