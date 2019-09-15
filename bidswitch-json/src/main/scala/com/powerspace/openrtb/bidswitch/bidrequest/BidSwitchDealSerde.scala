package com.powerspace.openrtb.bidswitch.bidrequest

import com.powerspace.bidswitch.DealExt
import com.powerspace.openrtb.json.ConfiguredSerde
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * Deal BidSwitch extension encoders
  */
object BidSwitchDealSerde extends ConfiguredSerde {

  import EncodingUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._

  implicit val dealExtEncoder: Encoder[DealExt] = deriveConfiguredEncoder[DealExt].cleanRtb

  val dealExtDecoder: Decoder[DealExt] = deriveConfiguredDecoder[DealExt]

}
