package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object QuoteProfile extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val quoteProfileFormat = jsonFormat5(QuoteProfile.apply)
}

case class QuoteProfile(
   name: String,
   description: String,
   vehicleCode: String,
   maxPassengers: Int,
   fareProfiles: Seq[FareProfile]
)