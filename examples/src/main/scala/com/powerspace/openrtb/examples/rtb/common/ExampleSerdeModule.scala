package com.powerspace.openrtb.examples.rtb.common

import com.powerspace.openrtb.example.ExampleProto
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.{OpenRtbExtensions, SerdeModule}

object ExampleSerdeModule extends SerdeModule {
  override def nativeRegistry: OpenRtbExtensions.ExtensionRegistry = ExtensionRegistry()

  override def extensionRegistry: OpenRtbExtensions.ExtensionRegistry = ExtensionRegistry()
    .registerExtension(ExampleProto.impExt)
    .registerExtension(ExampleProto.bidResponseExt)
}
