package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Video.CompanionAd
import com.google.openrtb.BidRequest.Imp.Video
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * OpenRTB Video Serde
  */
object OpenRtbVideoSerde {

  import EncodingUtils._
  import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val bannerEncoder: Encoder[Imp.Banner] = OpenRtbBannerSerde.encoder
  implicit val companionAdEncoder: Encoder[CompanionAd] = deriveEncoder[CompanionAd].cleanRtb

  def encoder: Encoder[Video] = deriveEncoder[Video].cleanRtb
}
