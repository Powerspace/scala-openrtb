package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.powerspace.bidswitch.VideoExt
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * Video BidSwitch extension encoders
  */
object BidSwitchVideoSerde extends EncoderProvider[Imp.Video] {

  import EncodingUtils._
  import io.circe._

  val videoExtEncoder: Encoder[VideoExt] = openRtbEncoder[VideoExt]
  val videoExtDecoder: Decoder[VideoExt] = openRtbDecoder[VideoExt]

}
