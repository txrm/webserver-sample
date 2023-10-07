ThisBuild / version := "0.1.1-SNAPSHOT"

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
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      "ch.qos.logback" % "logback-classic" % "1.4.7"
    ),
    assembly / assemblyJarName := "webserver-sample.jar",
    assembly / assemblyMergeStrategy := {
      case PathList("module-info.class") => MergeStrategy.discard
      case x if x.endsWith("/module-info.class") => MergeStrategy.discard
      case x =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    }
  )