package com.powerspace.openrtb.bidswitch

import com.google.openrtb.BidRequest.Imp.Banner.Format
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.BidRequest.Imp.Pmp.Deal
import com.google.openrtb.BidRequest.Imp.{Audio, Banner, Metric, Native, Pmp, Video}
import com.google.openrtb.BidRequest.{Data, Device, DistributionchannelOneof, Geo, Imp, Regs, Source, User}
import com.google.openrtb._
import com.powerspace.bidswitch.BidRequestExt.AdsTxt
import com.powerspace.bidswitch.ImpressionExt.Google
import com.powerspace.bidswitch.NativeExt.TripleLift
import com.powerspace.bidswitch._

object BidRequestFixtures {

  def sampleBidRequest(withNativeObject: Boolean = false): BidRequest = {

    val geo = Some(Geo(
      lat = Some(100.10d),
      lon = Some(200.20d),
      country = Some("Italy"),
      `type` = Some(LocationType.IP),
      ipservice = Some(LocationService.MAXMIND)
    ))

    val device = Some(Device(
      dnt = Some(true),
      ua = Some("ua-1"),
      ip = Some("ip-1"),
      geo = geo,
      didsha1 = Some("didsha1-1")
    ))

    val user = User(
      id = Some("id-1"),
      buyeruid = Some("buyerid-1"),
      yob = Some(10),
      gender = Some("m"),
      keywords = Some("keywords-1"),
      customdata = Some("customdata-1"),
      geo = geo,
      data = Seq(Data(id = Some("data-1"), name = Some("name-1"), segment = Seq()))
    )

    // extended user
    val userExtension = UserExt(ug = 30, cookieAge = Some(3), googleConsent = Seq(43))
    val extendedUser = user.withExtension(BidswitchProto.userExt)(Some(userExtension))

    // extended banner
    val banner = Banner(w = Some(10), h = Some(10), api = Seq(APIFramework.MRAID_1), format = Seq(Format(w = Some(10))))
    val bannerExtension = BannerExt(Seq(com.powerspace.bidswitch.Format(h = 10, w = 20)))
    val extendedBanner = banner.withExtension(BidswitchProto.bannerExt)(Some(bannerExtension))

    // extended native
    val requestOneof = if (withNativeObject) {
      val nativeRequest = NativeRequest(plcmtcnt = Some(40))
      RequestOneof.RequestNative(nativeRequest)
    }
    else {
      RequestOneof.Request("native-string")
    }
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

    val bidRequest = BidRequest(
      id = "fmySKZNcTFcTPOurFYivufGxMtuSYpen",
      at = Some(AuctionType.fromValue(value = 2)),
      imp = Seq(extendedImpression),
      cur = Seq("EUR"),
      device = device,
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
    val bidRequestExtension = BidRequestExt(ssp = "powerspace", mediaSrc = "powerspace", adsTxt = AdsTxt(status = 1, pubId = "pub-id-1"))
    bidRequest.withExtension(BidswitchProto.bidRequestExt)(Some(bidRequestExtension))
  }

}
