package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object QuoteResult extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val quoteResultFormat = jsonFormat2(QuoteResult.apply)
}

case class QuoteResult (
  tripDetails: DistanceResult,
  quotes: Seq[Quote]
)
