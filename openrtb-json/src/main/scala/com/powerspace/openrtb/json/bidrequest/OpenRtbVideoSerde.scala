package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Video.CompanionAd
import com.google.openrtb.BidRequest.Imp.Video
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * OpenRTB Video Serde
  */
object OpenRtbVideoSerde extends EncoderProvider[Video] {

  import EncodingUtils._
  import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders._
  import io.circe._

  implicit val bannerEncoder: Encoder[Imp.Banner] = OpenRtbBannerSerde.encoder
  implicit val companionAdEncoder: Encoder[CompanionAd] = openrtbEncoder[CompanionAd]

  def encoder: Encoder[Video] = openrtbEncoder[Video]
}
