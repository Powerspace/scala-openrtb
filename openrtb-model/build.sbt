val monocleVersion = "2.0.0"

libraryDependencies ++= Seq(
  "com.google.openrtb"         % "openrtb-core"   % "1.5.7" % "protobuf",
  "com.github.julien-truffaut" %% "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-law"   % monocleVersion % "test",
  "com.chuusai"                %% "shapeless"     % "2.3.3",
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"
)

PB.targets in Compile := Seq(
  scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value
)

PB.protoSources in Compile += target.value / "protobuf_external"
includeFilter in PB.generate := "openrtb.proto"
