package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumDecoders
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.{ConfiguredSerde, EncoderProvider}
import io.circe.generic.extras.semiauto._
import io.circe.{Decoder, Encoder}

/**
  * OpenRTB Imp Encoder and Decoder
  * @todo split up decoder and encoder
  */
class ImpressionLevelSerdes(videoSerde: OpenRtbVideoSerde)(implicit er: ExtensionRegistry) extends ConfiguredSerde {

  import EncodingUtils._

  import OpenRtbProtobufEnumEncoders._
  import OpenRtbProtobufEnumDecoders._

  private val openRtbBannerSerde = new OpenRtbBannerSerde()
  private val pmpSerde = new OpenRtbPmpSerde()

  implicit val bannerEncoder: Encoder[Imp.Banner] = openRtbBannerSerde.encoder
  val videoEncoder: Encoder[Imp.Video] = videoSerde.encoder
  val audioEncoder: Encoder[Imp.Audio] = extendedEncoder[Imp.Audio]

  implicit def pmpEncoder(implicit dealEncoder: Encoder[Imp.Pmp.Deal]): Encoder[Imp.Pmp] = pmpSerde.encoder

  implicit val bannerDecoder: Decoder[Imp.Banner] = openRtbBannerSerde.decoder
  val videoDecoder: Decoder[Imp.Video] = videoSerde.decoder
  val audioDecoder: Decoder[Imp.Audio] = extendedDecoder[Imp.Audio]

  implicit def pmpDecoder(implicit dealDecoder: Decoder[Imp.Pmp.Deal]): Decoder[Imp.Pmp] = pmpSerde.decoder
}

class OpenRtbImpressionSerde(implicit extensionRegistry: ExtensionRegistry) extends EncoderProvider[Imp] {

  import EncodingUtils._

  implicit val metricEncoder: Encoder[Imp.Metric] = deriveEncoder[Imp.Metric].cleanRtb
  def encoder(implicit bannerEncoder: Encoder[Imp.Banner],
              videoEncoder: Encoder[Imp.Video],
              audioEncoder: Encoder[Imp.Audio],
              pmpEncoder: Encoder[Imp.Pmp],
              nativeEncode: Encoder[Imp.Native]): Encoder[Imp] =
    extendedEncoder[Imp]

  implicit val metricDecoder: Decoder[Imp.Metric] = deriveDecoder[Imp.Metric]
  def decoder(implicit bannerDecoder: Decoder[Imp.Banner],
              videoDecoder: Decoder[Imp.Video],
              audioDecoder: Decoder[Imp.Audio],
              pmpDecoder: Decoder[Imp.Pmp],
              nativeDecode: Decoder[Imp.Native]): Decoder[Imp] =
    extendedDecoder[Imp]

}