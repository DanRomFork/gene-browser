package juntendouniversity.genebrowser

import cats.effect.{ IO, IOApp }
import com.typesafe.config.ConfigFactory
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Server
import juntendouniversity.genebrowser.api.{ Resource => ApiResource }
import juntendouniversity.genebrowser.dao.*
import juntendouniversity.genebrowser.domain.{ AppConfig, DatabaseConfig }
import juntendouniversity.genebrowser.handler.GeneBrowserHandlerImpl

object Main extends IOApp.Simple:
  def run: IO[Unit] =
    for {
      rawConfig <- IO.blocking(ConfigFactory.load())
      appConfig <- AppConfig.load[IO](rawConfig)
      _         <- runMigrations(appConfig.database)
      _         <- createServer(appConfig).useForever
    } yield ()

  private def createServer(config: AppConfig): cats.effect.Resource[IO, Server] =
    for {
      xa <- HikariTransactor.newHikariTransactor[IO](
        "org.postgresql.Driver",
        s"jdbc:postgresql://${config.database.host}:${config.database.port}/${config.database.name}",
        config.database.user,
        config.database.password,
        scala.concurrent.ExecutionContext.global
      )
      handler = GeneBrowserHandlerImpl[IO](
        SpeciesDao.make[IO](xa),
        ChromosomeDao.make[IO](xa),
        BasePairDao.make[IO](xa, config.pagination.basePairsWindowSize),
        GeneDao.make[IO](xa)
      )
      routes = new ApiResource[IO]().routes(handler)
      server <- BlazeServerBuilder[IO]
        .bindHttp(config.http.port.value, config.http.host.toString)
        .withHttpApp(routes.orNotFound)
        .resource
    } yield server

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
