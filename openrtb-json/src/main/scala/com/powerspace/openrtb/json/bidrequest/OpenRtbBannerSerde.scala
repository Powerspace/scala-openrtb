package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Banner
import com.google.openrtb.BidRequest.Imp.Banner.Format
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders

/**
  * OpenRTB Banner Serde
  */
object OpenRtbBannerSerde {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._

  implicit val formatEncoder: Encoder[Format] = deriveEncoder[Format].cleanRtb
  implicit val bannerEncoder: Encoder[Imp.Banner] = deriveEncoder[Banner].cleanRtb

}
