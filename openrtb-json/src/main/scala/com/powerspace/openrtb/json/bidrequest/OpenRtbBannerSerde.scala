package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Banner
import com.google.openrtb.BidRequest.Imp.Banner.Format
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders

/**
  * OpenRTB Banner Serde
  */
object OpenRtbBannerSerde extends EncoderProvider[Imp.Banner] {
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val formatEncoder: Encoder[Format] = deriveEncoder[Format].cleanRtb
  def encoder: Encoder[Imp.Banner] = deriveEncoder[Banner].cleanRtb
}
