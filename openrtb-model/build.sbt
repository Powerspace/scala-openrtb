libraryDependencies ++= Seq(
  "com.google.openrtb" % "openrtb-core" % "1.5.1" % "protobuf"
)

PB.targets in Compile := Seq(
  scalapb.gen(flatPackage = true, grpc = true) -> (sourceManaged in Compile).value
)

PB.protoSources in Compile += target.value / "protobuf_external"
includeFilter in PB.generate := "openrtb.proto"