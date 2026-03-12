package juntendouniversity.genebrowser.it.dao

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie.Transactor
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import doobie.implicits.*
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import scala.compiletime.uninitialized
import scala.concurrent.ExecutionContext
import juntendouniversity.genebrowser.it.TestFixtures.*

trait DaoSpecBase extends AsyncWordSpec with Matchers with BeforeAndAfterAll with BeforeAndAfterEach:
  implicit override val executionContext: ExecutionContext = ExecutionContext.global

  protected var xa: Transactor[IO] = uninitialized
  private var release: IO[Unit] = IO.unit

  override def beforeAll(): Unit =
    super.beforeAll()
    val (transactor, rel) = HikariTransactor.newHikariTransactor[IO](
      "org.postgresql.Driver", jdbcUrl, pgUser, pgPass,
      ExecutionContext.global
    ).allocated.unsafeRunSync()
    xa = transactor
    release = rel

    Flyway.configure()
      .dataSource(jdbcUrl, pgUser, pgPass)
      .locations("classpath:db/migration")
      .load()
      .migrate()

  override def beforeEach(): Unit =
    super.beforeEach()
    cleanAndSeed(xa).unsafeRunSync()

  override def afterEach(): Unit =
    sql"TRUNCATE genes, base_pairs, chromosomes, species RESTART IDENTITY CASCADE"
      .update.run.transact(xa).unsafeRunSync()
    super.afterEach()

  override def afterAll(): Unit =
    release.unsafeRunSync()
    super.afterAll()
