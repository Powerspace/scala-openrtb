package com.powerspace.openrtb.powerspace

import java.net.URL

import com.google.openrtb._
import com.powerspace.openrtb.PowerspaceProto
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.{FunSuite, GivenWhenThen}

class PowerspaceSerdeModuleTest extends FunSuite with GivenWhenThen {

  import PowerspaceSerdeModule._

  test("Powerspace BidRequest Serialization") {
    Given("A Powerspace BidRequest with extensions")
    val bidRequest = PowerspaceTestData.bidRequest

    When("I serialize it")
    val encoded = bidRequest.asJson

    Then("I should get the related JSON")
    val req = encoded.hcursor
    val bidRequestExt = req.downField("ext")
    assert(bidRequestExt.downField("bcampaign").downArray.as[Int].isRight)
    assert(bidRequestExt.downField("forced_algorithm").as[String].isRight)
    assert(bidRequestExt.downField("forced_deal").as[String].isRight)

    val userExt = req.downField("user").downField("ext")
    assert(userExt.downField("identified").as[Int].isRight)
    assert(userExt.downField("suspicious").as[Int].isRight)

    val geoExt = req.downField("user").downField("geo").downField("ext")
    assert(geoExt.downField("admin1").as[String].isRight)
    assert(geoExt.downField("admin2").as[String].isRight)
    assert(geoExt.downField("geo_names_id").as[Int].isRight)

    val siteExt = req.downField("site").downField("ext")
    assert(siteExt.downField("country").as[String].isRight)
    assert(siteExt.downField("network").as[String].isRight)

    val contentExt = req.downField("site").downField("content").downField("ext")
    assert(contentExt.downField("channel").as[String].isRight)
  }

  test("Powerspace BidRequest Deserialization") {
    Given("A JSON Powerspace BidRequest with extensions")
    val stream: URL = getClass.getResource("/powerspace-bid-request.json")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I deserialize it")
    val decoded = decode[BidRequest](json)

    Then("I should get the related Scala BidRequest")
    val bidRequest = decoded.toTry.get

    val bidRequestExt = bidRequest.extension(PowerspaceProto.bidRequest)
    assert(bidRequestExt.get.forcedAlgorithm.nonEmpty)
    assert(bidRequestExt.get.forcedDeal.nonEmpty)
    assert(bidRequestExt.get.bcampaign.nonEmpty)

    val userExt = bidRequest.user.get.extension(PowerspaceProto.user)
    assert(userExt.get.identified.nonEmpty)
    assert(userExt.get.suspicious.nonEmpty)

    val geoExt = bidRequest.user.get.geo.get.extension(PowerspaceProto.geo)
    assert(geoExt.get.admin1.get.nonEmpty)
    assert(geoExt.get.admin2.get.nonEmpty)
    assert(geoExt.get.geoNamesId.nonEmpty)

    val site = bidRequest.distributionchannelOneof.site.get
    val siteExt = site.extension(PowerspaceProto.siteExt)
    assert(siteExt.get.country.get.nonEmpty)
    assert(siteExt.get.network.get.nonEmpty)

    val contentExt = site.content.get.extension(PowerspaceProto.contentExt)
    assert(contentExt.get.channel.get.nonEmpty)
  }

  test("Powerspace BidResponse Serialization") {
    Given("A Powerspace BidResponse with extensions")
    val bidResponse = PowerspaceTestData.bidResponse

    When("I serialize it")
    val encoded = bidResponse.asJson

    Then("I should get the related JSON")
    val res = encoded.hcursor

    val bidExt = res.downField("seatbid").downArray.downField("bid").downArray.downField("ext")
    assert(bidExt.downField("margin").as[Double].isRight)
    assert(bidExt.downField("cpc").as[Double].isRight)
    assert(bidExt.downField("ctr").as[Double].isRight)
  }

  test("Powerspace BidResponse Deserialization") {
    Given("A JSON Powerspace BidResponse with extensions")
    val stream: URL = getClass.getResource("/powerspace-bid-response.json")
    val json: String = scala.io.Source.fromFile(stream.toURI).mkString

    When("I deserialize it")
    val decoded = decode[BidResponse](json)

    Then("I should get the related Scala BidResponse")
    val bidResponse = decoded.toTry.get
    val bidExt = bidResponse.seatbid.head.bid.head.extension(PowerspaceProto.bid)
    assert(bidExt.get.margin.get > 0d)
    assert(bidExt.get.cpc.get > 0d)
    assert(bidExt.get.ctr.get > 0d)
  }

}