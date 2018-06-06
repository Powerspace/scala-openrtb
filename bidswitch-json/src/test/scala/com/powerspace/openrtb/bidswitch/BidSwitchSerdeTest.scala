package com.powerspace.openrtb.bidswitch

import java.net.URL
import com.google.openrtb.BidResponse
import com.powerspace.bidswitch.BidswitchProto
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.{FunSuite, GivenWhenThen}

class BidSwitchSerdeTest extends FunSuite with GivenWhenThen {

  import BidSwitchSerdeModule._
  import BidRequestFixtures._
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

  test("BidSwitch bid request serialization") {
    Given("A BidSwitch BidRequest with any possible BidSwitch extension")
    val bidRequest = sampleBidRequest()

    When("I serialize it")
    val json = bidRequest.asJson
    println(json)

    Then("It should return a proper bid request with related extensions in JSON format")

    // bid request extensions
    val reqCursor = json.hcursor
    assert(reqCursor.downField("ext").downField("media_src").as[String].value == "powerspace")

    // impression extension
    val impCursor = reqCursor.downField("imp").downArray
    assert(impCursor.downField("ext").downField("inventory_class").as[Int].value == 1)

    // user extension
    val userCursor = reqCursor.downField("user")
    assert(userCursor.downField("ext").downField("cookie_age").as[Int].value == 3)

    // deal extension
    val dealCursor = impCursor.downField("pmp").downField("deals").downArray
    assert(dealCursor.downField("ext").downField("data_src").as[String].value == "datasrc-1")

    // video extension
    val videoCursor = impCursor.downField("video")
    assert(videoCursor.downField("ext").downField("player_type").as[Int].value == 3)

    // banner extension
    val bannerCursor = impCursor.downField("banner")
    assert(bannerCursor.downField("ext").downField("extra_sizes").downField("formats").downArray.downField("w").as[Int].value == 20)

    // native extension
    // @ todo
  }

}