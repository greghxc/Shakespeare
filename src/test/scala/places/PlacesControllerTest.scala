package places

import clients.GooglePlacesClient
import com.github.javafaker.Faker
import models.DistanceResult
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

  val expectedOrigin = faker.address().fullAddress()
  val expectedDestination = faker.address().fullAddress()
  val expectedAddressResult = any.addressResult
  val expectedDistance = any.distance
  val expectedOtherDistance = any.distance

  val mockPlacesClient = mock[GooglePlacesClient]

  val controller = new PlacesController(mockPlacesClient)

  describe("#getDistanceResult") {
    it("returns expected distance result") {
      val expectedDistanceResult = new DistanceResult(expectedDistance.inMeters, metersToMiles(expectedDistance.inMeters), Some(expectedAddressResult), Some(expectedAddressResult))
      when(mockPlacesClient.getAddress(expectedOrigin)).thenReturn(Future.successful(expectedAddressResult))
      when(mockPlacesClient.getAddress(expectedDestination)).thenReturn(Future.successful(expectedAddressResult))
      when(mockPlacesClient.getDistance(expectedOrigin, expectedDestination)).thenReturn(Future.successful(expectedDistance))
      when(mockPlacesClient.getDistance(expectedDestination, expectedOrigin)).thenReturn(Future.successful(expectedDistance))

      controller.getDistanceResult(expectedOrigin, expectedDestination).futureValue shouldEqual(expectedDistanceResult)
    }

    it("averages distance both ways") {
      val expectedDistanceResult = new DistanceResult(expectedDistance.inMeters, metersToMiles(expectedDistance.inMeters), Some(expectedAddressResult), Some(expectedAddressResult))
      when(mockPlacesClient.getAddress(expectedOrigin)).thenReturn(Future.successful(expectedAddressResult))
      when(mockPlacesClient.getAddress(expectedDestination)).thenReturn(Future.successful(expectedAddressResult))
      when(mockPlacesClient.getDistance(expectedOrigin, expectedDestination)).thenReturn(Future.successful(expectedDistance))
      when(mockPlacesClient.getDistance(expectedDestination, expectedOrigin)).thenReturn(Future.successful(expectedOtherDistance))

      controller.getDistanceResult(expectedOrigin, expectedDestination).futureValue shouldEqual(controller.getDistanceResult(expectedDestination, expectedOrigin).futureValue)
    }
  }

  def metersToMiles(meters: BigDecimal): BigDecimal = {
    val metersInMiles: BigDecimal = 1609.34
    (meters / metersInMiles).setScale(2, RoundingMode.FLOOR)
  }
}
