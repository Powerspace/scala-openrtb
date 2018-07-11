package com.powerspace.openrtb.bidswitch.bidresponse

import com.powerspace.bidswitch.NativeResponseExt
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration
import io.circe.{Decoder, Encoder}

object BidSwitchNativeResponseSerde {

  import EncodingUtils._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val nativeExtEncoder: Encoder[NativeResponseExt] = openRtbEncoder[NativeResponseExt]
  implicit val nativeExtDecoder: Decoder[NativeResponseExt] = openRtbDecoder[NativeResponseExt]

}
