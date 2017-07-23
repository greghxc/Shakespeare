package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object Quote extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val quoteFormat = jsonFormat5(Quote.apply)
}

case class Quote (
  vehicleName: String,
  vehicleDescription: String,
  vehCode: String,
  maxPassengers: Int,
  fares: Seq[Fare]
)