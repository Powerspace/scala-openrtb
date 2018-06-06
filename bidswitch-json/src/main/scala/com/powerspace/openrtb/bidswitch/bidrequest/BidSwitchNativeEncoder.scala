package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.powerspace.bidswitch.{BidswitchProto, NativeExt}
import com.powerspace.bidswitch.NativeExt.TripleLift
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.bidrequest.BidRequestSerde
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * Native BidSwitch extension encoders
  */
object BidSwitchNativeEncoder {

  import EncodingUtils._
  import JsonUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import io.circe.syntax._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val tripleLiftExt: Encoder[TripleLift] = deriveEncoder[TripleLift].transformBooleans.clean
  implicit val nativeExt: Encoder[NativeExt] = deriveEncoder[NativeExt].transformBooleans.clean

  implicit def encoder: Encoder[Imp.Native] = native =>
    BidRequestSerde.nativeEncoder.apply(native).addExtension(native.extension(BidswitchProto.requestNativeExt).asJson)
}
