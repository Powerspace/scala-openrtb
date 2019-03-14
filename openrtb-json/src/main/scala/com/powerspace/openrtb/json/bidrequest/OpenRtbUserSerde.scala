package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * OpenRTB User Encoder and Decoder
  * @todo split up decoder and encoder
  */
class OpenRtbUserSerde(bidRequestSerde: OpenRtbBidRequestSerde)(implicit er: ExtensionRegistry)
    extends EncoderProvider[BidRequest.User] {

  import EncodingUtils._
  import io.circe._

  private implicit val geoEncoder: Encoder[BidRequest.Geo] = bidRequestSerde.geoEncoder
  private implicit val segmentEncoder: Encoder[BidRequest.Data.Segment] = extendedEncoder[BidRequest.Data.Segment]
  private implicit val dataEncoder: Encoder[BidRequest.Data] = extendedEncoder[BidRequest.Data]
  def encoder: Encoder[BidRequest.User] = extendedEncoder[BidRequest.User]

  private implicit val geoDecoder: Decoder[BidRequest.Geo] = bidRequestSerde.geoDecoder
  private implicit val segmentDecoder: Decoder[BidRequest.Data.Segment] = extendedDecoder[BidRequest.Data.Segment]
  private implicit val dataDecoder: Decoder[BidRequest.Data] = extendedDecoder[BidRequest.Data]
  def decoder: Decoder[BidRequest.User] = extendedDecoder[BidRequest.User]

}
