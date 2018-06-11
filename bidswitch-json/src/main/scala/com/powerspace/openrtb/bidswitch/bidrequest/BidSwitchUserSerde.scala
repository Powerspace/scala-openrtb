package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest
import com.powerspace.bidswitch.UserExt.DigiTrust
import com.powerspace.bidswitch.{BidswitchProto, UserExt}
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.bidrequest.OpenRtbUserSerde
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * User BidSwitch extension encoders
  */
object BidSwitchUserSerde {

  import EncodingUtils._
  import JsonUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import io.circe.syntax._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val digiTrustEncoder: Encoder[DigiTrust] = deriveEncoder[DigiTrust].cleanRtb
  implicit val userExt: Encoder[UserExt] = deriveEncoder[UserExt].cleanRtb

  implicit def encoder: Encoder[BidRequest.User] = user =>
    OpenRtbUserSerde.encoder.apply(user).addExtension(user.extension(BidswitchProto.userExt).asJson)

}
