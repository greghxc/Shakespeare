package quotes

import models.{Fare, FareProfile, FuelSurchargeProfile, Quote}
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.mockito.MockitoSugar
import test._

class QuoteBuilderTest extends FunSpec with Matchers with MockitoSugar {
  val builder = new QuoteBuilder()
  val any = test.AnySingleton

  describe("#buildQuote") {
    it("builds a quote") {
      val fuelSurchargeProfile = FuelSurchargeProfile(0)

      val quoteProfile = any.quoteProfile.copy(
        fareProfiles = List(
          new FareProfile(
            name = "fare-test",
            baseFare = 21.50,
            perMile = 5.5,
            minimum = 10
          )
        )
      )

      val distanceResult = any.distanceResult.copy(
        miles = 30
      )

      val expectedFare = Fare(
        fareProfile = "fare-test",
        totalFare = 223.80,
        fareDetail = "Base Fare: $186.50\nFuel Surcharge: $0.00\nService Charge: $37.30\nTotal: $223.80"
      )

      val expectedQuote = Quote(
        vehicleName = quoteProfile.name,
        vehicleDescription = quoteProfile.description,
        vehCode = quoteProfile.vehicleCode,
        maxPassengers = quoteProfile.maxPassengers,
        List(expectedFare)
      )

      builder.buildQuote(quoteProfile, distanceResult, fuelSurchargeProfile) shouldEqual(expectedQuote)
    }

    it("respects fuel surcharges") {
      val fuelSurchargeProfile = FuelSurchargeProfile(0.01)

      val quoteProfile = any.quoteProfile.copy(
        fareProfiles = List(
          new FareProfile(
            name = "fare-test",
            baseFare = 20,
            perMile = 10,
            minimum = 10
          )
        )
      )

      val distanceResult = any.distanceResult.copy(
        miles = 30
      )

      val expectedFare = Fare(
        fareProfile = "fare-test",
        totalFare = 387.2,
        fareDetail = "Base Fare: $320.00\nFuel Surcharge: $3.20\nService Charge: $64.00\nTotal: $387.20"
      )

      val expectedQuote = Quote(
        vehicleName = quoteProfile.name,
        vehicleDescription = quoteProfile.description,
        vehCode = quoteProfile.vehicleCode,
        maxPassengers = quoteProfile.maxPassengers,
        List(expectedFare)
      )

      builder.buildQuote(quoteProfile, distanceResult, fuelSurchargeProfile) shouldEqual(expectedQuote)
    }

    it("respects minimum") {
      val fuelSurchargeProfile = FuelSurchargeProfile(0)

      val quoteProfile = any.quoteProfile.copy(
        fareProfiles = List(
          new FareProfile(
            name = "fare-test",
            baseFare = 20,
            perMile = 10,
            minimum = 400
          )
        )
      )

      val distanceResult = any.distanceResult.copy(
        miles = 30
      )

      val expectedFare = Fare(
        fareProfile = "fare-test",
        totalFare = 480,
        fareDetail = "Base Fare: $400.00\nFuel Surcharge: $0.00\nService Charge: $80.00\nTotal: $480.00"
      )

      val expectedQuote = Quote(
        vehicleName = quoteProfile.name,
        vehicleDescription = quoteProfile.description,
        vehCode = quoteProfile.vehicleCode,
        maxPassengers = quoteProfile.maxPassengers,
        List(expectedFare)
      )

      builder.buildQuote(quoteProfile, distanceResult, fuelSurchargeProfile) shouldEqual(expectedQuote)
    }
  }
}
