name := "scala-openrtb"

version in ThisBuild := "0.1"

scalaVersion in ThisBuild := "2.12.6"
organization in ThisBuild := "com.powerspace.openrtb"


lazy val openRtbModel = Project(id = "openrtb-model", base = file("openrtb-model")) // plain openrtb scala model

lazy val openRtbJson = Project(id = "openrtb-json", base = file("openrtb-json")) // json ser/deser

lazy val openRtbProtobuf = Project(id = "openrtb-proto", base = file("openrtb-proto")) // proto ser/deser

lazy val root = (project in file("."))
  .aggregate(
    openRtbModel,
    openRtbJson,
    openRtbProtobuf
  )
