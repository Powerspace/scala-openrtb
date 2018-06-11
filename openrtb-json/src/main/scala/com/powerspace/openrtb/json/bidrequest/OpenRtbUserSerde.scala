package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders

/**
  * OpenRTB User Serde
  */
object OpenRtbUserSerde extends {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  private implicit val geoEncoder: Encoder[BidRequest.Geo] = OpenRtbBidRequestSerde.geoEncoder
  private implicit val segmentEncoder: Encoder[BidRequest.Data.Segment] = deriveEncoder[BidRequest.Data.Segment].cleanRtb
  private implicit val dataEncoder: Encoder[BidRequest.Data] = deriveEncoder[BidRequest.Data].cleanRtb

  def encoder: Encoder[BidRequest.User] = deriveEncoder[BidRequest.User].cleanRtb
}
