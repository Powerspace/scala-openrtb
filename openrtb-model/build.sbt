libraryDependencies ++= Seq(
  "com.google.openrtb" % "openrtb-core" % "1.5.1" % "protobuf",
  "io.monix" %% "monix" % "3.0.0-RC1"
)

PB.targets in Compile := Seq(
  scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value
)

PB.protoSources in Compile += target.value / "protobuf_external"
includeFilter in PB.generate := "openrtb.proto"