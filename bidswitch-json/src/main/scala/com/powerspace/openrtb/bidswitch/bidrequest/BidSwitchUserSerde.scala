package com.powerspace.openrtb.bidswitch.bidrequest

import com.powerspace.bidswitch.UserExt
import com.powerspace.bidswitch.UserExt.DigiTrust
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * User BidSwitch extension encoders
  */
object BidSwitchUserSerde {

  import EncodingUtils._
  import io.circe._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val digiTrustEncoder: Encoder[DigiTrust] = openRtbEncoder[DigiTrust]
  val userExtEncoder: Encoder[UserExt] = openRtbEncoder[UserExt]

  implicit val digiTrustDecoder: Decoder[DigiTrust] = openRtbDecoder[DigiTrust]
  val userExtDecoder: Decoder[UserExt] = openRtbDecoder[UserExt]

}
