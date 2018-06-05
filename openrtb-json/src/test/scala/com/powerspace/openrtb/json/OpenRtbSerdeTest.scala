package com.powerspace.openrtb.json

import java.net.URL

import com.google.openrtb._
import io.circe.Decoder
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.{FunSuite, GivenWhenThen}

class OpenRtbSerdeTest extends FunSuite with GivenWhenThen {

  import OpenRtbSerdeModule._
  import BidRequestFixtures._

  implicit class DecoderResultEnhancement[T](result: Decoder.Result[T]) {
    def value: T = result.right.get
  }

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
    val bidRequest = sampleBidRequest()

    When("I serialize it")
    val json = bidRequest.asJson

    Then("It should return a proper bid request in JSON format")
    println(json)
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

    //@todo
    //val nativeStringCursor = nativeCursor.downField("request_oneof")
    //assert(nativeStringCursor.downField("request").as[String].value == "native-string")
  }

  test("OpenRTB-like bid request [with Native Object] serialization") {
    Given("An OpenRTB-like BidRequest witha a Native Object")
    val bidRequest = sampleBidRequest(withNativeObject = true)

    When("I serialize it")
    val json = bidRequest.asJson

    Then("It should return a proper bid request in JSON format")
    println(json)

    //@todo
    //val nativeStringCursor = json.hcursor.downField("imp").downArray.downField("native").downField("request_oneof")
    //assert(nativeStringCursor.downField("request").as[String].value == "native-string")
  }

}
