package com.powerspace.openrtb.conversion

import com.powerspace.openrtb.json.NativeManipulation
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.fixtures.BidRequestFixtures
import org.scalatest.{FunSuite, GivenWhenThen}

class ToNativeTest extends FunSuite with GivenWhenThen {

  test("Native request string/object converted") {

    Given("An ExtensionRegistry and a NativeManipulation object")
    implicit val extensionRegistry: ExtensionRegistry = ExtensionRegistry()
    val native = new NativeManipulation()

    When("I convert the native request to a real object")
    val request = native.toNativeAsString(BidRequestFixtures.getBidRequest(withNativeObject = true))

    Then("I check that the conversion successfully happened")
    assert(RequestLenses.getAllNatives(request).forall(_.isRequest))
    println(native.toNativeStringEncoder(request).noSpaces)

  }

}
