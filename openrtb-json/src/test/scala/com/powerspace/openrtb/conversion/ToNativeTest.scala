package com.powerspace.openrtb.conversion

import com.powerspace.openrtb.json.{BidRequestFixtures, NativeManipulation}

object ToNativeTest extends App {

  private val request = NativeManipulation
    .toNativeAsString(BidRequestFixtures.getBidRequest(withNativeObject = true))

  assert(RequestLenses.getAllNatives(request).forall(_.isRequest))
  println(NativeManipulation.toNativeStringEncoder(request).noSpaces)

}
