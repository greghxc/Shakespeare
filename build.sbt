enablePlugins(JavaAppPackaging)

name := "google-places-quote-service"
organization := "com.gregmertzlufft"
version := "1.0"
scalaVersion := "2.11.8"
maintainer := "greg@bikebloc.com"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV       = "2.4.16"
  val akkaHttpV   = "10.0.3"
  val scalaTestV  = "3.0.1"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "org.scalatest"     %% "scalatest" % scalaTestV % "test",
    "ch.megard"         %% "akka-http-cors" % "0.2.1",
    "org.mockito" % "mockito-core" % "1.8.5" % "test",
    "org.slf4j" % "slf4j-api" % "1.7.5",
    "org.slf4j" % "slf4j-simple" % "1.7.5",
    "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.9.1",
    "com.google.maps"   % "google-maps-services" % "0.2.3",
    "com.github.javafaker" % "javafaker" % "0.13"
  )
}
enablePlugins(JavaAppPackaging)

Revolver.settings
