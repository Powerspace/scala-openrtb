package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.EncoderProvider

import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

object BidSwitchPmpSerde extends EncoderProvider[Imp.Pmp] {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val dealEncoder: Encoder[Imp.Pmp.Deal] = BidSwitchDealSerde.encoder
  implicit val pmpEncoder: Encoder[Imp.Pmp] = deriveEncoder[Imp.Pmp].cleanRtb

  def encoder: Encoder[Imp.Pmp] = deriveEncoder[Imp.Pmp].transformBooleans.clean
}
