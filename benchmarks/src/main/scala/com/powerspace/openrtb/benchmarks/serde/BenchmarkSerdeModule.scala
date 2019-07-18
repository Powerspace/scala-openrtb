package com.powerspace.openrtb.benchmarks.serde

import com.powerspace.openrtb.example.ExampleProto
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.{OpenRtbExtensions, SerdeModule}

class BenchmarkSerdeModule extends SerdeModule {
  override def nativeRegistry: OpenRtbExtensions.ExtensionRegistry = ExtensionRegistry()

  override def extensionRegistry: OpenRtbExtensions.ExtensionRegistry =
    ExtensionRegistry()
      .registerExtension(ExampleProto.impExt)
}
