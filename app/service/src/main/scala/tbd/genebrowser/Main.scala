package tbd.genebrowser

import cats.effect.{ IO, IOApp }
import com.typesafe.config.ConfigFactory
import tbd.genebrowser.domain.{ AppConfig, DatabaseConfig }
import org.flywaydb.core.Flyway

object Main extends IOApp.Simple:
  def run: IO[Unit] =
    for {
      rawConfig <- IO.blocking(ConfigFactory.load())
      appConfig <- AppConfig.load[IO](rawConfig)
      _         <- runMigrations(appConfig.database)
      _         <- IO.println("Hello, World!")
    } yield ()

  private def runMigrations(dbConfig: DatabaseConfig): IO[Unit] = IO.blocking {
    val url       = s"jdbc:postgresql://${dbConfig.host}:${dbConfig.port}/${dbConfig.name}"
    val locations = dbConfig.migrationsLocations.map(loc => s"classpath:$loc")

    val flyway = Flyway
      .configure()
      .dataSource(url, dbConfig.user, dbConfig.password)
      .locations(locations*)
      .load()

    flyway.migrate()
  }
