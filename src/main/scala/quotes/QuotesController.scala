package quotes

import models._
import places.{PlacesController, PlacesControllerSingleton}

import scala.concurrent.{ExecutionContext, Future}

object QuotesControllerSingleton extends QuotesController (
  vehicles = FareConfig.vehicles,
  placesController = PlacesControllerSingleton,
  quoteBuilder = QuoteBuilderSingleton,
  fuelSurchargeProfile = FareConfig.fuelSurchargeProfile
)

class QuotesController(vehicles: List[QuoteProfile], placesController: PlacesController, quoteBuilder: QuoteBuilder, fuelSurchargeProfile: FuelSurchargeProfile) {
  def buildQuoteResponse(origin: String, destination: String)(implicit ec: ExecutionContext): Future[QuoteResult] = {
    val eventualDistance = placesController.getDistanceResult(origin, destination)
    for {
      d <- eventualDistance
      result = QuoteResult(
        tripDetails = d,
        quotes = for {
          v <- vehicles
          quote = quoteBuilder.buildQuote(v, d, fuelSurchargeProfile)
        } yield quote
      )
    } yield result
  }
}
