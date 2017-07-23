package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol


object FuelSurchargeProfile extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val fuelSurchargeProfileFormat = jsonFormat1(FuelSurchargeProfile.apply)
}

case class FuelSurchargeProfile (
  percentage: BigDecimal
)
