package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Video.CompanionAd
import com.google.openrtb.BidRequest.Imp.Video
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.bidrequest.OpenRtbBannerSerde.{OpenRtbBannerDecoder, OpenRtbBannerEncoder}
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumDecoders

/**
  * OpenRTB Video Encoder and Decoder
  * @todo split up decoder and encoder
  */
object OpenRtbVideoSerde extends EncoderProvider[Imp.Banner] {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import OpenRtbProtobufEnumDecoders._
  import io.circe._

  implicit val bannerEncoder: Encoder[Imp.Banner] = OpenRtbBannerEncoder.encoder
  implicit val companionAdEncoder: Encoder[CompanionAd] = openRtbEncoder[CompanionAd]
  def encoder: Encoder[Video] = openRtbEncoder[Video]

  implicit val companionAdDecoder: Decoder[CompanionAd] = openRtbDecoder[CompanionAd]
  implicit val bannerDecoder: Decoder[Imp.Banner] = OpenRtbBannerDecoder.decoder
  def decoder: Decoder[Video] = openRtbDecoder[Video]

}
