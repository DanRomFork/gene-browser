package tbd.genebrowser.domain

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AppConfigTest extends AnyFlatSpec with Matchers:

  "AppConfig" should "contain a database configuration" in {
    val dbConfig = DatabaseConfig(
      host              = "localhost",
      port              = 5432,
      name              = "testdb",
      user              = "testuser",
      password          = "testpass",
      migrationsLocations = List("db/migration")
    )

    val appConfig = AppConfig(database = dbConfig)

    appConfig.database should be(dbConfig)
    appConfig.database.host should be("localhost")
    appConfig.database.port should be(5432)
  }

  "AppConfig" should "be a case class" in {
    val dbConfig = DatabaseConfig(
      host              = "localhost",
      port              = 5432,
      name              = "testdb",
      user              = "testuser",
      password          = "testpass",
      migrationsLocations = List.empty
    )

    val config1 = AppConfig(database = dbConfig)
    val config2 = AppConfig(database = dbConfig)

    config1 should equal(config2)
    config1 should ===(config2)
  }
