package quotes

import models._

object QuoteBuilderSingleton extends QuoteBuilder

class QuoteBuilder {
  def buildQuote(quoteProfile: QuoteProfile, distanceResult: DistanceResult, fuelSurchargeProfile: FuelSurchargeProfile): Quote =  {
    val fares = quoteProfile.fareProfiles.map(x => {
        val calculatedFare: BigDecimal = (x.baseFare + (x.perMile * distanceResult.miles)).setScale(2, BigDecimal.RoundingMode.HALF_UP)
        val fare:BigDecimal = calculatedFare.max(x.minimum).setScale(2, BigDecimal.RoundingMode.HALF_UP)
        val fuelSurcharge: BigDecimal = (calculatedFare * fuelSurchargeProfile.percentage).setScale(2, BigDecimal.RoundingMode.HALF_UP)
        val serviceCharge: BigDecimal = (fare * 0.2).setScale(2, BigDecimal.RoundingMode.HALF_UP)
        val total: BigDecimal = (fare + serviceCharge + fuelSurcharge).setScale(2, BigDecimal.RoundingMode.HALF_UP)
        new Fare(
          fareProfile = x.name,
          totalFare = total,
          fareDetail = s"Base Fare: $$${"%1.2f".format(fare)}\nFuel Surcharge: $$${"%1.2f".format(fuelSurcharge)}\nService Charge: $$${"%1.2f".format(serviceCharge)}\nTotal: $$${"%1.2f".format(total)}"
        )
      }
    )
    Quote(
      vehicleName = quoteProfile.name,
      vehicleDescription = quoteProfile.description,
      vehCode = quoteProfile.vehicleCode,
      maxPassengers = quoteProfile.maxPassengers,
      fares = fares
    )
  }
}
