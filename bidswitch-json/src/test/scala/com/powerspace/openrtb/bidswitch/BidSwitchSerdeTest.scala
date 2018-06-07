package com.powerspace.openrtb.bidswitch

import java.net.URL

import com.google.openrtb.BidResponse
import com.powerspace.bidswitch.BidswitchProto
import com.powerspace.openrtb.bidswitch.BidRequestFixtures._
import com.powerspace.openrtb.bidswitch.BidResponseFixtures._
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.{FunSuite, GivenWhenThen}

class BidSwitchSerdeTest extends FunSuite with GivenWhenThen {

  import BidSwitchSerdeModule._
  import com.powerspace.openrtb.json.util.EncodingUtils._

  test("BidSwitch bid response deserialization") {
    Given("A BidSwitch bid response in JSON format")
    val stream: URL = getClass.getResource("/bidswitch-bidresponse.json")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I deserialize it")
    val decoded = decode[BidResponse](json)

    Then("It should return a proper Scala BidResponse")
    val bidResponse = decoded.toTry.get
    assert(bidResponse.id == "1234567890")
    assert(bidResponse.bidid.isEmpty)
    assert(bidResponse.seatbid.size == 1)
    assert(bidResponse.seatbid.head.bid.size == 2)
    val firstBid = bidResponse.seatbid.head.bid.head
    assert(firstBid.language.contains("en"))
    assert(firstBid.h.isEmpty)
    assert(firstBid.price == 9.43)

    val bidResponseExtension = decoded.map(_.extension(BidswitchProto.bidResponseExt)).toTry.get.get
    assert(bidResponseExtension.protocol.contains("5.3"))

    val bidExtension = decoded.map(_.seatbid.flatMap(_.bid).flatMap(_.extension(BidswitchProto.bidExt))).toTry.get.head
    assert(bidExtension.advertiserName.nonEmpty)

    assert(bidExtension.native.get.link.url.nonEmpty)
    assert(bidExtension.native.get.assets.nonEmpty)
    assert(bidExtension.native.get.assets.head.required.contains(true))
    assert(bidExtension.native.get.assets.head.getTitle.text.nonEmpty)

    val nativeExtension = bidExtension.native.get.extension(BidswitchProto.responseNativeExt).get
    assert(nativeExtension.adchoiceurl.isEmpty)
    assert(nativeExtension.viewtracker.isEmpty)
  }

  test("BidSwitch bid response serialization with no-bid") {
    Given("An BidSwitch bid response in JSON format with no bid")
    val stream: URL = getClass.getResource("/bidswitch-bidresponse-no-bid.json")
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

  test("BidSwitch Native bid request serialization") {
    Given("A BidSwitch BidRequest with any possible BidSwitch extension and a native object")
    val bidRequest = sampleBidRequest(withNativeObject = true)

    When("I serialize it")
    val json = bidRequest.asJson

    Then("It should return a proper native bid request with related extensions in JSON format")

    // bid request extensions
    val reqCursor = json.hcursor
    assert(reqCursor.downField("ext").downField("media_src").as[String].value == "powerspace")

    // impression extension
    val impCursor = reqCursor.downField("imp").downArray
    assert(impCursor.downField("ext").downField("inventory_class").as[Int].value == 1)

    // user extension
    val userCursor = reqCursor.downField("user")
    assert(userCursor.downField("ext").downField("cookie_age").as[Int].value == 3)

    // video extension
    val videoCursor = impCursor.downField("video")
    assert(videoCursor.downField("ext").downField("player_type").as[Int].value == 3)

    // banner extension
    val bannerCursor = impCursor.downField("banner")
    assert(bannerCursor.downField("ext").downField("extra_sizes").downArray.downField("w").as[Int].value == 20)

    // deal extension
    val dealCursor = impCursor.downField("pmp").downField("deals").downArray
    assert(dealCursor.downField("ext").downField("data_src").as[String].value == "datasrc-1")

    // native extension
    val nativeCursor = impCursor.downField("native")
    assert(nativeCursor.downField("ext").downField("triplelift").downField("formats").downArray.as[Int].value == 10)
  }

  test("BidSwitch NON-Native bid request serialization") {
    Given("A BidSwitch BidRequest without a Native object")
    val bidRequest = sampleBidRequest(withNativeObject = false)

    When("I serialize it")
    val json = bidRequest.asJson
    println(json)

    Then("It should return a proper NON-Native bid request")
    val nativeCursor = json.hcursor.downField("imp").downArray.downField("native").downField("request")
    assert(nativeCursor.as[String].value == "native-string")
  }

  test("BidSwitch Native bid response serialization") {
    Given("A BidSwitch BidResponse with any possible BidSwitch extension and a native object")
    val bidResponse = sampleBidResponse(withNativeObject = true)

    When("I serialize it")
    val json = bidResponse.asJson
    println(json)
    Then("It should return a proper native bid response with related extensions in JSON format")

    // bid request extensions
    val resCursor = json.hcursor
    //assert(reqCursor.downField("ext").downField("media_src").as[String].value == "powerspace")
  }

}