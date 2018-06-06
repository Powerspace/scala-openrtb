package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.powerspace.bidswitch.{BannerExt, BidswitchProto, Format}
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.bidrequest.OpenRtbBannerSerde
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * Banner BidSwitch extension encoders
  */
object BidSwitchBannerSerde {

  import EncodingUtils._
  import JsonUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import io.circe.syntax._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val formatExt: Encoder[Format] = deriveEncoder[Format].transformBooleans.clean
  implicit val bannerExt: Encoder[BannerExt] = deriveEncoder[BannerExt].transformBooleans.clean

  implicit val bannerEncoder: Encoder[Imp.Banner] = banner =>
    OpenRtbBannerSerde.bannerEncoder.apply(banner).addExtension(banner.extension(BidswitchProto.bannerExt).asJson)

}
