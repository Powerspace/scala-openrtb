package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest
import com.google.openrtb.BidRequest.Imp
import com.powerspace.bidswitch.{BidswitchProto, ImpressionExt}
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.bidrequest.OpenRtbImpressionSerde
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * Impression BidSwitch extension encoders
  */
object BidSwitchImpressionSerde {

  import EncodingUtils._
  import JsonUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import io.circe.syntax._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val googleImpressionEncoder: Encoder[com.powerspace.bidswitch.ImpressionExt.Google] =
    deriveEncoder[com.powerspace.bidswitch.ImpressionExt.Google].cleanRtb
  implicit val yieldoneImpressionEncoder: Encoder[com.powerspace.bidswitch.ImpressionExt.Yieldone] =
    deriveEncoder[com.powerspace.bidswitch.ImpressionExt.Yieldone].cleanRtb
  implicit val impressionExt: Encoder[ImpressionExt] = deriveEncoder[ImpressionExt].cleanRtb

  implicit val nativeEncoder: Encoder[Imp.Native] = BidSwitchNativeSerde.nativeEncoder
  implicit val bannerEncoder: Encoder[Imp.Banner] = BidSwitchBannerSerde.bannerEncoder
  implicit val videoEncoder: Encoder[Imp.Video] = BidSwitchVideoSerde.videoEncoder
  implicit val pmpEncoder: Encoder[Imp.Pmp] = BidSwitchPmpSerde.pmpEncoder

  implicit def encoder: Encoder[BidRequest.Imp] = impression =>
    OpenRtbImpressionSerde.encoder.apply(impression).addExtension(impression.extension(BidswitchProto.impressionExt).asJson)

}
