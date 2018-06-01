package com.powerspace.openrtb.json

import java.net.URL
import com.google.openrtb.{BidRequest, BidResponse}
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

    //@todo adm to be tested
  }

  test("OpenRTB-like bid request serialization") {
    Given("An OpenRTB-like BidRequest")
    //val bidRequest = BidRequest(id = "br-id-1", imp = Seq(Imp(id = "imp-1")), cur = Seq("EUR"))

    When("I serialize it")
    //val json = bidRequest.asJson

    Then("It should return a proper bid request in JSON format")
    //println(json)
  }

}