package com.powerspace.openrtb.powerspace

import com.powerspace.openrtb.json.ConfiguredSerde
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.{BidExt, LinkExt}

/**
  * Link Ext
  * Bid Ext
  */
object PowerspaceBidResponseSerdes extends ConfiguredSerde {

  import EncodingUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._

  val linkExtEncoder: Encoder[LinkExt] = deriveEncoder[LinkExt].cleanRtb
  val bidExtEncoder: Encoder[BidExt] = deriveEncoder[BidExt].cleanRtb

  val linkExtDecoder: Decoder[LinkExt] = deriveDecoder[LinkExt]
  val bidExtDecoder: Decoder[BidExt] = deriveDecoder[BidExt]

}