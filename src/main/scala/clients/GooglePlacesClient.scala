package clients

import com.google.maps.{DistanceMatrixApi, GeoApiContext, GeocodingApi}
import com.google.maps.model.{AddressComponent, AddressComponentType, Distance, DistanceMatrix, DistanceMatrixElement, Unit}
import exceptions.InvalidZipException
import models.{AddressResult, DistanceMatrixResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Properties

object GooglePlacesClientSingleton extends GooglePlacesClient {
  val apiKey: String = Properties.envOrElse("GOOGLE_API_KEY", "")

  override val context: GeoApiContext = new GeoApiContext.Builder()
    .apiKey(apiKey)
    .build()
}

trait GooglePlacesClient {
  val context: GeoApiContext

  def getDistanceMatrix(origin: String, destination: String): Future[DistanceMatrixResult] = {
    Future {
      val matrix = DistanceMatrixApi.newRequest(context)
        .destinations(destination, origin)
        .origins(origin, destination)
        .units(Unit.IMPERIAL)
        .await
      DistanceMatrixResult(
        matrix.rows(0).elements(0).distance,
        matrix.rows(1).elements(1).distance,
        matrix.rows(0).elements(0).duration
      )
    }
  }

  def getAddress(address: String): Future[AddressResult] = {
    Future {
      val resolvedAddress = GeocodingApi.geocode(context, address).await()(0)
      val postalCode = getPostalCodeFromComponents(resolvedAddress.addressComponents)
      AddressResult(
        originalQuery = address,
        formattedAddress = resolvedAddress.formattedAddress,
        postalCode = postalCode
      )
    }
  }

  def getAddresses(origin: String, destination: String): Future[Seq[AddressResult]] = {
    val eventualOriginAddress = getAddress(origin)
    val eventualDestinationAddress = getAddress(destination)
    for {
      origin <- eventualOriginAddress
      destination <- eventualDestinationAddress
    } yield { Seq(origin, destination) }
  }

  def getPostalCodeFromComponents(components: Array[AddressComponent]): String = {
    val zips = for {
      component <- components.filter(_.types.contains(AddressComponentType.POSTAL_CODE))
      zip = component.longName
    } yield zip
    zips match {
      case Array(zip: String) => zip
      case _ => throw new InvalidZipException("Unable to determine zip")
    }
  }
}
