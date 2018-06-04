package com.powerspace.openrtb.json

import java.net.URL

import com.google.openrtb.BidRequest.Imp.Banner.Format
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.BidRequest.Imp.Pmp.Deal
import com.google.openrtb.BidRequest.Imp.{Audio, Banner, Metric, Native, Pmp, Video}
import com.google.openrtb.BidRequest.{Data, Device, DistributionchannelOneof, Geo, Imp, Regs, Source, User}
import com.google.openrtb._
import io.circe.Json
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.{FunSuite, GivenWhenThen}

class OpenRtbSerdeTest extends FunSuite with GivenWhenThen {

  import OpenRtbSerdeModule._

  test("OpenRTB-like (Elastic Ads) bid response deserialization") {
    Given("An OpenRTB-like bid response in JSON format")
    val stream: URL = getClass.getResource("/elasticads-bidresponse.json")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I deserialize it")
    val decoded = decode[BidResponse](json)

    Then("It should return a proper Scala BidResponse")
    val bidResponse = decoded.toTry.get
    assert(bidResponse.id.nonEmpty)
    assert(bidResponse.bidid.isEmpty)
    assert(bidResponse.seatbid.nonEmpty)
    assert(bidResponse.seatbid.head.bid.nonEmpty)
    val firstBid = bidResponse.seatbid.head.bid.head
    assert(firstBid.language.isEmpty)
    assert(firstBid.h.isEmpty)
    assert(firstBid.price > 0)

    assert(firstBid.admOneof.admNative.isDefined)
    assert(firstBid.admOneof.adm.isEmpty)

    assert(firstBid.getAdmNative.assets.nonEmpty)
    assert(firstBid.getAdmNative.assets.head.getTitle.text.contains("printemps"))
    assert(firstBid.getAdmNative.assets.last.id == 5)
    assert(firstBid.getAdmNative.assets.last.getData.value.contains("Sarenza"))
  }

  test("OpenRTB-like bid response serialization with no-bid") {
    Given("An OpenRTB-like bid response in JSON format with no bid")
    val stream: URL = getClass.getResource("/elasticads-bidresponse-no-bid.json")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I deserialize it")
    val decoded = decode[BidResponse](json)

    Then("It should return a proper Scala BidResponse with no bid in it")
    val bidResponse = decoded.toTry.get
    assert(bidResponse.id.nonEmpty)
    assert(bidResponse.bidid.isEmpty)
    assert(bidResponse.seatbid.nonEmpty)
    assert(bidResponse.seatbid.head.bid.isEmpty)
  }

  test("OpenRTB-like bid request serialization") {
    Given("An OpenRTB-like BidRequest")
    val bidRequest = getBidRequest

    When("I serialize it")
    val json = bidRequest.asJson

    Then("It should return a proper bid request in JSON format")
    println(json)
    val reqCursor = json.hcursor
    assert(reqCursor.downField("id").as[String].right.get == "fmySKZNcTFcTPOurFYivufGxMtuSYpen")
    assert(reqCursor.downField("at").as[Int].right.get == 2)
    assert(reqCursor.downField("cur").as[Seq[String]].right.get == Seq("EUR"))

    assert(reqCursor.downField("source").downField("pchain").as[String].right.get == "pchain-1")

    val impCursor = reqCursor.downField("imp").downArray
    assert(impCursor.downField("id").as[String].right.get == "imp-1")
    assert(impCursor.downField("displaymanager").as[String].right.get == "displaymanager-1")
    assert(impCursor.downField("bidfloor").as[Double].right.get == 10d)
    assert(impCursor.downField("instl").as[Int].right.get == 1)

    val videoCursor = impCursor.downField("video")
    assert(videoCursor.downField("mimes").as[Seq[String]].right.get == Seq("mimes-1"))
    assert(videoCursor.downField("api").as[Seq[Int]].right.get == Seq(3))
    assert(videoCursor.downField("skip").as[Int].right.get == 1)
    assert(videoCursor.downField("pos").as[Int].right.get == 4)
    assert(videoCursor.downField("companionad").downArray.downField("w").as[Int].right.get == 10)
    assert(videoCursor.downField("companionad").downArray.downField("api").as[Seq[Int]].right.get == Seq(3))

    val audioCursor = impCursor.downField("audio")
    assert(audioCursor.downField("minduration").as[Int].right.get == 10)

    val bannerCursor = impCursor.downField("banner")
    assert(bannerCursor.downField("w").as[Int].right.get == 10)
    assert(bannerCursor.downField("api").as[Seq[Int]].right.get == Seq(3))
    assert(bannerCursor.downField("format").downArray.downField("w").as[Int].right.get == 10)

    val metricCursor = impCursor.downField("metric")
    assert(metricCursor.downArray.downField("type").as[String].right.get == "type-1")

  }

  private def getBidRequest: BidRequest = {

    def geo = Some(Geo(
      lat = Some(100.10d),
      lon = Some(200.20d),
      country = Some("Italy"),
      `type` = Some(LocationType.IP),
      ipservice = Some(LocationService.MAXMIND)
    ))

    def device = Some(Device(
      dnt = Some(true),
      ua = Some("ua-1"),
      ip = Some("ip-1"),
      geo = geo,
      didsha1 = Some("didsha1-1")
    ))

    def user = Some(User(
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

    def imp = Seq(Imp(
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
      native = Some(Native(ver = Some("ver-1"), api = Seq(APIFramework.MRAID_1), battr = Seq(CreativeAttribute.FLASH), requestOneof = RequestOneof.Empty)),
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