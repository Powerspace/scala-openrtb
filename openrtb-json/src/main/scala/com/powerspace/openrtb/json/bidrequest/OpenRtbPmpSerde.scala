package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Pmp.Deal
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders

/**
  * OpenRTB Pmp Serde
  */
object OpenRtbPmpSerde extends EncoderProvider[Imp.Pmp] {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._

  implicit val dealEncoder: Encoder[Imp.Pmp.Deal] = openrtbEncoder[Deal]

  def encoder: Encoder[Imp.Pmp] = openrtbEncoder[Imp.Pmp]

}
