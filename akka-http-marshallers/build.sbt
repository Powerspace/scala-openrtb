val akkaVersion = "2.5.25"
val akkaHttpVersion = "10.1.9"
val akkaHttpJsonVersion = "1.28.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core"  % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http"       % akkaHttpVersion,
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpJsonVersion
)
