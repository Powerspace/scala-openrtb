package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.powerspace.bidswitch.{BidswitchProto, VideoExt}
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.bidrequest.BidRequestSerde
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * Video BidSwitch extension encoders
  */
object BidSwitchVideoEncoder {

  import EncodingUtils._
  import JsonUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import io.circe.syntax._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val videoExt: Encoder[VideoExt] = deriveEncoder[VideoExt].transformBooleans.clean

  implicit def encoder: Encoder[Imp.Video] = video =>
    BidRequestSerde.videoEncoder.apply(video).addExtension(video.extension(BidswitchProto.videoExt).asJson)

}
