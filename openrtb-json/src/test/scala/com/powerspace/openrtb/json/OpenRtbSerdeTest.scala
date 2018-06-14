package com.powerspace.openrtb.json

import java.net.URL

import com.google.openrtb._
import com.powerspace.openrtb.json.BidRequestFixtures._
import com.powerspace.openrtb.json.BidResponseFixtures._
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.{FunSuite, GivenWhenThen}

class OpenRtbSerdeTest extends FunSuite with GivenWhenThen {

  import EncodingUtils._
  import OpenRtbSerdeModule._

  test("OpenRTB-like (Elastic Ads) bid response decoding") {
    Given("An OpenRTB-like bid response in JSON format")
    val stream: URL = getClass.getResource("/elasticads-bidresponse.json")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I decode it")
    val decoded = decode[BidResponse](json)

    Then("It should return a proper Scala BidResponse")
    val bidResponse = decoded.toTry.get
    assert(bidResponse.id.nonEmpty)
    assert(bidResponse.bidid.isEmpty)
    assert(bidResponse.seatbid.nonEmpty)
    assert(bidResponse.seatbid.head.bid.nonEmpty)
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

  test("OpenRTB-like bid response decoding with no-bid") {
    Given("An OpenRTB-like bid response in JSON format with no bid")
    val stream: URL = getClass.getResource("/elasticads-bidresponse-no-bid.json")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I decode it")
    val decoded = decode[BidResponse](json)

    Then("It should return a proper Scala BidResponse with no bid in it")
    val bidResponse = decoded.toTry.get
    assert(bidResponse.id.nonEmpty)
    assert(bidResponse.bidid.isEmpty)
    assert(bidResponse.seatbid.nonEmpty)
    assert(bidResponse.seatbid.head.bid.isEmpty)
  }

  test("OpenRTB-like bid request encoding") {

    Given("An OpenRTB-like BidRequest")
    val bidRequest = getBidRequest(withNativeObject = false)

    When("I encode it")
    val json = bidRequest.asJson

    Then("It should return a proper bid request in JSON format")
    val reqCursor = json.hcursor
    assert(reqCursor.downField("id").as[String].value == "fmySKZNcTFcTPOurFYivufGxMtuSYpen")
    assert(reqCursor.downField("at").as[Int].value == 2)
    assert(reqCursor.downField("cur").as[Seq[String]].value == Seq("EUR"))
    assert(reqCursor.downField("source").downField("pchain").as[String].value == "pchain-1")

    val impCursor = reqCursor.downField("imp").downArray
    assert(impCursor.downField("id").as[String].value == "imp-1")
    assert(impCursor.downField("displaymanager").as[String].value == "displaymanager-1")
    assert(impCursor.downField("bidfloor").as[Double].value == 10d)
    assert(impCursor.downField("instl").as[Int].value == 1)

    val videoCursor = impCursor.downField("video")
    assert(videoCursor.downField("mimes").as[Seq[String]].value == Seq("mimes-1"))
    assert(videoCursor.downField("api").as[Seq[Int]].value == Seq(3))
    assert(videoCursor.downField("skip").as[Int].value == 1)
    assert(videoCursor.downField("pos").as[Int].value == 4)
    assert(videoCursor.downField("companionad").downArray.downField("w").as[Int].value == 10)
    assert(videoCursor.downField("companionad").downArray.downField("api").as[Seq[Int]].value == Seq(3))

    val audioCursor = impCursor.downField("audio")
    assert(audioCursor.downField("minduration").as[Int].value == 10)

    val bannerCursor = impCursor.downField("banner")
    assert(bannerCursor.downField("w").as[Int].value == 10)
    assert(bannerCursor.downField("api").as[Seq[Int]].value == Seq(3))
    assert(bannerCursor.downField("format").downArray.downField("w").as[Int].value == 10)

    val metricCursor = impCursor.downField("metric")
    assert(metricCursor.downArray.downField("type").as[String].value == "type-1")

    val pmpCursor = impCursor.downField("pmp")
    assert(pmpCursor.downField("private_auction").as[Int].value == 1)
    assert(pmpCursor.downField("deals").downArray.downField("id").as[String].value == "deal-1")

    val nativeCursor = impCursor.downField("native")
    assert(nativeCursor.downField("ver").as[String].value == "ver-1")
    assert(nativeCursor.downField("battr").downArray.as[Int].value == 17)
    // @todo test native spec
    assert(nativeCursor.downField("request").as[String].contains("native-string"))


    val regsCursor = reqCursor.downField("regs")
    assert(regsCursor.downField("coppa").as[Int].value == 1)

    val deviceCursor = reqCursor.downField("device")
    assert(deviceCursor.downField("ip").as[String].value == "ip-1")
    assert(deviceCursor.downField("geo").downField("lon").as[Double].value == 200.20d)
    assert(deviceCursor.downField("geo").downField("ipservice").as[Int].value == 3)

    val userCursor = reqCursor.downField("user")
    assert(userCursor.downField("id").as[String].value == "id-1")
    assert(userCursor.downField("gender").as[String].value == "m")
    assert(userCursor.downField("data").downArray.downField("name").as[String].value == "name-1")

  }

  test("OpenRTB-like bid request [with Native Object] serialization") {
    Given("An OpenRTB-like BidRequest witha a Native Object")
    val bidRequest = getBidRequest(withNativeObject = true)

    When("I serialize it")
    val json = bidRequest.asJson

    Then("It should return a proper bid request in JSON format")
    val nativeObjectCursor = json.hcursor.downField("imp").downArray.downField("native").downField("request")
    assert(nativeObjectCursor.downField("plcmtcnt").as[Int].value == 40)
  }

  test("OpenRTB-like bid response [with Native Object] encoding") {
    Given("A BidSwitch BidResponse with a native object")
    val bidResponse = sampleBidResponse(withNativeObject = true)

    When("I encode it")
    val json = bidResponse.asJson
    println(json)
    Then("It should return a proper native bid response with related extensions in JSON format")

    val resCursor = json.hcursor
    assert(resCursor.downField("id").as[String].value.nonEmpty)
    assert(resCursor.downField("cur").as[String].value.nonEmpty)

    val seatBidCursor = resCursor.downField("seatbid").downArray
    assert(seatBidCursor.downField("seat").as[String].value.nonEmpty)

    val bidCursor = seatBidCursor.downField("bid").downArray
    assert(bidCursor.downField("price").as[Float].value == 10d)
    assert(bidCursor.downField("burl").as[String].toOption.nonEmpty)
    assert(bidCursor.downField("language").as[String].toOption.nonEmpty)

    val admCursor = bidCursor.downField("adm")
    assert(admCursor.downField("privacy").as[String].toOption.nonEmpty)
    assert(admCursor.downField("link").downField("url").as[String].toOption.nonEmpty)
    assert(admCursor.downField("link").downField("clicktrackers").downArray.as[String].toOption.nonEmpty)

    val assetCursor = admCursor.downField("assets").downArray
    assert(assetCursor.downField("id").as[Int].value == 10)
    assert(assetCursor.downField("required").as[Int].value == 1)
    assert(assetCursor.downField("link").downField("url").as[String].value.nonEmpty)

    val imgCursor = assetCursor.downField("img")
    //assert(imgCursor.downField("url").as[String].value == "url-img")
  }

  test("OpenRTB-like bid response encoding") {
    Given("A BidSwitch BidResponse")
    val bidResponse = sampleBidResponse(withNativeObject = true)

    When("I encode it")
    val json = bidResponse.asJson
    println(json)

    Then("It should return a proper native bid response with related extensions in JSON format")
    val resCursor = json.hcursor
    assert(resCursor.downField("id").as[String].value.nonEmpty)
    assert(resCursor.downField("cur").as[String].value.nonEmpty)

    val seatBidCursor = resCursor.downField("seatbid").downArray
    assert(seatBidCursor.downField("seat").as[String].value.nonEmpty)

    val bidCursor = seatBidCursor.downField("bid").downArray
    assert(bidCursor.downField("price").as[Float].value == 10d)
    assert(bidCursor.downField("burl").as[String].value.nonEmpty)
    assert(bidCursor.downField("language").as[String].value.nonEmpty)

    val admCursor = bidCursor.downField("adm")
    assert(admCursor.downField("privacy").as[String].value.nonEmpty)
    assert(admCursor.downField("link").downField("url").as[String].value.nonEmpty)
    assert(admCursor.downField("link").downField("clicktrackers").downArray.as[String].value.nonEmpty)

    val assetCursor = admCursor.downField("assets").downArray
    assert(assetCursor.downField("id").as[Int].value == 10)
    assert(assetCursor.downField("required").as[Int].value == 1)
    assert(assetCursor.downField("link").downField("url").as[String].value.nonEmpty)

    val imgCursor = assetCursor.downField("img")
    //assert(imgCursor.downField("url").as[String].value == "url-img")
  }

  test("OpenRTB-like Native bid request decoding") {
    Given("An OpenRTB-like native bid response in JSON format")
    val stream: URL = getClass.getResource("/openrtb-like-bidrequest-native.json")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I decode it")
    val decoded = decode[BidRequest](json)

    Then("It should return a proper Scala BidRequest")
    val bidRequest = decoded.toTry.get

    // Bid Request
    assert(bidRequest.id.nonEmpty)
    assert(bidRequest.tmax.nonEmpty)

    // Distribution Channel
    val distribution = bidRequest.distributionchannelOneof
    assert(distribution.isSite)
    assert(distribution.site.get.id.nonEmpty)
    assert(distribution.site.get.domain.nonEmpty)
    assert(distribution.site.get.publisher.nonEmpty)
    assert(distribution.site.get.publisher.get.id.nonEmpty)

    // Source
    val source = bidRequest.source.get
    assert(source.pchain.nonEmpty)
    assert(source.fd.nonEmpty)

    // Regs
    val regs = bidRequest.regs.get
    assert(regs.coppa.nonEmpty)

    // User
    val user = bidRequest.user.get
    assert(user.id.nonEmpty)
    assert(user.keywords.nonEmpty)

    // User Data
    val data = user.data.head
    assert(data.id.nonEmpty)
    assert(data.name.nonEmpty)

    // Data Segment
    assert(data.segment.head.id.nonEmpty)
    assert(data.segment.head.name.nonEmpty)
    assert(data.segment.head.value.nonEmpty)

    // User Geo
    assert(user.geo.get.ipservice.nonEmpty)
    assert(user.geo.get.zip.nonEmpty)
    assert(user.geo.get.`type`.nonEmpty)

    // Impressions
     val impression = bidRequest.imp.head
    assert(impression.id.nonEmpty)
    assert(impression.clickbrowser.nonEmpty)
    assert(impression.tagid.nonEmpty)

    // Native
    val native = impression.native.get
    assert(native.ver.nonEmpty)
    assert(native.requestOneof.isRequestNative)

    // Native Request
    val nativeRequest = native.requestOneof.requestNative.get
    assert(nativeRequest.layout.nonEmpty)
    assert(nativeRequest.ver.nonEmpty)
    assert(nativeRequest.context.nonEmpty)

    // Assets
    val asset = nativeRequest.assets.head
    assert(asset.id == 1)
    assert(asset.required.get)

    // Assets Title
    assert(asset.getTitle.len == 28)

    // Assets Img
    val assetTitle = nativeRequest.assets(1)
    assert(assetTitle.getImg.w.get == 120)

    // Assets Data
    val assetData = nativeRequest.assets(3)
    assert(assetData.getData.len.get == 134)

    // Video
    val video = impression.video.get
    assert(video.protocols.nonEmpty)
    assert(video.companionad.head.hmax.nonEmpty)
    assert(video.maxduration.nonEmpty)
    assert(video.mimes.nonEmpty)

    // Audio
    val audio = impression.audio.get
    assert(audio.startdelay.nonEmpty)
    assert(audio.mimes.nonEmpty)

    // Banner
    val banner = impression.banner.get
    assert(banner.h.nonEmpty)
    assert(banner.pos.nonEmpty)

    // Metrics
    assert(impression.metric.nonEmpty)
    val metric = impression.metric.head
    assert(metric.value.nonEmpty)
    assert(metric.`type`.nonEmpty)

    // Pmp & Deal
    val pmp = impression.pmp.get
    assert(pmp.privateAuction.nonEmpty)
    assert(pmp.deals.head.id.nonEmpty)
    assert(pmp.deals.head.bidfloor.nonEmpty)
    assert(pmp.deals.head.wseat.nonEmpty)

  }

//  test("Cross encoding") {
//    Given("")
//    val stream: URL = getClass.getResource("/elasticads-bidresponse.json")
//    val json: String = scala.io.Source.fromFile(stream.toURI).mkString
//
//    When("")
//    val decoded = decode[BidResponse](json).right.get
//    val encoded = decoded.asJson.toString()
//
//    Then("")
//    //assert(json == encoded)
//    println(json)
//    println("_______")
//    println(encoded)
//    assert(true)
//  }


}
