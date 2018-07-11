package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Banner.Format
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumDecoders

/**
  * OpenRTB Banner Encoder and Decoder
  */
class OpenRtbBannerSerde(implicit er: ExtensionRegistry) extends EncoderProvider[Imp.Banner] {

    import io.circe._
    import EncodingUtils._
    import OpenRtbProtobufEnumEncoders._
    import OpenRtbProtobufEnumDecoders._

    implicit val formatEncoder: Encoder[Format] = extendedEncoder[Format]
    def encoder: Encoder[Imp.Banner] = extendedEncoder[Imp.Banner]

    implicit val formatDecoder: Decoder[Format] = extendedDecoder[Format]
    def decoder: Decoder[Imp.Banner] = extendedDecoder[Imp.Banner]

}
