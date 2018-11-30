package com.powerspace.openrtb.json

import java.net.URL

import com.google.openrtb._
import com.powerspace.openrtb.json.fixtures.BidRequestFixtures._
import com.powerspace.openrtb.json.fixtures.BidResponseFixtures._
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.Json
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.{FunSuite, GivenWhenThen}

class OpenRtbSerdeTest extends FunSuite with GivenWhenThen {

  import EncodingUtils._

  import OpenRtbSerdeModule._

  test("OpenRTB-like bid response deserialization") {
    Given("An OpenRTB-like bid response in JSON format with a string-ed native response")
    val stream: URL = getClass.getResource("/elasticads-bidresponse.json")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I deserialize it")
    val decoded = decode[BidResponse](json)

    Then("I should get a proper Scala BidResponse")
    val bidResponse = decoded.toTry.get
    assert(bidResponse.id.nonEmpty)
    assert(bidResponse.bidid.nonEmpty)
    assert(bidResponse.seatbid.nonEmpty)
    assert(bidResponse.seatbid.head.bid.nonEmpty)
    assert(bidResponse.seatbid.head.bid.nonEmpty)
    val firstBid = bidResponse.seatbid.head.bid.head
    assert(firstBid.language.isEmpty)
    assert(firstBid.h.isEmpty)
    assert(firstBid.price > 0)

    assert(firstBid.admOneof.admNative.nonEmpty)
    assert(firstBid.admOneof.adm.isEmpty)

    // the library is capable of unescape and decode the native response in JSON string format
    assert(firstBid.getAdmNative.link.url.nonEmpty)
    assert(firstBid.getAdmNative.assets.nonEmpty)
    assert(firstBid.getAdmNative.assets.head.getTitle.text.contains("printemps"))
    assert(firstBid.getAdmNative.assets.last.id == 6)
    assert(firstBid.getAdmNative.assets.last.getImg.url.contains("cloudfront"))
  }

  test("OpenRTB-like BidResponse deserialization with no-bid") {
    Given("An OpenRTB-like BidResponse in JSON format with no bid")
    val stream: URL = getClass.getResource("/elasticads-bidresponse-no-bid.json")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I deserialize it")
    val decoded = decode[BidResponse](json)

    Then("I should get a proper Scala BidResponse with no bid in it")
    val bidResponse = decoded.toTry.get
    assert(bidResponse.id.nonEmpty)
    assert(bidResponse.bidid.isEmpty)
    assert(bidResponse.seatbid.nonEmpty)
    assert(bidResponse.seatbid.head.bid.isEmpty)
  }

  test("OpenRTB-like BidRequest with native object serialization") {

    Given("An OpenRTB-like BidRequest with native object")
    val bidRequest = getBidRequest(withNativeObject = true)

    When("I serialize it")
    val json = bidRequest.asJson

    Then("I should get a proper BidRequest in JSON format")
    val reqCursor = json.hcursor
    assert(reqCursor.downField("id").as[String].value == "fmySKZNcTFcTPOurFYivufGxMtuSYpen")
    assert(reqCursor.downField("at").as[Int].value == 2)
    assert(reqCursor.downField("cur").as[Seq[String]].value == Seq("EUR"))
    assert(reqCursor.downField("source").downField("pchain").as[String].value == "pchain-1")

    val siteCursor = reqCursor.downField("site")
    assert(siteCursor.downField("id").as[String].value == "id-site")
    assert(siteCursor.downField("name").as[String].value == "name-site")

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

    val assetCursor = nativeCursor.downField("request").downField("assets").downArray
    assert(assetCursor.downField("id").as[Int].value == 44)
    assert(assetCursor.downField("title").downField("len").as[Int].value == 44)

  }

  test("OpenRTB-like BidRequest with a native object encoding") {
    Given("An OpenRTB-like BidRequest with a a native object")
    val bidRequest = getBidRequest(withNativeObject = true)

    When("I serialize it")
    val json = bidRequest.asJson

    Then("I should get a proper BidRequest in JSON format")
    val nativeObjectCursor = json.hcursor.downField("imp").downArray.downField("native").downField("request")
    assert(nativeObjectCursor.downField("plcmtcnt").as[Int].value == 40)
  }

  test("OpenRTB-like BidResponse [with Native Object] encoding") {
    Given("A BidSwitch BidResponse with a native object")
    val bidResponse = sampleBidResponse(withNativeObject = true)

    When("I encode it")
    val json = bidResponse.asJson
    Then("It should return a proper native BidResponse with related extensions in JSON format")

    val resCursor = json.hcursor
    assert(resCursor.downField("id").as[String].value.nonEmpty)
    assert(resCursor.downField("cur").as[String].value.nonEmpty)

    val seatBidCursor = resCursor.downField("seatbid").downArray
    assert(seatBidCursor.downField("seat").as[String].value.nonEmpty)

    val bidCursor = seatBidCursor.downField("bid").downArray
    assert(bidCursor.downField("price").as[Float].value == 10d)
    assert(bidCursor.downField("burl").as[String].toOption.nonEmpty)
    assert(bidCursor.downField("language").as[String].toOption.nonEmpty)

    val admCursor = bidCursor.downField("adm").downField("native")
    assert(admCursor.downField("privacy").as[String].toOption.nonEmpty)
    assert(admCursor.downField("link").downField("url").as[String].toOption.nonEmpty)
    assert(admCursor.downField("link").downField("clicktrackers").downArray.as[String].toOption.nonEmpty)

    val assetCursor = admCursor.downField("assets").downArray
    assert(assetCursor.downField("id").as[Int].value == 10)
    assert(assetCursor.downField("required").as[Int].value == 1)
    assert(assetCursor.downField("link").downField("url").as[String].value.nonEmpty)

    val imgCursor = assetCursor.downField("img")
    assert(imgCursor.downField("url").as[String].value == "url-img")
  }

