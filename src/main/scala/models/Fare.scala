package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object Fare extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val fareFormat = jsonFormat4(Fare.apply)
}

case class Fare (
  fareProfile: String,
  totalFare: BigDecimal,
  fareDetail: String,
  fareLineItems: Seq[FareLineItem] = Seq()
)

