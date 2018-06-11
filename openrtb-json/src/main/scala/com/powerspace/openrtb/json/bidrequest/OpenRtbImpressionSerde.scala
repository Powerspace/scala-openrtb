package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb._
import com.google.openrtb.NativeRequest.EventTrackers
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * OpenRTB Imp Serde
  */
object OpenRtbImpressionSerde extends EncoderProvider[BidRequest.Imp] {
  import EncodingUtils._
  import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders._

  import io.circe._
  import io.circe.syntax._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames
  import io.circe.generic.extras.semiauto._

  implicit val bannerEncoder = OpenRtbBannerSerde.encoder
  implicit val videoEncoder = OpenRtbVideoSerde.encoder
  implicit val audioEncoder: Encoder[Imp.Audio] = deriveEncoder[Imp.Audio].cleanRtb
  implicit val pmpEncoder = OpenRtbPmpSerde.encoder

  implicit val nativeRequestEncoder = OpenRtbNativeRequestSerde.encoder
  implicit val metricEncoder: Encoder[Imp.Metric] = deriveEncoder[Imp.Metric].cleanRtb

  def encoder: Encoder[Imp] = deriveEncoder[Imp].cleanRtb
}
