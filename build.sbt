ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

val AkkaVersion = "2.8.0"
val AkkaHTTPVersion = "10.5.0"


lazy val root = (project in file("."))
  .settings(
    name := "webserver-sample",
    libraryDependencies ++=Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHTTPVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHTTPVersion,
      "org.slf4j" % "slf4j-simple" % "2.0.8" % Test
    )
  )
