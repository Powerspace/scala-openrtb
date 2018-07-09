package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.powerspace.bidswitch.NativeExt
import com.powerspace.bidswitch.NativeExt.TripleLift
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * Native BidSwitch extension encoders
  */
object BidSwitchNativeRequestSerde extends EncoderProvider[Imp.Native] {

  import EncodingUtils._

  private implicit val tripleLiftExtEncoder = openRtbEncoder[TripleLift]
  val nativeExtEncoder = openRtbEncoder[NativeExt]
  private implicit val tripleLiftExtDecoder = openRtbDecoder[TripleLift]
  val nativeExtDecoder = openRtbDecoder[NativeExt]

}
