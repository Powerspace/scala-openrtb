package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * OpenRTB User Encoder and Decoder
  * @todo split up decoder and encoder
  */
class OpenRtbUserSerde(implicit er: ExtensionRegistry) extends EncoderProvider[BidRequest.User] {

  import EncodingUtils._
  import io.circe._

  private implicit val geoEncoder: Encoder[BidRequest.Geo] = new OpenRtbBidRequestSerde().OpenRtbBidRequestEncoder.geoEncoder
  private implicit val segmentEncoder: Encoder[BidRequest.Data.Segment] = openRtbEncoder[BidRequest.Data.Segment]
  private implicit val dataEncoder: Encoder[BidRequest.Data] = openRtbEncoder[BidRequest.Data]
  def encoder: Encoder[BidRequest.User] = extendedEncoder[BidRequest.User]

  private implicit val geoDecoder: Decoder[BidRequest.Geo] = new OpenRtbBidRequestSerde().OpenRtbBidRequestDecoder.geoDecoder
  private implicit val segmentDecoder: Decoder[BidRequest.Data.Segment] = openRtbDecoder[BidRequest.Data.Segment]
  private implicit val dataDecoder: Decoder[BidRequest.Data] = openRtbDecoder[BidRequest.Data]
  def decoder: Decoder[BidRequest.User] = extendedDecoder[BidRequest.User]

}
