package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders

/**
  * OpenRTB User Serde
  */
object OpenRtbUserSerde {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._

  implicit val geoEncoder: Encoder[BidRequest.Geo] = OpenRtbBidRequestSerde.geoEncoder

  implicit val segmentEncoder: Encoder[BidRequest.Data.Segment] = deriveEncoder[BidRequest.Data.Segment].cleanRtb
  implicit val dataEncoder: Encoder[BidRequest.Data] = deriveEncoder[BidRequest.Data].cleanRtb
  implicit def encoder: Encoder[BidRequest.User] = deriveEncoder[BidRequest.User].cleanRtb

}
