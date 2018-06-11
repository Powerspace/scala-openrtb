package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest
import com.google.openrtb.BidRequest.Imp
import com.powerspace.bidswitch.{BidswitchProto, ImpressionExt}
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.bidrequest.OpenRtbImpressionSerde
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * Impression BidSwitch extension encoders
  */
object BidSwitchImpressionSerde extends EncoderProvider[BidRequest.Imp] {

  import EncodingUtils._
  import JsonUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import io.circe.syntax._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  private implicit val googleImpressionEncoder: Encoder[com.powerspace.bidswitch.ImpressionExt.Google] =
    deriveEncoder[com.powerspace.bidswitch.ImpressionExt.Google].cleanRtb
  private implicit val yieldoneImpressionEncoder: Encoder[com.powerspace.bidswitch.ImpressionExt.Yieldone] =
    deriveEncoder[com.powerspace.bidswitch.ImpressionExt.Yieldone].cleanRtb
  private implicit val impressionExt: Encoder[ImpressionExt] = deriveEncoder[ImpressionExt].cleanRtb

  private implicit val nativeEncoder: Encoder[Imp.Native] = BidSwitchNativeSerde.encoder
  private implicit val bannerEncoder: Encoder[Imp.Banner] = BidSwitchBannerSerde.encoder
  private implicit val videoEncoder: Encoder[Imp.Video] = BidSwitchVideoSerde.encoder
  private implicit val pmpEncoder: Encoder[Imp.Pmp] = BidSwitchPmpSerde.encoder

  def encoder: Encoder[BidRequest.Imp] = impression => {
    val impJson = OpenRtbImpressionSerde.encoder.apply(impression)
    val extJson = impression.extension(BidswitchProto.impressionExt).asJson

    impJson.asObject.map(_.add("ext", extJson)).asJson
  }
}
