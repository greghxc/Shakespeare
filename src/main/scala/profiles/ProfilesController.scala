package profiles

import models.{FareConfig, FuelSurchargeProfile, QuoteProfile}

object ProfilesControllerSingleton extends ProfilesController

class ProfilesController {
  def getQuoteProfiles(): Seq[QuoteProfile] = {
    FareConfig.vehicles
  }
  def getSurchargeProfile(): FuelSurchargeProfile = {
    FareConfig.fuelSurchargeProfile
  }
}
