val monocleVersion = "1.5.0" // 1.5.0-cats based on cats 1.0.x

libraryDependencies ++= Seq(
  "com.google.openrtb" % "openrtb-core" % "1.5.1" % "protobuf",
  "io.monix" %% "monix" % "3.0.0-RC1",
  "com.github.julien-truffaut" %% "monocle-core" % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-law" % monocleVersion % "test",
  "com.chuusai" %% "shapeless" % "2.3.3"
)

PB.targets in Compile := Seq(
  scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value
)

PB.protoSources in Compile += target.value / "protobuf_external"
includeFilter in PB.generate := "openrtb.proto"