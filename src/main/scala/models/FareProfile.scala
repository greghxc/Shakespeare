package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object FareProfile extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val fareProfileFormat = jsonFormat4(FareProfile.apply)
}

case class FareProfile (
  name: String,
  baseFare: BigDecimal,
  perMile: BigDecimal,
  minimum: BigDecimal
)