  test("OpenRTB-like BidResponse encoding") {
    Given("A BidSwitch BidResponse")
    val bidResponse = sampleBidResponse(withNativeObject = true)

    When("I encode it")
    val json = bidResponse.asJson

    Then("It should return a proper native BidResponse with related extensions in JSON format")
    val resCursor = json.hcursor
    assert(resCursor.downField("id").as[String].value.nonEmpty)
    assert(resCursor.downField("cur").as[String].value.nonEmpty)

    val seatBidCursor = resCursor.downField("seatbid").downArray
    assert(seatBidCursor.downField("seat").as[String].value.nonEmpty)

    val bidCursor = seatBidCursor.downField("bid").downArray
    assert(bidCursor.downField("price").as[Float].value == 10d)
    assert(bidCursor.downField("burl").as[String].value.nonEmpty)
    assert(bidCursor.downField("language").as[String].value.nonEmpty)

    val admCursor = bidCursor.downField("adm").downField("native")
    assert(admCursor.downField("privacy").as[String].value.nonEmpty)
    assert(admCursor.downField("link").downField("url").as[String].value.nonEmpty)
    assert(admCursor.downField("link").downField("clicktrackers").downArray.as[String].value.nonEmpty)

    val assetCursor = admCursor.downField("assets").downArray
    assert(assetCursor.downField("id").as[Int].value == 10)
    assert(assetCursor.downField("required").as[Int].value == 1)
    assert(assetCursor.downField("link").downField("url").as[String].value.nonEmpty)

    val imgCursor = assetCursor.downField("img")
    assert(imgCursor.downField("url").as[String].value == "url-img")
  }

  test("OpenRTB-like BidRequest with native object deserialization") {
    Given("An OpenRTB-like BidRequest with native object in JSON format")
    val stream: URL = getClass.getResource("/openrtb-like-bidrequest-native.json")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I deserialize it")
    val decoded = decode[BidRequest](json)

    Then("I should get a proper Scala BidRequest")
    val bidRequest = decoded.toTry.get

    // BidRequest
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

  test("Deserializing and re-serializing a JSON BidResponse with native string I obtain the same JSON") {
    import gnieh.diffson.circe._

    Given("An JSON OpenRTB-like BidResponse")
    val stream: URL = this.getClass.getResource("/elasticads-bidresponse.json")
    val Right(originalJson) = parse(scala.io.Source.fromFile(stream.toURI).mkString)

    When("I deserialize it")
    val bidResponse = originalJson.as[BidResponse].right.get

    And("I serialize it")
    val serializedJson = bidResponse.asJson

    /**
      * We will ignore the native part taking it out of the JSONs. The reason is that the library automatically
      * decodes the native response even when i's given in a String format. As a consequence, the serialization
      * process generates a JSON containing a native object rather than a String.
      * The content though is identical: check out `originalJson` and `serializedJson` adm content.
      * @todo edit one of the two versions to make possible to compare them
      */
    Then("There's no difference between the original JSON and the processed one")
    def deleteAdm(json: Json): Json = json.hcursor.downField("seatbid").downArray.downField("bid").downArray.downField("adm").delete.top.get
    val difference = JsonDiff.diff(deleteAdm(originalJson), deleteAdm(serializedJson), remember = false)
    assert(difference.ops.isEmpty)
  }


  test("Deserializing and re-serializing an unfolded JSON BidResponse with native object I obtain the same JSON") {
    import gnieh.diffson.circe._

    Given("An unfolded JSON OpenRTB-like BidResponse")
    val stream: URL = this.getClass.getResource("/elasticads-bidresponse-unfold.json")
    val Right(originalJson) = parse(scala.io.Source.fromFile(stream.toURI).mkString)

    When("I deserialize it")
    val bidResponse = originalJson.as[BidResponse].right.get

    And("I serialize it")
    val serializedJson = bidResponse.asJson

    /**
      * Here we are actually asserting that there is only one difference concerning the `ver` field in the native
      * response object. For compatibility purpose we able to de-serialize `ver` as an Int or as a String, but it
      * gets always serialized as a String.
      */
    Then("There's no difference between the original JSON and the processed one")
    val patch = JsonDiff.diff(serializedJson, originalJson, remember = false)
    assert(patch.ops.head.path.path.exists(p => p.left.exists(name => name == "ver")))
  }

}
