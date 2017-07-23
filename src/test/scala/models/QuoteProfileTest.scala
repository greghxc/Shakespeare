package models

import com.typesafe.config.{ConfigFactory, ConfigList, ConfigValue, ConfigObject}
import org.scalatest.{FunSpec, Matchers}
import spray.json._

import scala.collection.JavaConverters._

class QuoteProfileTest extends FunSpec with Matchers {
  val thing = ConfigFactory.load("fares").getConfigList("quoteProfiles")
  val vehicles: ConfigList = ConfigFactory.load("fares").getList("quoteProfiles")
  val myList: List[ConfigValue] = ConfigFactory.load("fares").getList("quoteProfiles").asScala.toList
  val any = test.AnySingleton

  describe("testConstructor") {
    val quote: QuoteProfile = QuoteProfile(
      name = "Vehicle Name",
      description = "Vehicle Description",
      vehicleCode = "VEHCODE",
      maxPassengers = 4,
      fareProfiles = List(any.fareProfile)
    )

    val json = quote.toJson.toString

    it("serializes correctly") {
      val qp = json.parseJson.convertTo[QuoteProfile]
      qp shouldEqual quote
    }
  }
  describe("fromConfig") {
    it("works") {
      val vehicles = FareConfig.vehicles
      System.out.println(vehicles)
      true shouldEqual true
    }
  }
}