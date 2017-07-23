package clients

import com.google.maps.{DistanceMatrixApi, GeoApiContext, GeocodingApi}
import com.google.maps.model.{AddressComponent, AddressComponentType, Distance, Unit}
import exceptions.InvalidZipException
import models.AddressResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Properties

object GooglePlacesClientSingleton extends GooglePlacesClient {
  val apiKey: String = Properties.envOrElse("googleApiKey", "")

  override val context: GeoApiContext = new GeoApiContext().setApiKey(apiKey)
}

trait GooglePlacesClient {
  val context: GeoApiContext

  def getDistance(origin: String, destination: String): Future[Distance] = {
    Future {
      DistanceMatrixApi.newRequest(context)
        .destinations(destination)
        .origins(origin)
        .units(Unit.IMPERIAL)
        .await
        .rows(0)
        .elements(0)
        .distance
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
