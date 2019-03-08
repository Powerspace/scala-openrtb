package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Video
import com.google.openrtb.BidRequest.Imp.Video.CompanionAd
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.common.{OpenRtbProtobufEnumDecoders, OpenRtbProtobufEnumEncoders}
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * OpenRTB Video Encoder and Decoder
  * @todo split up decoder and encoder
  */
class OpenRtbVideoSerde(bannerSerde: OpenRtbBannerSerde)(implicit er: ExtensionRegistry)
    extends EncoderProvider[Imp.Banner] {

  import EncodingUtils._
  import io.circe._
  import OpenRtbProtobufEnumEncoders._
  import OpenRtbProtobufEnumDecoders._

  implicit val bannerEncoder: Encoder[Imp.Banner] = bannerSerde.encoder
  implicit val companionAdEncoder: Encoder[CompanionAd] = extendedEncoder[CompanionAd]
  def encoder: Encoder[Video] = extendedEncoder[Video]

  implicit val companionAdDecoder: Decoder[CompanionAd] = extendedDecoder[CompanionAd]
  implicit val bannerDecoder: Decoder[Imp.Banner] = bannerSerde.decoder
  def decoder: Decoder[Video] = extendedDecoder[Video]

}
