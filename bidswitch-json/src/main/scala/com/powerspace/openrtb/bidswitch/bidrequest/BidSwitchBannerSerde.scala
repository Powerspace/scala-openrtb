package com.powerspace.openrtb.bidswitch.bidrequest


import com.google.openrtb.BidRequest.Imp
import com.powerspace.bidswitch.{BannerExt, Format}
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.common.{OpenRtbProtobufEnumDecoders, OpenRtbProtobufEnumEncoders}
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * Banner BidSwitch extension encoders
  */
object BidSwitchBannerSerde extends EncoderProvider[Imp.Banner] {

  import OpenRtbProtobufEnumEncoders._
  import OpenRtbProtobufEnumDecoders._
  import EncodingUtils._

  implicit val formatEncoder = openRtbEncoder[Format]
  val bannerExtEncoder = openRtbEncoder[BannerExt]

  implicit val formatDecoder = openRtbDecoder[Format]
  val bannerExtDecoder = openRtbDecoder[BannerExt]

}
