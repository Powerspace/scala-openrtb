package com.powerspace.openrtb.bidswitch.bidrequest

import com.powerspace.bidswitch.ImpressionExt
import com.powerspace.openrtb.json.ConfiguredSerde
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * Impression BidSwitch extension encoders
  */
object BidSwitchImpressionSerde extends ConfiguredSerde {

  import EncodingUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._

  private implicit val googleImpressionEncoder: Encoder[com.powerspace.bidswitch.ImpressionExt.Google] =
    deriveConfiguredEncoder[com.powerspace.bidswitch.ImpressionExt.Google].cleanRtb
  private implicit val yieldoneImpressionEncoder: Encoder[com.powerspace.bidswitch.ImpressionExt.Yieldone] =
    deriveConfiguredEncoder[com.powerspace.bidswitch.ImpressionExt.Yieldone].cleanRtb

  val impressionExtEncoder: Encoder[ImpressionExt] = deriveConfiguredEncoder[ImpressionExt].cleanRtb

  private implicit val googleImpressionDecoder: Decoder[com.powerspace.bidswitch.ImpressionExt.Google] =
    deriveConfiguredDecoder[com.powerspace.bidswitch.ImpressionExt.Google]
  private implicit val yieldoneImpressionDecoder: Decoder[com.powerspace.bidswitch.ImpressionExt.Yieldone] =
    deriveConfiguredDecoder[com.powerspace.bidswitch.ImpressionExt.Yieldone]

  val impressionExtDecoder: Decoder[ImpressionExt] = deriveConfiguredDecoder[ImpressionExt]

}
