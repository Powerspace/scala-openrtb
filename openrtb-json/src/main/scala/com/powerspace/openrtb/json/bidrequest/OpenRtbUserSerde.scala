package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders

/**
  * OpenRTB User Serde
  */
object OpenRtbUserSerde extends EncoderProvider[BidRequest.User] {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._

  private implicit val geoEncoder: Encoder[BidRequest.Geo] = OpenRtbBidRequestSerde.geoEncoder
  private implicit val segmentEncoder: Encoder[BidRequest.Data.Segment] = openrtbEncoder[BidRequest.Data.Segment]
  private implicit val dataEncoder: Encoder[BidRequest.Data] = openrtbEncoder[BidRequest.Data]

  def encoder: Encoder[BidRequest.User] =
    openrtbEncoder[BidRequest.User]

}
