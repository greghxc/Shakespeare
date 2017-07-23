package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object AddressResult extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val addressResultFormat = jsonFormat3(AddressResult.apply)
}

case class AddressResult (
  originalQuery: String,
  formattedAddress: String,
  postalCode: String
)
