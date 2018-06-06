package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.powerspace.openrtb.json.bidrequest.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.util.EncodingUtils

object BidSwitchPmpSerde {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._

  implicit val dealEncoder: Encoder[Imp.Pmp.Deal] = BidSwitchDealSerde.encoder
  implicit val pmpEncoder: Encoder[Imp.Pmp] = deriveEncoder[Imp.Pmp].transformBooleans.clean

}
