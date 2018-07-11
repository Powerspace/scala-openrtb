package com.powerspace.openrtb.powerspace

import com.google.openrtb.BidRequest
import com.google.openrtb.BidRequest.{Content, Geo, User, Site}
import com.google.openrtb.BidResponse.SeatBid.Bid
import com.powerspace.openrtb._
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.SerdeModule

/**
  * Register Powerspace BidResponse extensions
  */
object PowerspaceSerdeModule extends SerdeModule {


  import PowerspaceBidRequestSerdes._
  import PowerspaceBidResponseSerdes._

  override def extensionRegistry: ExtensionRegistry = ExtensionRegistry()
    .registerExtension[BidRequest, BidRequestExt](
      extension = PowerspaceProto.bidRequest,
      encoder = bidRequestExtEncoder,
      decoder = bidRequestExtDecoder
    )
    .registerExtension[Content, ContentExt](
      extension = PowerspaceProto.contentExt,
      encoder = contentExtEncoder,
      decoder = contentExtDecoder
    )
    .registerExtension[Site, SiteExt](
      extension = PowerspaceProto.siteExt,
      encoder = siteExtEncoder,
      decoder = siteDecoder
    )
    .registerExtension[User, UserExt](
      extension = PowerspaceProto.user,
      encoder = userExtEncoder,
      decoder = userExtDecoder
    )
    .registerExtension[Geo, GeoExt](
      extension = PowerspaceProto.geo,
      encoder = geoExtEncoder,
      decoder = geoExtDecoder
    )
    .registerExtension[Bid, BidExt](
      extension = PowerspaceProto.bid,
      encoder = bidExtEncoder,
      decoder = bidExtDecoder
    )

  override def nativeRegistry: ExtensionRegistry = ExtensionRegistry()

}