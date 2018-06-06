package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest.Imp.Banner.Format
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.BidRequest.Imp.Pmp.Deal
import com.google.openrtb.BidRequest.Imp.{Audio, Banner, Metric, Native, Pmp, Video}
import com.google.openrtb.BidRequest.{Data, Device, DistributionchannelOneof, Geo, Imp, Regs, Source, User}
import com.google.openrtb._

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

    val user = Some(User(
      id = Some("id-1"),
      buyeruid = Some("buyerid-1"),
      yob = Some(10),
      gender = Some("m"),
      keywords = Some("keywords-1"),
      customdata = Some("customdata-1"),
      geo = geo,
      data = Seq(Data(id = Some("data-1"), name = Some("name-1"), segment = Seq()))
    ))

    val banner = Banner(w = Some(10), h = Some(10), api = Seq(APIFramework.MRAID_1), format = Seq(Format(w = Some(10))))
    val requestOneof = if (withNativeObject) {
      val nativeRequest = NativeRequest(plcmtcnt = Some(40))
      RequestOneof.RequestNative(nativeRequest)
    }
    else {
      RequestOneof.Request("native-string")
    }

    val imp = Seq(Imp(
      id = "imp-1",
      banner = Some(banner),
      video = Some(Video(mimes = Seq("mimes-1"), api = Seq(APIFramework.MRAID_1), pos = Some(AdPosition.HEADER), skip = Some(true), companionad = Seq(banner))),
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
      pmp = Some(Pmp(privateAuction = Some(true), deals = Seq(Deal(id = "deal-1")))),
      native = Some(Native(ver = Some("ver-1"), api = Seq(APIFramework.MRAID_1), battr = Seq(CreativeAttribute.FLASH), requestOneof = requestOneof)),
      exp = Some(10),
      metric = Seq(Metric(Some("type-1"), Some(10d), Some("vendor-1")))
    ))

    BidRequest(
      id = "fmySKZNcTFcTPOurFYivufGxMtuSYpen",
      at = Some(AuctionType.fromValue(value = 2)),
      imp = imp,
      cur = Seq("EUR"),
      device = device,
      regs = Some(Regs(coppa = Some(true))),
      user = user,
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
  }

}
