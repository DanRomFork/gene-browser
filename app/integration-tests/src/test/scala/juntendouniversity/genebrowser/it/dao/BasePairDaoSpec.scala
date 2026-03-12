package juntendouniversity.genebrowser.it.dao

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import juntendouniversity.genebrowser.dao.BasePairDao

class BasePairDaoSpec extends DaoSpecBase:
  "BasePairDao" should {
    "return base pairs ordered by position" in {
      BasePairDao.make[IO](xa, windowSize = 3).findByChromosome(180092, chromosomeIndex = 1, offset = 0).map { result =>
        result should have size 3
        result.map(_.position) shouldBe List(100, 200, 300)
        result.head.leftNucleotide shouldBe "A"
        result.head.rightNucleotide shouldBe "T"
      }.unsafeToFuture()
    }

    "respect the offset parameter" in {
      BasePairDao.make[IO](xa, windowSize = 3).findByChromosome(180092, chromosomeIndex = 1, offset = 2).map { result =>
        result should have size 3
        result.map(_.position) shouldBe List(300, 400, 500)
      }.unsafeToFuture()
    }

    "return an empty list for an unknown chromosome index" in {
      BasePairDao.make[IO](xa, windowSize = 3).findByChromosome(180092, chromosomeIndex = 99, offset = 0).map { result =>
        result shouldBe empty
      }.unsafeToFuture()
    }
  }
