package models

import com.typesafe.config.{Config, ConfigFactory}
import scala.collection.JavaConversions._

object FareConfig {
  private val fareConfig: Config = ConfigFactory.load("fares")

  val vehicles: List[QuoteProfile] = {
    def quoteProfileFromConfig(conf: Config): QuoteProfile = {
      val fareProfiles: List[FareProfile] = conf.getConfigList("fareProfiles").toList.map(conf =>
        new FareProfile(
          name = conf.getString("name"),
          baseFare = conf.getDouble("baseFare"),
          perMile = conf.getDouble("perMile"),
          minimum = conf.getDouble("minimum")
        )
      )
      QuoteProfile(
        name = conf.getString("name"),
        description = conf.getString("description"),
        vehicleCode = conf.getString("vehicleCode"),
        maxPassengers = conf.getInt("maxPassengers"),
        fareProfiles = fareProfiles
      )
    }
    fareConfig.getConfigList("quoteProfiles").toList.map(vehicle =>
      quoteProfileFromConfig(vehicle)
    )
  }

  val fuelSurchargeProfile = new FuelSurchargeProfile(fareConfig.getDouble("fuelSurcharge"))
}
