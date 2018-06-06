package com.powerspace.openrtb.bidswitch.bidresponse

import com.google.openrtb.BidResponse
import com.powerspace.bidswitch.{BidResponseExt, BidswitchProto}
import com.powerspace.openrtb.json.bidresponse.OpenRtbBidResponseSerde
import io.circe.generic.extras.Configuration

/**
  * Decoder for the BidSwitch bid response extension
  */
object BidSwitchBidResponseSerde {

  import io.circe._
  import BidSwitchBidSerde._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  private implicit val bidResponseExtDecoder: Decoder[BidResponseExt] = _.downField("ext")
    .downField("protocol")
    .as[Option[String]]
    .map(BidResponseExt.apply)

  implicit def decoder: Decoder[BidResponse] = for {
    bidResponse <- OpenRtbBidResponseSerde.decoder
    extension <- bidResponseExtDecoder
  } yield bidResponse.withExtension(BidswitchProto.bidResponseExt)(Some(extension))

}
