package com.powerspace.openrtb.bidswitch

import java.net.URL

import com.google.openrtb.BidResponse
import com.powerspace.bidswitch.{BidResponseExt, BidswitchProto}
import io.circe.parser._
import org.scalatest.{FunSuite, GivenWhenThen}

class BidSwitchSerDeTest extends FunSuite with GivenWhenThen {
  import BidSwitchSerdeModule._

  test("bidswitch response parsing") {
    val stream: URL = getClass.getResource("/bidswitch-bidresponse.json")

    Given("A bidswitch bidresponse")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I deserialize it")
    Then("it should return a proper BidResponse")
    val decoded = decode[BidResponse](json)

    // @todo provide implicit accessors on BidSwitch structures instead of relying on the extension thing
    // @todo improve toString with extensions

    println(decoded.map(_.toProtoString))
    println(decoded.map(_.extension(BidswitchProto.bidResponse).map(_.toProtoString)))
    println(decoded.map(_.seatbid.flatMap(_.bid).map(_.extension(BidswitchProto.bidExt).map(_.toProtoString))))
  }

  test("bidswitch response serialization") {
    Given("A bidrequest")
    ???

    When("I serialize it")
    ???

    Then("it should return a proper JSON")
    ???
  }

  test("bidswitch request parsing") {
    Given("A bidrequest")
    ???

    When("I serialize it")
    ???

    Then("it should return a proper JSON")
    ???
  }

  test("bidswitch request serialization") {
    Given("A bidrequest")
    ???

    When("I serialize it")
    ???

    Then("it should return a proper JSON")
    ???
  }


}