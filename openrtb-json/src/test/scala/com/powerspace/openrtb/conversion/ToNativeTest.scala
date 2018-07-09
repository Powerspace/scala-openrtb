package com.powerspace.openrtb.conversion

import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.{BidRequestFixtures, NativeManipulation}

object ToNativeTest extends App {

  implicit val extensionRegistry: ExtensionRegistry = ExtensionRegistry()

  private val native = new NativeManipulation()
  private val request = native.toNativeAsString(BidRequestFixtures.getBidRequest(withNativeObject = true))

  assert(RequestLenses.getAllNatives(request).forall(_.isRequest))

  println(native.toNativeStringEncoder(request).noSpaces)

}
