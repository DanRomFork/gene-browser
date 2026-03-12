import sbt._

ThisBuild / organization := "tbd"
ThisBuild / scalaVersion := "3.7.4"
ThisBuild / version := "0.1.0"

val modulesFolder = "app"

val http4sVersion = "0.23.30"
val circeVersion  = "0.14.10"

val commonSettings = Seq(
  fork := true,
  packageDoc / publishArtifact := false
)

lazy val root = project
  .in(file("."))
  .settings(name := "gene-browser")
  .settings(commonSettings)
  .aggregate(modules)

lazy val modules = project
  .in(file(modulesFolder))
  .settings(commonSettings)
  .aggregate(`gene-browser-service`)

lazy val `gene-browser-service` = project
  .in(file(s"$modulesFolder/service"))
  .settings(name := "gene-browser-service")
  .settings(commonSettings)
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.5.4",
      "org.tpolecat"  %% "doobie-core" % "1.0.0-RC5",
      "org.tpolecat"  %% "doobie-hikari" % "1.0.0-RC5",
      "org.tpolecat"  %% "doobie-postgres" % "1.0.0-RC5",
      "org.flywaydb"   % "flyway-core" % "10.10.0",
      "org.flywaydb"   % "flyway-database-postgresql" % "10.10.0",
      "com.typesafe"   % "config" % "1.4.3",
      "com.github.pureconfig" %% "pureconfig-core" % "0.17.6",
      "com.github.pureconfig" %% "pureconfig-cats-effect" % "0.17.6",
      "org.http4s"    %% "http4s-dsl" % http4sVersion,
      "org.http4s"    %% "http4s-ember-server" % http4sVersion,
      "org.http4s"    %% "http4s-client" % http4sVersion,
      "org.http4s"    %% "http4s-circe" % http4sVersion,
      "io.circe"      %% "circe-core" % circeVersion,
      "io.circe"      %% "circe-generic" % circeVersion,
      "io.circe"      %% "circe-parser" % circeVersion,
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    ),
    Compile / guardrailTasks := List(
      ScalaServer(
        file(s"$modulesFolder/service/src/main/openapi/api.yaml"),
        pkg = "tbd.genebrowser.api",
        framework = "http4s"
      )
    ),
    Compile / mainClass := Some("tbd.genebrowser.Main"),
    Docker / packageName := "gene-browser",
    Docker / version := version.value,
    dockerBaseImage := "eclipse-temurin:17-jre",
    dockerExposedPorts := Seq(8080),
    dockerRepository := {
      val registry = sys.env.getOrElse("DOCKER_REGISTRY", "ghcr.io")
      val repo = sys.env.getOrElse("GITHUB_REPOSITORY", "")
      if (repo.isEmpty) None else Some(s"$registry/$repo".toLowerCase)
    },
    dockerUpdateLatest := true
  )
