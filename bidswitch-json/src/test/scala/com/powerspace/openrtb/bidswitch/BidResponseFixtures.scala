package com.powerspace.openrtb.bidswitch

import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.BidResponse.SeatBid.Bid
import com.google.openrtb.BidResponse.SeatBid.Bid.AdmOneof
import com.google.openrtb.NativeResponse.Asset.{AssetOneof, Image}
import com.google.openrtb.NativeResponse.{Asset, EventTracker, Link}
import com.google.openrtb._
import com.powerspace.bidswitch.{BidExt, BidResponseExt, BidswitchProto, NativeResponseExt}

object BidResponseFixtures {

  def sampleBidResponse(withNativeObject: Boolean): BidResponse = {
    // extended native objects
    val assetOneof = AssetOneof.Img(Image(`type` = Some(ImageAssetType.MAIN), url = "url-1"))
    val asset = Asset(id = 10, required = Some(true), link = Some(Link(url = "url-1")), assetOneof = assetOneof)

    val nativeExtension = NativeResponseExt(viewtracker = Some("wt-1"), adchoiceurl = Some("adch-1"))
    val nativeResponse = NativeResponse(ver = Some("ver-1"), link = Link(url = "url-1"), assetsurl = Some("asseturl-1"),
      dcourl = Some("dcourl-1"), imptrackers = Seq("imptracker-1"), jstracker = Some("jstracker-1"), privacy = Some("privacy-1"),
      eventtrackers = Seq(EventTracker(event = Some(EventType.IMPRESSION), method = EventTrackingMethod.IMG, url = Some("url-1"))),
      assets = Seq(asset))
    val extendedNativeResponse = nativeResponse.withExtension(BidswitchProto.responseNativeExt)(Some(nativeExtension))

    val admOneof = if (withNativeObject) AdmOneof.AdmNative(extendedNativeResponse) else AdmOneof.Adm("native-string")

    val google = com.powerspace.bidswitch.BidExt.Google(attribute = Seq(10), vendorType = Seq(4))
    val yieldone = com.powerspace.bidswitch.BidExt.Yieldone(creativeType = Some("ctype-1"), creativeCategoryId = Some(10))

    // extended bid
    val bid = Bid(id = "bidid-1", impid = "impid-1", price = 10d, adid = Some("adid-1"), nurl = Some("nurl-1"), adomain = Seq("adom-1"),
      bundle = Some("bundle-1"), iurl = Some("iurl-1"), cid = Some("cid-1"), crid = Some("crid-1"), cat = Seq("cat-1"),
      attr = Seq(CreativeAttribute.POP), api = Some(APIFramework.MRAID_1), tactic = Some("tactic-1"), protocol = Some(Protocol.VAST_1_0),
      qagmediarating = Some(QAGMediaRating.MATURE), dealid = Some("dealid-1"), w = Some(10), h = Some(10), exp = Some(10),
      burl = Some("burl-1"), lurl = Some("lurl-1"), wratio = Some(10), hratio = Some(10), language = Some("ENG"), admOneof = admOneof)
    val bidExtension = BidExt(asid = Some("asid-1"), country = Some("Italy"), advertiserName = Some("coca-cola"), google = Some(google),
      yieldone = Some(yieldone), native = Some(extendedNativeResponse))
    val extendedBid = bid.withExtension(BidswitchProto.bidExt)(Some(bidExtension))

    // extended bid response
    val bidResponseExtension = BidResponseExt(protocol = Some("protocol-1"))
    val bidResponse = BidResponse(id = "brid-1", seatbid = Seq(SeatBid(bid = Seq(extendedBid), seat = Some("powerspace"), group = Some(true))))
    bidResponse.withExtension(BidswitchProto.bidResponseExt)(Some(bidResponseExtension))
  }

}
