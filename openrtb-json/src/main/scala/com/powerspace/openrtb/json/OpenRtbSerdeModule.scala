package com.powerspace.openrtb.json

import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry

/**
  * Provides serialization and deserialization for OpenRTB entities.
  */
object OpenRtbSerdeModule extends SerdeModule {
  override implicit def nativeRegistry = ExtensionRegistry()

  override implicit def extensionRegistry = ExtensionRegistry()

}
