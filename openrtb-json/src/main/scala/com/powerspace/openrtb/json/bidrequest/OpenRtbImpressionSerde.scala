package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.bidrequest.OpenRtbBannerSerde.{OpenRtbBannerDecoder, OpenRtbBannerEncoder}
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumDecoders
import io.circe.generic.extras.Configuration
import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.semiauto._

/**
  * OpenRTB Imp Encoder and Decoder
  * @todo split up decoder and encoder
  */
object ImpressionLevelSerdes {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import OpenRtbProtobufEnumDecoders._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames.withDefaults

  implicit val bannerEncoder: Encoder[Imp.Banner] = OpenRtbBannerEncoder.encoder
  implicit val videoEncoder: Encoder[Imp.Video] = OpenRtbVideoSerde.encoder
  implicit val audioEncoder: Encoder[Imp.Audio] = openRtbEncoder[Imp.Audio]
  implicit val pmpEncoder: Encoder[Imp.Pmp] = OpenRtbPmpSerde.encoder

  implicit val bannerDecoder: Decoder[Imp.Banner] = OpenRtbBannerDecoder.decoder
  implicit val videoDecoder: Decoder[Imp.Video] = OpenRtbVideoSerde.decoder
  implicit val audioDecoder: Decoder[Imp.Audio] = openRtbDecoder[Imp.Audio]
  implicit val pmpDecoder: Decoder[Imp.Pmp] = OpenRtbPmpSerde.decoder

}

object OpenRtbImpressionSerde extends EncoderProvider[Imp] {

  import EncodingUtils._

  implicit val metricEncoder: Encoder[Imp.Metric] = deriveEncoder[Imp.Metric].cleanRtb
  def encoder(implicit bannerEncoder: Encoder[Imp.Banner],
              videoEncoder: Encoder[Imp.Video],
              audioEncoder: Encoder[Imp.Audio],
              pmpEncoder: Encoder[Imp.Pmp],
              nativeEncode: Encoder[Imp.Native]): Encoder[Imp] =
    openRtbEncoder[Imp]

  implicit val metricDecoder: Decoder[Imp.Metric] = deriveDecoder[Imp.Metric]
  def decoder(implicit bannerDecoder: Decoder[Imp.Banner],
              videoDecoder: Decoder[Imp.Video],
              audioDecoder: Decoder[Imp.Audio],
              pmpDecoder: Decoder[Imp.Pmp],
              nativeDecode: Decoder[Imp.Native]): Decoder[Imp] =
    openRtbDecoder[Imp]

}