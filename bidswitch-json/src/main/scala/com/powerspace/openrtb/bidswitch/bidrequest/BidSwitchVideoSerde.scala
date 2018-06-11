package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.powerspace.bidswitch.{BidswitchProto, VideoExt}
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.bidrequest.OpenRtbVideoSerde
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * Video BidSwitch extension encoders
  */
object BidSwitchVideoSerde extends EncoderProvider[Imp.Video] {

  import EncodingUtils._
  import JsonUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import io.circe.syntax._

  implicit val videoExt: Encoder[VideoExt] = openrtbEncoder[VideoExt]

  def encoder: Encoder[Imp.Video] = video =>
    OpenRtbVideoSerde.encoder.apply(video).addExtension(video.extension(BidswitchProto.videoExt).asJson)

}
