package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.bidrequest.OpenRtbBidRequestSerde.{OpenRtbBidRequestDecoder, OpenRtbBidRequestEncoder}
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumDecoders

/**
  * OpenRTB User Encoder and Decoder
  * @todo split up decoder and encoder
  */
object OpenRtbUserSerde extends EncoderProvider[BidRequest.User] {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import OpenRtbProtobufEnumDecoders._
  import io.circe._

  private implicit val geoEncoder: Encoder[BidRequest.Geo] = OpenRtbBidRequestEncoder.geoEncoder
  private implicit val segmentEncoder: Encoder[BidRequest.Data.Segment] = openRtbEncoder[BidRequest.Data.Segment]
  private implicit val dataEncoder: Encoder[BidRequest.Data] = openRtbEncoder[BidRequest.Data]
  def encoder: Encoder[BidRequest.User] = openRtbEncoder[BidRequest.User]

  private implicit val geoDecoder: Decoder[BidRequest.Geo] = OpenRtbBidRequestDecoder.geoDecoder
  private implicit val segmentDecoder: Decoder[BidRequest.Data.Segment] = openRtbDecoder[BidRequest.Data.Segment]
  private implicit val dataDecoder: Decoder[BidRequest.Data] = openRtbDecoder[BidRequest.Data]
  def decoder: Decoder[BidRequest.User] = openRtbDecoder[BidRequest.User]

}
