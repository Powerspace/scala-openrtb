package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb._
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import io.circe.Encoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveEncoder

object ImpressionLevelEncoders {

  import EncodingUtils._

  import OpenRtbProtobufEnumEncoders._

  import io.circe._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val bannerEncoder: Encoder[Imp.Banner] = OpenRtbBannerSerde.encoder
  implicit val videoEncoder: Encoder[Imp.Video] = OpenRtbVideoSerde.encoder
  implicit val audioEncoder: Encoder[Imp.Audio] = openrtbEncoder[Imp.Audio]
  implicit val pmpEncoder: Encoder[Imp.Pmp] = OpenRtbPmpSerde.encoder

}

/**
  * OpenRTB Imp Serde
  */
object OpenRtbImpressionSerde {

  import EncodingUtils._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val metricEncoder: Encoder[Imp.Metric] = deriveEncoder[Imp.Metric].cleanRtb

  def encoder(implicit bannerEncoder: Encoder[Imp.Banner],
              videoEncoder: Encoder[Imp.Video],
              audioEncoder: Encoder[Imp.Audio],
              pmpEncoder: Encoder[Imp.Pmp],
              nativeEncode: Encoder[BidRequest.Imp.Native]): Encoder[Imp] =
    openrtbEncoder[Imp]

}
