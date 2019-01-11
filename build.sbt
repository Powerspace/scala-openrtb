name := "scala-openrtb"

version in ThisBuild := "1.1.3"

scalaVersion in ThisBuild := "2.12.6"
organization in ThisBuild := "com.powerspace.openrtb"

scalacOptions in ThisBuild := Seq(
  "-unchecked", "-feature",
  "-deprecation",
  "-encoding", "utf8",
  "-opt:l:default,l:inline,l:method",
  "-opt-inline-from:**",
  "-unchecked",
  "-Ywarn-dead-code", "-Ywarn-numeric-widen", "-Ywarn-unused-import",
  "-language:postfixOps", "-language:implicitConversions", "-language:existentials", "-language:higherKinds"
)

val testDependencies = Seq(libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.1" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % "test"))

// OpenRTB Scala model
lazy val openRtbModel = Project(id = "openrtb-model", base = file("openrtb-model"))
  .settings(testDependencies: _*)

// OpenRTB JSON Serialization & Deserialization
lazy val openRtbJson = Project(id = "openrtb-json", base = file("openrtb-json"))
  .dependsOn(openRtbModel)

// BidSwitch Scala model
lazy val bidswitchModel = Project(id = "bidswitch-model", base = file("bidswitch-model"))
  .dependsOn(openRtbModel % "compile->compile;test->test")

// BidSwitch JSON Serialization & Deserialization
lazy val bidswitchJson = Project(id = "bidswitch-json", base = file("bidswitch-json"))
  .dependsOn(bidswitchModel % "compile->compile;test->test", openRtbJson % "compile->compile;test->test")
  .settings(testDependencies: _*)

// examples
lazy val examples = Project(id = "openrtb-examples", base = file("openrtb-examples"))
  .dependsOn(openRtbJson % "compile->compile;test->test")
  .settings(skip in publish := true)

lazy val common = Project(id = "common", base = file("common"))
  .dependsOn(
    openRtbJson % "compile->compile;test->test",
    bidswitchModel % "compile->compile;test->test",
    bidswitchJson % "compile->compile;test->test")
  .settings(testDependencies: _*)

lazy val root = (project in file("."))
  .aggregate(
    openRtbModel,
    openRtbJson,
    bidswitchModel,
    bidswitchJson,
    common,
    examples
  )
