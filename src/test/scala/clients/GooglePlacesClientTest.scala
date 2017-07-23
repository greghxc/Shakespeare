package clients

import com.google.maps.model.{AddressComponent, AddressComponentType}
import exceptions.InvalidZipException
import org.scalatest.{FunSpec, Matchers}

class ComponentHelper {
  val zipType = AddressComponentType.POSTAL_CODE
  val stateType = AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1

  val componentWithZip = new AddressComponent
  componentWithZip.longName = "98122"
  componentWithZip.shortName = "98122"
  componentWithZip.types = Array(zipType)


  val componentWithDifferentZip = new AddressComponent
  componentWithDifferentZip.longName = "98123"
  componentWithDifferentZip.shortName = "98123"
  componentWithDifferentZip.types = Array(zipType)

  val componentNoZip = new AddressComponent
  componentNoZip.longName = "Washington"
  componentNoZip.shortName = "WA"
  componentNoZip.types = Array(stateType)

  val componentsWithZip = Array(
    componentWithZip,
    componentNoZip
  )

  val componentsWithNoZip = Array(
    componentNoZip
  )

  val componentsWithMultiZip = Array(
    componentWithZip,
    componentWithDifferentZip,
    componentNoZip
  )
}

class GooglePlacesClientTest extends FunSpec with Matchers {
  val componentHelper = new ComponentHelper
  val client = GooglePlacesClientSingleton

  describe("testGetPostalCodeFromComponents") {
    describe("when given an array of components with a postal_code") {
      it("should work") {
        client.getPostalCodeFromComponents(componentHelper.componentsWithZip) shouldBe "98122"
      }
    }
    describe("when given an array of components with no postal_code") {
      it("throws expected exception") {
        assertThrows[InvalidZipException] {
          client.getPostalCodeFromComponents(componentHelper.componentsWithNoZip)
        }
      }
    }
    describe("when given an array of components with multiple postal_code") {
      it("throws expected exception") {
        assertThrows[InvalidZipException] {
          client.getPostalCodeFromComponents(componentHelper.componentsWithNoZip)
        }
      }
    }
  }
}

