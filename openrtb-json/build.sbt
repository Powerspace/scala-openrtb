val circeVersion = "0.13.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-generic-extras"
).map(_ % circeVersion)

libraryDependencies += "org.scalactic" %% "scalactic"     % "3.0.8" % "test"
libraryDependencies += "org.scalatest" %% "scalatest"     % "3.0.8" % "test"
libraryDependencies += "org.gnieh"     %% "diffson-circe" % "4.0.3" % "test"
