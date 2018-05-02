package test

import com.github.javafaker.Faker
import com.google.maps.model.{Distance, Duration}
import models._

object AnySingleton extends Any

class Any {
  val faker: Faker = new Faker()

  def addressResult = {
    val street = faker.address().streetAddress()
    val city = faker.address().city()
    val state = faker.address().state()
    val zip = faker.address().zipCode()
    val country = "USA"

    AddressResult(
      originalQuery = s"$street $city $state",
      formattedAddress = s"$street, $city, $state $zip, $country",
      locality = s"$city",
      postalCode = zip
    )
  }

  def distanceResult = {
    val meters = faker.number().numberBetween(1000L, 20000L)
    val milesPerMeter = 0.000621371
    val minutes = faker.number().numberBetween(8, 90)

    DistanceResult(
      meters = meters,
      miles = meters * milesPerMeter,
      minutes = minutes,
      origin = Some(addressResult),
      destination = Some(addressResult)
    )
  }

  def quoteProfile: QuoteProfile = {
    QuoteProfile(
      name = faker.beer().name(),
      description = faker.beer().style(),
      vehicleCode = faker.idNumber().valid(),
      maxPassengers = faker.number().numberBetween(1, 9),
      fareProfiles = List(fareProfile, fareProfile)
    )
  }

  def fareProfile: FareProfile = {
    val baseFare: BigDecimal = faker.number().numberBetween(50, 100) / 10
    val perMile: BigDecimal = faker.number().numberBetween(10, 70) / 10
    val minumum: BigDecimal = baseFare + (perMile * 15)

    FareProfile(
      name = faker.pokemon().name(),
      baseFare = baseFare,
      perMile = perMile,
      minimum = minumum
    )
  }

  def fuelSurchargeProfile: FuelSurchargeProfile = {
    FuelSurchargeProfile(
      percentage = faker.number().numberBetween(1, 10) / 100
    )
  }

  def quote: Quote = {
    val vehicle = quoteProfile
    Quote(
      vehicleName = vehicle.name,
      vehicleDescription = vehicle.description,
      vehCode = vehicle.vehicleCode,
      maxPassengers = vehicle.maxPassengers,
      fares = List(fare, fare)
    )
  }

  def fare: Fare = {
    Fare(
      fareProfile = faker.app().name(),
      totalFare = faker.number().numberBetween(50, 100) / 10,
      fareDetail = faker.lorem().sentence()
    )
  }

  def distance: Distance = {
    val distance = new Distance()
    distance.inMeters = faker.number().numberBetween(1000, 10000)
    distance.humanReadable = faker.lorem().word()
    distance
  }

  def duration: Duration = {
    val duration = new Duration()
    duration.inSeconds = faker.number().numberBetween(5*60, 90*60)
    duration.humanReadable = faker.lorem().word()
    duration
  }
}
