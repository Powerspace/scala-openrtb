package com.powerspace.openrtb.bidswitch

import java.net.URL

import com.google.openrtb.BidResponse
import com.powerspace.bidswitch.{BidResponseExt, BidswitchProto}
import io.circe.parser._
import org.scalatest.{FunSuite, GivenWhenThen}

class BidSwitchSerDeTest extends FunSuite with GivenWhenThen {
  import BidSwitchBidResponseSerde._

  test("bidswitch parsing") {
    val stream: URL = getClass.getResource("/bidswitch-bidresponse.json")

    Given("A bidswitch bidresponse")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I deserialize it")
    Then("it should return a proper BidResponse")
    val decoded = decode[BidResponse](json)
    println(decoded)
    println(decoded.map(_.extension(BidswitchProto.bidResponse)))
    println(decoded.map(_.seatbid.flatMap(_.bid).map(_.extension(BidswitchProto.bidExt))))
  }

  test("bidswitch deser") {
    Given("A bidrequest")
    ???

    When("I serialize it")
    ???

    Then("it should return a proper JSON")
    ???
  }
}