val http4sVersion = "0.21.0-M4"
val akkaHttpVersion = "10.1.9"
val akkaHttpJsonVersion = "1.28.0"
val monixVersion = "3.0.0"

PB.targets in Compile := Seq(
  scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value
)

PB.protoSources in Compile += target.value / "protobuf_external"
includeFilter in PB.generate := "example.proto"

libraryDependencies ++= Seq(
  "org.http4s"        %% "http4s-dsl"          % http4sVersion,
  "org.http4s"        %% "http4s-blaze-server" % http4sVersion,
  "org.http4s"        %% "http4s-blaze-client" % http4sVersion,
  "org.http4s"        %% "http4s-circe"        % http4sVersion,
  "org.http4s"        %% "http4s-twirl"        % http4sVersion,
  "com.typesafe.akka" %% "akka-http-core"      % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http"           % akkaHttpVersion,
  "de.heikoseeberger" %% "akka-http-circe"     % akkaHttpJsonVersion,
  "io.monix"          %% "monix"               % monixVersion
)
