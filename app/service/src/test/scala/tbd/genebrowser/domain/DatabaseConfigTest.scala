package tbd.genebrowser.domain

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DatabaseConfigTest extends AnyFlatSpec with Matchers:

  "DatabaseConfig" should "have all required fields" in {
    val config = DatabaseConfig(
      host              = "localhost",
      port              = 5432,
      name              = "testdb",
      user              = "testuser",
      password          = "testpass",
      migrationsLocations = List("db/migration")
    )

    config.host should be("localhost")
    config.port should be(5432)
    config.name should be("testdb")
    config.user should be("testuser")
    config.password should be("testpass")
    config.migrationsLocations should have size 1
    config.migrationsLocations should contain("db/migration")
  }

  "DatabaseConfig" should "handle multiple migration locations" in {
    val config = DatabaseConfig(
      host              = "localhost",
      port              = 5432,
      name              = "testdb",
      user              = "testuser",
      password          = "testpass",
      migrationsLocations = List("db/migration", "migration")
    )

    config.migrationsLocations should have size 2
    config.migrationsLocations should contain allOf ("db/migration", "migration")
  }

  "DatabaseConfig" should "have valid port number" in {
    val config = DatabaseConfig(
      host              = "localhost",
      port              = 5432,
      name              = "testdb",
      user              = "testuser",
      password          = "testpass",
      migrationsLocations = List.empty
    )

    config.port should be > 0
    config.port should be < 65536
  }
