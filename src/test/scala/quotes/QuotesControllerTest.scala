package quotes

import models.{AddressResult, DistanceResult, Quote, QuoteResult}
import org.scalatest.{FunSuite, Matchers, path}
import places.PlacesController
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import test.{Any, AnySingleton}
import org.mockito.Mockito.verify;
import org.mockito.Mockito.when;

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class QuotesControllerTest extends path.FunSpec with Matchers with MockitoSugar with ScalaFutures {
  val any = AnySingleton

  val mockPlacesController =  mock[PlacesController]
  val mockQuoteBuilder = mock[QuoteBuilder]

  val expectedOrigin = "123 24th Ave, Seattle, WA"
  val expectedDestination = "123 24th Ave, Seattle, WA"
  val expectedDistanceResult = any.distanceResult

  val expectedVehicle1 = any.quoteProfile
  val expectedVehicle2 = any.quoteProfile

  val expectedQuote1 = any.quote
  val expectedQuote2 = any.quote

  val expectedVehicles = List(expectedVehicle1, expectedVehicle2)
  val expectedQuotes = List(expectedQuote1, expectedQuote2)

  val expectedFuelSurchargeProfile = any.fuelSurchargeProfile

  val expectedQuoteResult = QuoteResult(
    tripDetails = expectedDistanceResult,
    quotes = expectedQuotes
  )

  val controller = new QuotesController(expectedVehicles, mockPlacesController, mockQuoteBuilder, expectedFuelSurchargeProfile)

  describe(".buildQuote") {
    it("builds expected quote") {
      val eventualDistance = Future.successful(
        expectedDistanceResult
      )

      when(mockPlacesController.getDistanceResult(expectedOrigin, expectedDestination)).thenReturn(eventualDistance)
      when(mockQuoteBuilder.buildQuote(expectedVehicle1, expectedDistanceResult, expectedFuelSurchargeProfile)).thenReturn(expectedQuote1)
      when(mockQuoteBuilder.buildQuote(expectedVehicle2, expectedDistanceResult, expectedFuelSurchargeProfile)).thenReturn(expectedQuote2)

      controller.buildQuoteResponse(expectedOrigin, expectedDestination).futureValue shouldEqual(expectedQuoteResult)
    }
  }
}
