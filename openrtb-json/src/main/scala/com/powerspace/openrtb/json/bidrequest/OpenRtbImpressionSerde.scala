package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb._
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * OpenRTB Imp Serde
  */
object OpenRtbImpressionSerde extends EncoderProvider[BidRequest.Imp] {
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._

  import io.circe._
  import io.circe.syntax._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  import io.circe.generic.extras.semiauto._
  implicit val bannerEncoder = OpenRtbBannerSerde.encoder
  implicit val videoEncoder = OpenRtbVideoSerde.encoder
  implicit val pmpEncoder = OpenRtbPmpSerde.encoder
  implicit val metricEncoder: Encoder[Imp.Metric] = deriveEncoder[Imp.Metric].transformBooleans.clean
  implicit val audioEncoder: Encoder[Imp.Audio] = deriveEncoder[Imp.Audio].transformBooleans.clean

  implicit val nativeEncoder = OpenRtbNativeSerde.encoder

  def encoder: Encoder[Imp] = deriveEncoder[Imp].transformBooleans.clean
    .mapJson(identity(_))

}
