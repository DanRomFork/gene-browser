package juntendouniversity.genebrowser.it.api

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie.hikari.HikariTransactor
import io.circe.Json
import org.http4s.Uri
import org.http4s.circe.*
import org.http4s.ember.client.EmberClientBuilder
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import scala.compiletime.uninitialized
import scala.concurrent.ExecutionContext
import juntendouniversity.genebrowser.it.TestFixtures.*

trait ApiSpecBase extends AsyncWordSpec with Matchers with BeforeAndAfterAll:
  implicit override val executionContext: ExecutionContext = ExecutionContext.global

  protected var client: org.http4s.client.Client[IO] = uninitialized
  private var httpRelease: IO[Unit] = IO.unit

  override def beforeAll(): Unit =
    super.beforeAll()

    val (xa, dbRel) = HikariTransactor.newHikariTransactor[IO](
      "org.postgresql.Driver", jdbcUrl, pgUser, pgPass,
      ExecutionContext.global
    ).allocated.unsafeRunSync()
    cleanAndSeed(xa).unsafeRunSync()
    dbRel.unsafeRunSync()

    val (c, rel) = EmberClientBuilder.default[IO].build.allocated.unsafeRunSync()
    client = c
    httpRelease = rel

  override def afterAll(): Unit =
    httpRelease.unsafeRunSync()
    super.afterAll()

  protected def get(path: String): IO[Json] =
    client.expect[Json](Uri.unsafeFromString(s"$serviceUrl$path"))
