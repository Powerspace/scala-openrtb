package com.powerspace.openrtb.json.fixtures

import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.BidResponse.SeatBid.Bid
import com.google.openrtb.BidResponse.SeatBid.Bid.AdmOneof
import com.google.openrtb.NativeResponse.Asset.{AssetOneof, Image}
import com.google.openrtb.NativeResponse.{Asset, EventTracker, Link}
import com.google.openrtb._

/**
  * BidResponse entities needed for testing purposes
  */
object BidResponseFixtures {

  def getAsset: Asset = {
    val assetOneof = AssetOneof.Img(Image(`type` = Some(ImageAssetType.MAIN), url = "url-img"))
    Asset(
      id = 10,
      required = Some(true),
      link = Some(Link(url = "url-1")),
      assetOneof = assetOneof)
  }

  def getNativeResponse: NativeResponse = {
    val link = Link(url = "url-1", clicktrackers = Seq("clicktrackers-1"))
    NativeResponse(
      ver = Some("ver-1"),
      link = link,
      assetsurl = Some("asseturl-1"),
      dcourl = Some("dcourl-1"),
      imptrackers = Seq("imptracker-1"),
      jstracker = Some("jstracker-1"),
      privacy = Some("privacy-1"),
      eventtrackers = Seq(EventTracker(
        event = Some(EventType.IMPRESSION),
        method = EventTrackingMethod.IMG,
        url = Some("url-1"))
      ),
      assets = Seq(getAsset))
  }

  def getBid(admOneof: AdmOneof): Bid = Bid(
    id = "bidid-1",
    impid = "impid-1",
    price = 10d,
    adid = Some("adid-1"),
    nurl = Some("nurl-1"),
    adomain = Seq("adom-1"),
    bundle = Some("bundle-1"),
    iurl = Some("iurl-1"),
    cid = Some("cid-1"),
    crid = Some("crid-1"),
    cat = Seq("cat-1"),
    attr = Seq(CreativeAttribute.POP),
    api = Some(APIFramework.MRAID_1),
    tactic = Some("tactic-1"),
    protocol = Some(Protocol.VAST_1_0),
    qagmediarating = Some(QAGMediaRating.MATURE),
    dealid = Some("dealid-1"),
    w = Some(10),
    h = Some(10),
    exp = Some(10),
    burl = Some("burl-1"),
    lurl = Some("lurl-1"),
    wratio = Some(10),
    hratio = Some(10),
    language = Some("ENG"),
    admOneof = admOneof)

  def sampleBidResponse(withNativeObject: Boolean): BidResponse = {
    val admOneof = if (withNativeObject) AdmOneof.AdmNative(getNativeResponse) else AdmOneof.Adm("native-string")
    val bid = getBid(admOneof)
    BidResponse(
      id = "brid-1",
      cur = Some("cur-1"),
      seatbid = Seq(SeatBid(
        bid = Seq(bid),
        seat = Some("powerspace"),
        group = Some(true)))
    )
  }

}
