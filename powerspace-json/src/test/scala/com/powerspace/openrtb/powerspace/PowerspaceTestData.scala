package com.powerspace.openrtb.powerspace

import com.google.openrtb.BidRequest.DistributionchannelOneof.Site
import com.google.openrtb.BidRequest.Imp.Banner.Format
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.BidRequest.Imp.Pmp.Deal
import com.google.openrtb.BidRequest.Imp.{Audio, Banner, Metric, Native, Pmp, Video}
import com.google.openrtb.BidRequest.{Content, Data, Device, Geo, Imp, Regs, Source, User}
import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.BidResponse.SeatBid.Bid
import com.google.openrtb.BidResponse.SeatBid.Bid.AdmOneof
import com.google.openrtb.NativeResponse.Asset.{AssetOneof, Image}
import com.google.openrtb.NativeResponse.{Asset, EventTracker, Link}
import com.google.openrtb._
import com.powerspace.openrtb._

/**
  * Builds Powerspace BidRequest and BidResponse with any possible extension in it
  */
object PowerspaceTestData {

  private val geo = {
    val ext = GeoExt(
      geoNamesId = Some(1),
      admin1 = Some("adm1"),
      admin2 = Some("adm2")
    )
    Geo(
      lat = Some(100.10d),
      lon = Some(200.20d),
      country = Some("Italy"),
      `type` = Some(LocationType.IP),
      ipservice = Some(LocationService.MAXMIND)
    ).withExtension(PowerspaceProto.geo)(Some(ext))
  }

  private val device = {
    Device(
      dnt = Some(true),
      ua = Some("ua-1"),
      ip = Some("ip-1"),
      geo = Some(geo),
      didsha1 = Some("didsha1-1")
    )
  }

  private val user = {
    val userExt = UserExt(identified = Some(true), suspicious = Some(false))
    User(
      id = Some("id-1"),
      buyeruid = Some("buyerid-1"),
      yob = Some(10),
      gender = Some("m"),
      keywords = Some("keywords-1"),
      customdata = Some("customdata-1"),
      geo = Some(geo),
      data = Seq(Data(id = Some("data-1"), name = Some("name-1"), segment = Seq()))
    ).withExtension(PowerspaceProto.user)(Some(userExt))
  }

  private val banner = Banner(w = Some(10), h = Some(10), api = Seq(APIFramework.MRAID_1), format = Seq(Format(w = Some(10))))

  private val impression = {
    val native = RequestOneof.RequestNative(NativeRequest(plcmtcnt = Some(40)))
    Imp(
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
      native = Some(Native(ver = Some("ver-1"), api = Seq(APIFramework.MRAID_1), battr = Seq(CreativeAttribute.FLASH), requestOneof = native)),
      exp = Some(10),
      metric = Seq(Metric(Some("type-1"), Some(10d), Some("vendor-1")))
    )
  }

  val site: Site = {
    val siteExt = SiteExt(network = Some("network"), country = Some("ITA"))
    val contentExt = ContentExt(channel = Some("channel"))
    Site(
      value = BidRequest.Site(
        id = Some("siteId"),
        name = Some("name"),
        mobile = Some(false),
        content = Some(
          Content(id = Some("content"), title = Some("title"))
            .withExtension(PowerspaceProto.contentExt)(Some(contentExt))
        )
      ).withExtension(PowerspaceProto.siteExt)(Some(siteExt))
    )
  }

  /**
    * A BidRequest containing
    * - BidRequest extension
    * - Geo extension
    * - Site extension
    * - Site content extension
    */
  val bidRequest: BidRequest = {
    val ext = BidRequestExt(forcedAlgorithm = Some("ab"), forcedDeal = Some("deal"), bcampaign = Seq(2))
    BidRequest(
      id = "fmySKZNcTFcTPOurFYivufGxMtuSYpen",
      imp = Seq(impression),
      device = Some(device),
      regs = Some(Regs(coppa = Some(true))),
      user = Some(user),
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
      distributionchannelOneof = site
    ).withExtension(PowerspaceProto.bidRequest)(Some(ext))
  }

  val asset: Asset = {
    val assetOneof = AssetOneof.Img(Image(`type` = Some(ImageAssetType.MAIN), url = "url-img"))
    Asset(
      id = 10,
      required = Some(true),
      link = Some(Link(url = "url-1")),
      assetOneof = assetOneof)
  }

  val nativeResponse: NativeResponse = {
    NativeResponse(
      ver = Some("ver-1"),
      link = Link(url = "url-1", clicktrackers = Seq("clicktrackers-1")),
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
      assets = Seq(asset))
  }

  val bid: Bid = {
    val ext = BidExt(ctr = Some(0.5d), cpc = Some(0.9d), margin = Some(0.6d))
    Bid(
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
      admOneof = AdmOneof.AdmNative(nativeResponse)
    ).withExtension(PowerspaceProto.bid)(Some(ext))
  }

  /**
    * A BidResponse containing
    * - Bid extension
    */
  val bidResponse: BidResponse =
    BidResponse(
      id = "brid-1",
      cur = Some("cur-1"),
      seatbid = Seq(SeatBid(
        bid = Seq(bid),
        seat = Some("powerspace"),
        group = Some(true)))
    )

}
