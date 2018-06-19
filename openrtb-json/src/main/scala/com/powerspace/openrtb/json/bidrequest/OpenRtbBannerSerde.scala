package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Banner.Format
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumDecoders

/**
  * OpenRTB Banner Encoder and Decoder
  */
object OpenRtbBannerSerde extends EncoderProvider[Imp.Banner] {

    import io.circe._
    import EncodingUtils._

    object OpenRtbBannerEncoder {
        import OpenRtbProtobufEnumEncoders._

        implicit val formatEncoder: Encoder[Format] = openRtbEncoder[Format]
        def encoder: Encoder[Imp.Banner] = openRtbEncoder[Imp.Banner]
    }

    object OpenRtbBannerDecoder {
        import OpenRtbProtobufEnumDecoders._

        implicit val formatDecoder: Decoder[Format] = openRtbDecoder[Format]
        def decoder: Decoder[Imp.Banner] = openRtbDecoder[Imp.Banner]
    }

}
