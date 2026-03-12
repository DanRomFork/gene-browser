addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.16")
addSbtPlugin("dev.guardrail" % "sbt-guardrail" % "0.75.2")
libraryDependencies += "dev.guardrail" %% "guardrail-scala-http4s" % "0.76.0"
