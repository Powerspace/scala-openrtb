val akkaHttpVersion = "10.1.0"
val akkaHttpJsonVersion = "1.20.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core"  % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http"       % akkaHttpVersion,
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpJsonVersion
)
