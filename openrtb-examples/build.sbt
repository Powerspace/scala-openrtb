val akkaHttpVersion = "10.1.0"
val akkaHttpJsonVersion = "1.20.0"
val scalaOpenRtbVersion = "1.1.3"

PB.targets in Compile := Seq(
  scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value
)

PB.protoSources in Compile += target.value / "protobuf_external"
includeFilter in PB.generate := "bidswitch.proto"
includeFilter in PB.generate := "example.proto"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpJsonVersion,
  "org.typelevel" %% "cats-effect" % "1.0.0",
  "org.typelevel" %% "cats-core" % "1.1.0",
  "org.typelevel" %% "cats-laws" % "1.1.0",
  "io.monix" %% "monix-catnap" % "3.0.0-RC2",
  "io.monix" %% "monix-execution" % "3.0.0-RC2",
  "io.monix" %% "monix-eval" % "3.0.0-RC2",
  "io.monix" %% "monix-reactive" % "3.0.0-RC2"
)