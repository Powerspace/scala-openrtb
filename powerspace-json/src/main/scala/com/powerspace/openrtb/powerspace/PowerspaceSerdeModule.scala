package com.powerspace.openrtb.powerspace

import com.powerspace.openrtb.PowerspaceProto
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.SerdeModule

/**
  * Register Powerspace BidResponse extensions
  */
object PowerspaceSerdeModule extends SerdeModule {
  import com.powerspace.openrtb.json.util.EncodingUtils._

  override def extensionRegistry: ExtensionRegistry = ExtensionRegistry()
    .registerExtension(PowerspaceProto.bidRequest)
    .registerExtension(PowerspaceProto.contentExt)
    .registerExtension(PowerspaceProto.siteExt)
    .registerExtension(PowerspaceProto.user)
    .registerExtension(PowerspaceProto.geo)
    .registerExtension(PowerspaceProto.bid)

  override def nativeRegistry: ExtensionRegistry = ExtensionRegistry()

}