package com.powerspace.openrtb.bidswitch.bidresponse

import com.google.openrtb.NativeResponse
import com.powerspace.bidswitch.{BidswitchProto, NativeResponseExt}
import com.powerspace.openrtb.json.bidresponse.OpenRtbNativeSerde
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration
import io.circe.{Decoder, Encoder, Json}

object BidSwitchNativeResponseSerde {

  import EncodingUtils._
  import io.circe.syntax._
  import io.circe.generic.extras.semiauto._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val nativeExtEncoder: Encoder[NativeResponseExt] = openRtbEncoder[NativeResponseExt]
  implicit val nativeExtDecoder: Decoder[NativeResponseExt] = openRtbDecoder[NativeResponseExt]

}
