name := "scala-openrtb"

version in ThisBuild := "1.4.0"

scalaVersion in ThisBuild       := "2.13.1"
crossScalaVersions in ThisBuild := Seq("2.12.10", "2.13.1")
organization in ThisBuild       := "com.powerspace.openrtb"
organizationName in ThisBuild   := "Powerspace"
organizationHomepage            := Some(url("https://powerspace.com/"))

scalacOptions in ThisBuild := Seq(
  "-unchecked",
  "-feature",
  "-deprecation",
  "-encoding",
  "utf8",
  "-opt:l:default,l:inline,l:method",
  "-unchecked",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-language:existentials",
  "-language:higherKinds"
)

publishArtifact in root := false

val testSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.0.8" % "test",
    "org.scalatest" %% "scalatest" % "3.0.8" % "test",
    "org.scalamock" %% "scalamock" % "4.4.0" % "test")
)

// OpenRTB Scala model
lazy val openRtbModel = Project(id = "openrtb-model", base = file("openrtb-model"))
  .settings(testSettings: _*)

// OpenRTB JSON Serialization & Deserialization
lazy val openRtbJson = Project(id = "openrtb-json", base = file("openrtb-json"))
  .dependsOn(openRtbModel)

// BidSwitch Scala model
lazy val bidswitchModel = Project(id = "bidswitch-model", base = file("bidswitch-model"))
  .dependsOn(openRtbModel % "compile->compile;test->test")

// BidSwitch JSON Serialization & Deserialization
lazy val bidswitchJson = Project(id = "bidswitch-json", base = file("bidswitch-json"))
  .dependsOn(bidswitchModel % "compile->compile;test->test", openRtbJson % "compile->compile;test->test")
  .settings(testSettings: _*)

// Akka Http marshallers and unmarshallers
lazy val akkaHttpMarshaller = Project(id = "akka-http-marshallers", base = file("akka-http-marshallers"))
  .dependsOn(openRtbJson)

// scala-openrtb examples
lazy val examples = Project(id = "examples", base = file("examples"))
  .dependsOn(openRtbJson % "compile->compile;test->test")
  .settings(skip in publish := true)

lazy val benchmarks = Project(id = "benchmarks", base = file("benchmarks"))
  .enablePlugins(JmhPlugin)
  .dependsOn(
    openRtbJson,
    examples
  )
  .settings(skip in publish := true)

lazy val root = (project in file("."))
  .aggregate(
    openRtbModel,
    openRtbJson,
    bidswitchModel,
    bidswitchJson,
    akkaHttpMarshaller,
    examples,
    benchmarks
  )
