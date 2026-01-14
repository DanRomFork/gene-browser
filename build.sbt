import sbt._

ThisBuild / organization := "tbd"
ThisBuild / scalaVersion := "3.7.4"
ThisBuild / version := "0.1.0"

enablePlugins(JavaAppPackaging, DockerPlugin)

dockerBaseImage := "eclipse-temurin:17-jre"
dockerExposedPorts := Seq(8080)
Docker / packageName := "gene-browser"
Docker / version := version.value
// Use environment variable, fallback to empty (will be set at build time)
dockerRepository := {
  val registry = sys.env.getOrElse("DOCKER_REGISTRY", "ghcr.io")
  val repo = sys.env.getOrElse("GITHUB_REPOSITORY", "")
  if (repo.isEmpty) None else Some(s"$registry/$repo".toLowerCase)
}

dockerUpdateLatest := true

val modulesFolder = "app"

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
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.5.4",
      "org.tpolecat" %% "doobie-core" % "1.0.0-RC5",
      "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC5",
      "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC5",
      "org.flywaydb" % "flyway-core" % "10.10.0",
      "org.flywaydb" % "flyway-database-postgresql" % "10.10.0",
      "com.typesafe" % "config" % "1.4.3",
      "org.typelevel" %% "cats-effect" % "3.5.4",
      "com.github.pureconfig" %% "pureconfig-core" % "0.17.6",
      "com.github.pureconfig" %% "pureconfig-cats-effect" % "0.17.6",
      "com.typesafe" % "config" % "1.4.3"
    ),
    Compile / mainClass := Some("Main")
  )
