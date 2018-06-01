package com.powerspace.openrtb.bidswitch

import java.net.URL

import com.google.openrtb.BidResponse
import com.powerspace.bidswitch.BidswitchProto
import io.circe.parser._
import org.scalatest.{FunSuite, GivenWhenThen}

/**
  * @todo provide implicit accessors on BidSwitch structures instead of relying on the extension thing
  * @todo improve toString with extensions
  */
class BidSwitchSerDeTest extends FunSuite with GivenWhenThen {

  import BidSwitchSerdeModule._

  test("BidSwitch bid response deserialization") {
    val stream: URL = getClass.getResource("/bidswitch-bidresponse.json")

    Given("A BidSwitch bid response in JSON format")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I deserialize it")
    val decoded = decode[BidResponse](json)

    Then("It should return a proper Scala BidResponse")
    assert(decoded.isRight)
    val bidResponse = decoded.right.get
    assert(bidResponse.id == "1234567890")
    assert(bidResponse.bidid.isEmpty)
    assert(bidResponse.seatbid.size == 1)
    assert(bidResponse.seatbid.head.bid.size == 2)
    val firstBid = bidResponse.seatbid.head.bid.head
    assert(firstBid.language.contains("en"))
    assert(firstBid.h.isEmpty)
    assert(firstBid.price == 9.43)

    assert(decoded.map(_.extension(BidswitchProto.bidResponse)).isRight)
    val bidResponseExtension = decoded.map(_.extension(BidswitchProto.bidResponse)).right.get.get
    assert(bidResponseExtension.protocol.contains("5.3"))

    assert(decoded.map(_.seatbid.flatMap(_.bid).map(_.extension(BidswitchProto.bidExt))).isRight)
    val bidExtension = decoded.map(_.seatbid.flatMap(_.bid).map(_.extension(BidswitchProto.bidExt)))

    println(decoded)
    println(bidResponseExtension)
    println(bidExtension)
  }

  test("BidSwitch bid request serialization") {
    Given("A BidSwitch BidRequest")
    ???

    When("I serialize it")
    ???

    Then("It should return a proper bid request in JSON format")
    ???
  }
}