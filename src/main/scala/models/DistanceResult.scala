package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object DistanceResult extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val distanceResultFormat = jsonFormat5(DistanceResult.apply)
}

case class DistanceResult (
  meters: Long,
  miles: BigDecimal,
  minutes: Int,
  origin: Option[AddressResult] = None,
  destination: Option[AddressResult] = None
)
