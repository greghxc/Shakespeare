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

        val fareLineItems: Seq[FareLineItem] =
          Seq(
            FareLineItem("fare", "Base Fare", fare),
            FareLineItem("fuelCharge", "Fuel Surcharge", fuelSurcharge),
            FareLineItem("serviceCharge", "Service Charge", serviceCharge),
            FareLineItem("total", "Total", total)
          )

        new Fare(
          fareProfile = x.name,
          totalFare = total,
          fareDetail = fareDetailFrom(fareLineItems),
          fareLineItems = fareLineItems
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

  def fareDetailFrom(fareLineItems: Seq[FareLineItem]): String = {
    val lines = for {
      f <- fareLineItems
      line = s"${f.lineItemDescription}: $$${"%1.2f".format(f.lineItemCost)}"
    } yield line
    lines.foldLeft("") { (a, l) =>
      if (a == "") {
        a + l;
      } else {
        a + "\n" + l
      }
    }
  }
}
