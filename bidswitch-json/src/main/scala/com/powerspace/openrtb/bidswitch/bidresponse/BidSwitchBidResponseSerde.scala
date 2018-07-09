package com.powerspace.openrtb.bidswitch.bidresponse

import com.powerspace.bidswitch.BidResponseExt
import com.powerspace.openrtb.json.ConfiguredSerde
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * Decoder for the BidSwitch bid response extension
  */
object BidSwitchBidResponseSerde extends ConfiguredSerde {

  import EncodingUtils._
  import io.circe._

  implicit val bidResponseExtEncoder: Encoder[BidResponseExt] = openRtbEncoder[BidResponseExt]

  implicit val bidResponseExtDecoder: Decoder[BidResponseExt] = openRtbDecoder[BidResponseExt]

}