package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * OpenRTB User Serde
  */
object OpenRtbUserSerde {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._

  implicit val geoEncoder: Encoder[BidRequest.Geo] = OpenRtbBidRequestSerde.geoEncoder

  implicit val segmentEncoder: Encoder[BidRequest.Data.Segment] = deriveEncoder[BidRequest.Data.Segment].transformBooleans.clean
  implicit val dataEncoder: Encoder[BidRequest.Data] = deriveEncoder[BidRequest.Data].transformBooleans.clean
  implicit val userEncoder: Encoder[BidRequest.User] = deriveEncoder[BidRequest.User].transformBooleans.clean

}
