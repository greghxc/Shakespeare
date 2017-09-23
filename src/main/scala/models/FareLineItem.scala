package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object FareLineItem extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val fareLineItemFormat = jsonFormat3(FareLineItem.apply);
}

case class FareLineItem (
  lineItemType: String,
  lineItemDescription: String,
  lineItemCost: BigDecimal
)
