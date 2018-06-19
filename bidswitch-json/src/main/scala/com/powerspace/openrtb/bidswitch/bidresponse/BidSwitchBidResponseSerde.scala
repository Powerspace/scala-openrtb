package com.powerspace.openrtb.bidswitch.bidresponse

import com.google.openrtb.BidResponse
import com.powerspace.bidswitch.{BidResponseExt, BidswitchProto}
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.bidresponse.OpenRtbBidResponseSerde
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders

/**
  * Decoder for the BidSwitch bid response extension
  */
object BidSwitchBidResponseSerde extends EncoderProvider[BidResponse] {

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
  private implicit val bidResponseExtEncoder: Encoder[BidResponseExt] = deriveEncoder[BidResponseExt].cleanRtb

  private implicit val seatBidEncoder: Encoder[BidResponse.SeatBid] = BidSwitchBidSerde.seatBidEncoder

  def decoder: Decoder[BidResponse] = for {
    bidResponse <- OpenRtbBidResponseSerde.decoder
    extension <- bidResponseExtDecoder
  } yield bidResponse.withExtension(BidswitchProto.bidResponseExt)(Some(extension))

  def encoder: Encoder[BidResponse] = bidResponse =>
    OpenRtbBidResponseSerde.encoder.apply(bidResponse).addExtension(bidResponse.extension(BidswitchProto.bidResponseExt).asJson)
}