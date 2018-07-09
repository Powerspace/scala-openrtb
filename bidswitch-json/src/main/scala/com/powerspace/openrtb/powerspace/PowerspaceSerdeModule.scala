package com.powerspace.openrtb.powerspace

import com.google.openrtb.BidResponse.SeatBid.Bid
import com.google.openrtb.NativeResponse.Link
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.SerdeModule
import com.powerspace.openrtb.{BidExt, LinkExt, PowerspaceProto}

/**
  * @todo write tests
  */
object PowerspaceSerdeModule extends SerdeModule {


  /**
    * Register Powerspace BidResponse extensions
    */
  override def extensionRegistry: ExtensionRegistry = ExtensionRegistry()
    .registerExtension[Bid, BidExt](
    extension = PowerspaceProto.bid,
    encoder = PowerspaceBidResponseSerdes.bidExtEncoder,
    decoder = PowerspaceBidResponseSerdes.bidExtDecoder
  )
    .registerExtension[Link, LinkExt](
    extension = PowerspaceProto.link,
    encoder = PowerspaceBidResponseSerdes.linkExtEncoder,
    decoder = PowerspaceBidResponseSerdes.linkExtDecoder
  )

  override def nativeRegistry: ExtensionRegistry = ExtensionRegistry()
}