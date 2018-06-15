package com.powerspace.openrtb.bidswitch

import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.BidResponse.SeatBid.Bid.AdmOneof
import com.google.openrtb._
import com.powerspace.bidswitch.BidExt.{Google, Yieldone}
import com.powerspace.bidswitch.{BidExt, BidResponseExt, BidswitchProto, NativeResponseExt}

/**
  * BidSwitch BidResponse entities needed for testing purposes
  */
object BidResponseFixtures {

  import com.powerspace.openrtb.json.BidResponseFixtures._

  def sampleBidResponse(withNativeObject: Boolean): BidResponse = {

    // extended native response
    val nativeExtension = NativeResponseExt(
      viewtracker = Some("wt-1"),
      adchoiceurl = Some("adch-1"))
    val extendedNativeResponse =
      getNativeResponse.withExtension(BidswitchProto.responseNativeExt)(Some(nativeExtension))

    // extended bid
    val google = Google(attribute = Seq(10), vendorType = Seq(4))
    val yieldone = Yieldone(creativeType = Some("ctype-1"), creativeCategoryId = Some(10))
    val bidExtension = BidExt(
      asid = Some("asid-1"),
      country = Some("Italy"),
      advertiserName = Some("coca-cola"),
      google = Some(google),
      yieldone = Some(yieldone),
      native = Some(extendedNativeResponse))
    val admOneof = if (withNativeObject) AdmOneof.AdmNative(extendedNativeResponse) else AdmOneof.Adm("native-string")
    val extendedBid = getBid(admOneof).withExtension(BidswitchProto.bidExt)(Some(bidExtension))

    // extended bid response
    val bidResponseExtension = BidResponseExt(protocol = Some("protocol-1"))
    val bidResponse = BidResponse(
      id = "brid-1",
      seatbid = Seq(SeatBid(
        bid = Seq(extendedBid),
        seat = Some("powerspace"),
        group = Some(true))
      )
    )
    bidResponse.withExtension(BidswitchProto.bidResponseExt)(Some(bidResponseExtension))

  }

}
