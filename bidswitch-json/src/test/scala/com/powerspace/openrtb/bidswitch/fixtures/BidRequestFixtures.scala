package com.powerspace.openrtb.bidswitch.fixtures

import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.BidRequest.Imp.Pmp.Deal
import com.google.openrtb.BidRequest.Imp.{Audio, Metric, Native, Pmp, Video}
import com.google.openrtb.BidRequest.{DistributionchannelOneof, Imp, Regs, Source}
import com.google.openrtb._
import com.powerspace.bidswitch.BidRequestExt.AdsTxt
import com.powerspace.bidswitch.ImpressionExt.Google
import com.powerspace.bidswitch.NativeExt.TripleLift
import com.powerspace.bidswitch._

/**
  * BidSwitch BidRequest entities needed for testing purposes
  */
object BidRequestFixtures {

  import com.powerspace.openrtb.json.fixtures.BidRequestFixtures._

  def sampleBidRequest(withNativeObject: Boolean): BidRequest = {

    // extended user
    val user = getUser
    val userExtension = UserExt(ug = 30, cookieAge = Some(3), googleConsent = Seq(43))
    val extendedUser = user.withExtension(BidswitchProto.userExt)(Some(userExtension))

    // extended banner
    val banner = getBanner
    val bannerExtension = BannerExt(Seq(com.powerspace.bidswitch.Format(h = 10, w = 20)))
    val extendedBanner = banner.withExtension(BidswitchProto.bannerExt)(Some(bannerExtension))

    val requestOneof = if (withNativeObject) RequestOneof.RequestNative(NativeRequest(plcmtcnt = Some(40))) else RequestOneof.Request("native-string")

    // extended native
    val native = Native(ver = Some("ver-1"), api = Seq(APIFramework.MRAID_1), battr = Seq(CreativeAttribute.FLASH), requestOneof = requestOneof)
    val nativeExtension = NativeExt(triplelift = Some(TripleLift(formats = Seq(10))))
    val extendedNative = native.withExtension(BidswitchProto.requestNativeExt)(Some(nativeExtension))

    // extended video
    val video = Video(mimes = Seq("mimes-1"), api = Seq(APIFramework.MRAID_1), pos = Some(AdPosition.HEADER), skip = Some(true), companionad = Seq(extendedBanner))
    val videoExtension = VideoExt(skippable = 1, playerType = 3, vastUrlRq = Some(4))
    val extendedVideo = video.withExtension(BidswitchProto.videoExt)(Some(videoExtension))

    // extended deal
    val deal = Deal(id = "deal-1")
    val dealExtension = DealExt(dataSrc = Some("datasrc-1"))
    val extendedDeal = deal.withExtension(BidswitchProto.dealExt)(Some(dealExtension))

    val impression = Imp(
      id = "imp-1",
      banner = Some(extendedBanner),
      video = Some(extendedVideo),
      audio = Some(Audio(minduration = Some(10), maxseq = Some(10))),
      displaymanager = Some("displaymanager-1"),
      displaymanagerver = Some("displaymanagerver-1"),
      instl = Some(true),
      tagid = Some("tagid-1"),
      bidfloor = Some(10d),
      bidfloorcur = Some("bidflooorcur-1"),
      clickbrowser = Some(true),
      secure = Some(true),
      iframebuster = Seq("iframebuster-1"),
      pmp = Some(Pmp(privateAuction = Some(true), deals = Seq(extendedDeal))),
      native = Some(extendedNative),
      exp = Some(10),
      metric = Seq(Metric(Some("type-1"), Some(10d), Some("vendor-1")))
    )

    // extended impression
    val impressionExtension = ImpressionExt(google = Google(excludedAttribute = Seq(1), allowedVendorType = Seq(2)), inventoryClass = Some(1))
    val extendedImpression = impression.withExtension(BidswitchProto.impressionExt)(Some(impressionExtension))

    val device = getDevice
    val bidRequest = BidRequest(
      id = "fmySKZNcTFcTPOurFYivufGxMtuSYpen",
      at = Some(AuctionType.fromValue(2)),
      imp = Seq(extendedImpression),
      cur = Seq("EUR"),
      device = Some(device),
      regs = Some(Regs(coppa = Some(true))),
      user = Some(extendedUser),
      tmax = Some(10),
      wseat = Seq("wseat-1", "wseat-2"),
      allimps = Some(true),
      bcat = Seq("bcat-1", "bcat-2"),
      badv = Seq("badv-1", "badv-2"),
      bapp = Seq("bapp-1", "bapp-2"),
      test = Some(true),
      bseat = Seq("bseat-1", "bseat-2"),
      wlang = Seq("wlang-1", "wlang-2"),
      source = Some(Source(fd = Some(true), tid = None, pchain = Some("pchain-1"))),
      distributionchannelOneof = DistributionchannelOneof.Empty
    )

    // extended bid request
    val bidRequestExtension: BidRequestExt = BidRequestExt(ssp = "powerspace", mediaSrc = "powerspace", adsTxt = AdsTxt(status = 1, pubId = "pub-id-1"))
    bidRequest.withExtension(BidswitchProto.bidRequestExt)(Some(bidRequestExtension))
  }

}
