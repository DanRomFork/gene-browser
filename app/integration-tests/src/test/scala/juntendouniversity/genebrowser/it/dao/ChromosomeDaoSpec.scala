package juntendouniversity.genebrowser.it.dao

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import juntendouniversity.genebrowser.dao.ChromosomeDao

class ChromosomeDaoSpec extends DaoSpecBase:
  "ChromosomeDao" should {
    "return chromosomes ordered by index for a known TSN" in {
      ChromosomeDao.make[IO](xa).findByTsn(180092).map { result =>
        result should have size 2
        result.map(_.index) shouldBe List(1, 2)
      }.unsafeToFuture()
    }

    "return an empty list for an unknown TSN" in {
      ChromosomeDao.make[IO](xa).findByTsn(999999).map { result =>
        result shouldBe empty
      }.unsafeToFuture()
    }
  }
