package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Pmp.Deal
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * OpenRTB Pmp Serde
  */
object OpenRtbPmpSerde extends EncoderProvider[Imp.Pmp] {
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val dealEncoder: Encoder[Imp.Pmp.Deal] = deriveEncoder[Deal].transformBooleans.clean

  def encoder: Encoder[Imp.Pmp] = deriveEncoder[Imp.Pmp].transformBooleans.clean
}
