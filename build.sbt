name := "scala-openrtb"

version in ThisBuild := "0.1"

scalaVersion in ThisBuild := "2.12.6"
organization in ThisBuild := "com.powerspace.openrtb"

scalacOptions in ThisBuild := Seq(
  "-unchecked", "-feature",
  "-deprecation",
  "-encoding", "utf8",
  "-opt:l:default,l:inline,l:method",
  "-opt-inline-from:**",
  "-unchecked",
  "-Ywarn-dead-code", "-Ywarn-numeric-widen" /*, "-Ywarn-unused"*/ , "-Ywarn-unused-import",
  "-language:postfixOps", "-language:implicitConversions","-language:existentials","-language:higherKinds"
  /*,"-Ymacro-debug-lite"*/)



// plain openrtb scala model
lazy val openRtbModel = Project(id = "openrtb-model", base = file("openrtb-model"))

// json ser/deser
lazy val openRtbJson = Project(id = "openrtb-json", base = file("openrtb-json"))
  .dependsOn(openRtbModel)

// proto ser/deser
lazy val openRtbProtobuf = Project(id = "openrtb-proto", base = file("openrtb-proto"))
  .dependsOn(openRtbModel)


lazy val root = (project in file("."))
  .aggregate(
    openRtbModel,
    openRtbJson,
    openRtbProtobuf
  )
