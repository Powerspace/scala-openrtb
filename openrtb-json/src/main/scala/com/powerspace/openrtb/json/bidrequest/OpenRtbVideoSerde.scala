package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Video.CompanionAd
import com.google.openrtb.BidRequest.Imp.Video
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * OpenRTB Video Serde
  */
object OpenRtbVideoSerde {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._

  implicit val bannerEncoder: Encoder[Imp.Banner] = OpenRtbBannerSerde.bannerEncoder

  implicit val companionAdEncoder: Encoder[CompanionAd] = deriveEncoder[CompanionAd].transformBooleans.clean
  implicit val videoEncoder: Encoder[Video] = deriveEncoder[Video].transformBooleans.clean


}
