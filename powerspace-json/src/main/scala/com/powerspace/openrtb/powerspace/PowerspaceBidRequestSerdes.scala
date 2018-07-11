package com.powerspace.openrtb.powerspace

import com.powerspace.openrtb._
import com.powerspace.openrtb.json.ConfiguredSerde
import com.powerspace.openrtb.json.util.EncodingUtils

object PowerspaceBidRequestSerdes extends ConfiguredSerde {

  import EncodingUtils._

  val bidRequestExtEncoder = openRtbEncoder[BidRequestExt]
  val bidRequestExtDecoder = openRtbDecoder[BidRequestExt]

  val contentExtEncoder = openRtbEncoder[ContentExt]
  val contentExtDecoder = openRtbDecoder[ContentExt]

  val siteExtEncoder = openRtbEncoder[SiteExt]
  val siteDecoder = openRtbDecoder[SiteExt]

  val userExtEncoder = openRtbEncoder[UserExt]
  val userExtDecoder = openRtbDecoder[UserExt]

  val geoExtEncoder = openRtbEncoder[GeoExt]
  val geoExtDecoder = openRtbDecoder[GeoExt]

}