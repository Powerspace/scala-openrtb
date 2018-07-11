package com.powerspace.openrtb.powerspace

import com.powerspace.openrtb.BidExt
import com.powerspace.openrtb.json.ConfiguredSerde

object PowerspaceBidResponseSerdes extends ConfiguredSerde {

  import com.powerspace.openrtb.json.util.EncodingUtils._
  import io.circe._

  val bidExtEncoder: Encoder[BidExt] = openRtbEncoder[BidExt]
  val bidExtDecoder: Decoder[BidExt] = openRtbDecoder[BidExt]

}