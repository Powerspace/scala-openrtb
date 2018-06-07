package com.powerspace.openrtb.bidswitch.bidresponse

import com.google.openrtb.BidResponse
import com.powerspace.bidswitch.{BidResponseExt, BidswitchProto}
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.bidresponse.OpenRtbBidResponseSerde
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders

/**
  * Decoder for the BidSwitch bid response extension
  */
object BidSwitchBidResponseSerde {

  import io.circe._
  import io.circe.syntax._
  import io.circe.generic.extras.semiauto._
  import JsonUtils._
  import EncodingUtils._
  import BidSwitchBidSerde._
  import OpenRtbProtobufEnumEncoders._

  private implicit val bidResponseExtDecoder: Decoder[BidResponseExt] = _.downField("ext")
    .downField("protocol")
    .as[Option[String]]
    .map(BidResponseExt.apply)

  implicit def bidResponseDecoder: Decoder[BidResponse] = for {
    bidResponse <- OpenRtbBidResponseSerde.decoder
    extension <- bidResponseExtDecoder
  } yield bidResponse.withExtension(BidswitchProto.bidResponseExt)(Some(extension))


  implicit val bidResponseExtension: Encoder[BidResponseExt] = deriveEncoder[BidResponseExt].cleanRtb

  implicit def bidResponseEncoder(implicit seatBidEncoder: Encoder[BidResponse.SeatBid]): Encoder[BidResponse] = bidResponse =>
    OpenRtbBidResponseSerde.encoder.apply(bidResponse).addExtension(bidResponse.extension(BidswitchProto.bidResponseExt).asJson)

}