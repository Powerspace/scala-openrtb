import sbt.url

sonatypeProfileName in ThisBuild := "com.powerspace"

publishMavenStyle in ThisBuild := true

licenses in ThisBuild := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage in ThisBuild := Some(url("https://github.com/Powerspace/scala-openrtb"))

scmInfo in ThisBuild := Some(
  ScmInfo(
    url("https://github.com/Powerspace/scala-openrtb"),
    "scm:git@github.com:Powerspace/scala-openrtb.git"
  )
)

developers in ThisBuild := List(
  Developer(
    id = "waiter-melon",
    name = "Emanuele Pirro",
    email = "pirroemanuele@gmail.com",
    url = url("https://github.com/waiter-melon")),
  Developer(
    id = "rlebran",
    name = "Romain Lebran",
    email = "rlebran@gmail.com",
    url = url("https://github.com/rlebran")),
  Developer(
    id = "valdo404",
    name = "Laurent Valdes",
    email = "valderama@gmail.com",
    url = url("https://github.com/valdo404")),
  Developer(
    id = "Garnek20",
    name = "Pawel Gontarz",
    email = "garnek522@gmail.com",
    url = url("https://github.com/Garnek20"))
)

pomIncludeRepository in ThisBuild := { _ =>
  false
}
publishTo in ThisBuild := sonatypePublishToBundle.value
publishMavenStyle in ThisBuild := true
