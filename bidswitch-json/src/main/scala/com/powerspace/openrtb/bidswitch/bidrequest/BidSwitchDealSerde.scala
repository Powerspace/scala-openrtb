package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest.Imp.Pmp
import com.powerspace.bidswitch.{BidswitchProto, DealExt}
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.bidrequest.OpenRtbPmpSerde
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * Deal BidSwitch extension encoders
  */
object BidSwitchDealSerde {

  import EncodingUtils._
  import JsonUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import io.circe.syntax._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val dealExt: Encoder[DealExt] = deriveEncoder[DealExt].transformBooleans.clean

  implicit def encoder: Encoder[Pmp.Deal] = deal =>
    OpenRtbPmpSerde.dealEncoder.apply(deal).addExtension(deal.extension(BidswitchProto.dealExt).asJson)

}
