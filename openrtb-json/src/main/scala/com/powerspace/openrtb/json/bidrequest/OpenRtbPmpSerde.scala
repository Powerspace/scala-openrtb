package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Pmp.Deal
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * OpenRTB Pmp Serde
  */
object OpenRtbPmpSerde {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._

  implicit val dealEncoder: Encoder[Imp.Pmp.Deal] = deriveEncoder[Deal].transformBooleans.clean
  implicit val pmpEncoder: Encoder[Imp.Pmp] = deriveEncoder[Imp.Pmp].transformBooleans.clean

}
