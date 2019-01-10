import sbt.Keys.publishMavenStyle

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

publishArtifact in root := false

val publishSettings = Seq(
  publishTo := Some("Sonatype Nexus Repository Manager" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"),
  sonatypeProfileName := "com.powerspace",
  publishMavenStyle := true,
  licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),

  homepage := Some(url("https://github.com/Powerspace/scala-openrtb")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/Powerspace/scala-openrtb"),
      "https://github.com/Powerspace/scala-openrtb.git"
    )
  ),
  developers := List(
    Developer(id = "waiter-melon", name = "Emanuele Pirro", email = "pirroemanuele@gmail.com", url = url("https://github.com/waiter-melon")),
    Developer(id = "rlebran", name = "Romain Lebran", email = "rlebran@gmail.com", url = url("https://github.com/rlebran")),
    Developer(id = "valdo404", name = "Laurent Valdes", email = "valderama@gmail.com", url = url("https://github.com/valdo404")),
    Developer(id = "Garnek20", name = "Pawel Gontarz", email = "garnek522@gmail.com", url = url("https://github.com/Garnek20"))
  )
)

val testDependencies = Seq(libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.1" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % "test"))

// OpenRTB Scala model
lazy val openRtbModel = Project(id = "openrtb-model", base = file("openrtb-model"))
  .settings(testDependencies: _*)
  .settings(publishSettings: _*)

// OpenRTB JSON Serialization & Deserialization
lazy val openRtbJson = Project(id = "openrtb-json", base = file("openrtb-json"))
  .dependsOn(openRtbModel)
  .settings(publishSettings: _*)

// BidSwitch Scala model
lazy val bidswitchModel = Project(id = "bidswitch-model", base = file("bidswitch-model"))
  .dependsOn(openRtbModel % "compile->compile;test->test")
  .settings(publishSettings: _*)

// BidSwitch JSON Serialization & Deserialization
lazy val bidswitchJson = Project(id = "bidswitch-json", base = file("bidswitch-json"))
  .dependsOn(bidswitchModel % "compile->compile;test->test", openRtbJson % "compile->compile;test->test")
  .settings(testDependencies: _*)
  .settings(publishSettings: _*)

lazy val common = Project(id = "common", base = file("common"))
  .dependsOn(
    openRtbJson % "compile->compile;test->test",
    bidswitchModel % "compile->compile;test->test",
    bidswitchJson % "compile->compile;test->test")
  .settings(testDependencies: _*)
  .settings(publishSettings: _*)

lazy val root = (project in file("."))
  .aggregate(
    openRtbModel,
    openRtbJson,
    bidswitchModel,
    bidswitchJson,
    common
  )
//  .settings(publishSettings: _*)
  .settings(publish := {})
