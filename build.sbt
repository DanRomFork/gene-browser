import sbt._

ThisBuild / organization := "tbd"
ThisBuild / scalaVersion := "3.7.4"
ThisBuild / version := "0.1.0"

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
