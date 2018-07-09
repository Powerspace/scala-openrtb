package com.powerspace.openrtb.powerspace

import com.powerspace.openrtb.BidRequestExt
import com.powerspace.openrtb.json.ConfiguredSerde
import com.powerspace.openrtb.json.util.EncodingUtils

object PowerspaceBidRequestSerdes extends ConfiguredSerde {

  import EncodingUtils._

  val extEncoder = openRtbEncoder[BidRequestExt]
  val extDecoder = openRtbDecoder[BidRequestExt]

}