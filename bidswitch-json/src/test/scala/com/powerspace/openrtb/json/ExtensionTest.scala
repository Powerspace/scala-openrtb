package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest
import com.powerspace.bidswitch.{BidRequestExt, BidswitchProto}
import com.powerspace.openrtb.bidswitch.bidrequest.BidSwitchBidRequestSerde
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.scalatest.{FunSuite, GivenWhenThen}

class ExtensionTest extends FunSuite with GivenWhenThen {

  import OpenrtbExtensions._
  val decoder: Decoder[BidRequestExt] = new Decoder[BidRequestExt] {
    override def apply(c: HCursor): Result[BidRequestExt] = ???
  }

  val bidRequestExt = BidswitchProto.bidRequestExt
  val bidRequestExtEncoder = BidSwitchBidRequestSerde.bidRequestExt
  val bidRequestExtDecoder = decoder
  val baseEncoder = OpenRtbSerdeModule.bidRequestEncoder

  test("Automatic encoder builder") {
    val extensionRegistry = ExtensionRegistry(Seq())
      .registerExtension[BidRequest, BidRequestExt](
      extension = bidRequestExt,
      encoder = bidRequestExtEncoder,
      decoder = bidRequestExtDecoder)


    val bidswitchEncoder = extensionRegistry.encoderWithExtensions[BidRequest](baseEncoder)

    val result = bidswitchEncoder.apply(com.powerspace.openrtb.bidswitch.BidRequestFixtures.sampleBidRequest(true))
      .hcursor.downField("ext").as[Json]

    assert(result.toOption.isDefined)

    // @todo make openrtbEncoder with extensions management
    // @todo make openrtbDecoder with extensions management

  }
}
