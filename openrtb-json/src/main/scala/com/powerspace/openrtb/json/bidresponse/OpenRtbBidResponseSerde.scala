package com.powerspace.openrtb.json.bidresponse

import com.google.openrtb.{BidResponse, NoBidReason}
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * OpenRTB BidResponse Encoder and Decoder
  */
class OpenRtbBidResponseSerde(implicit er: ExtensionRegistry) extends EncoderProvider[BidResponse] {

  import OpenRtbProtobufEnumEncoders._
  import EncodingUtils._
  import io.circe._

  def encoder(implicit seatBidEncoder: Encoder[BidResponse.SeatBid]): Encoder[BidResponse] = extendedEncoder[BidResponse]

  private implicit val noBidReasonDecoder: Decoder[Option[NoBidReason]] = Decoder.decodeOption[Int].map(_.map(NoBidReason.fromValue))

  def decoder(implicit seatBidDecoder: Decoder[BidResponse.SeatBid]): Decoder[BidResponse] = extendedDecoder[BidResponse]

}
