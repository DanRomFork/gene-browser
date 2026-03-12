package juntendouniversity.genebrowser.domain

import com.comcast.ip4s.{ Host, Port }
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AppConfigTest extends AnyFlatSpec with Matchers:

  private val dbConfig = DatabaseConfig(
    host                = "localhost",
    port                = 5432,
    name                = "testdb",
    user                = "testuser",
    password            = "testpass",
    migrationsLocations = List("db/migration")
  )

  private val httpConfig = HttpConfig(
    host = Host.fromString("0.0.0.0").get,
    port = Port.fromInt(8080).get
  )

  private val paginationConfig = PaginationConfig(basePairsWindowSize = 100000L)

  "AppConfig" `should` "contain a database configuration" in {
    val appConfig = AppConfig(database = dbConfig, http = httpConfig, pagination = paginationConfig)

    appConfig.database `should` be(dbConfig)
    appConfig.database.host `should` be("localhost")
    appConfig.database.port `should` be(5432)
  }

  "AppConfig" `should` "be a case class" in {
    val config1 = AppConfig(database = dbConfig, http = httpConfig, pagination = paginationConfig)
    val config2 = AppConfig(database = dbConfig, http = httpConfig, pagination = paginationConfig)

    config1 `should` equal(config2)
    config1 `should` ===(config2)
  }
