package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Banner
import com.google.openrtb.BidRequest.Imp.Banner.Format
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * OpenRTB Banner Serde
  */
object OpenRtbBannerSerde extends EncoderProvider[Imp.Banner] {
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val formatEncoder: Encoder[Format] = deriveEncoder[Format].transformBooleans.clean
  def encoder: Encoder[Imp.Banner] = deriveEncoder[Banner].transformBooleans.clean
}
