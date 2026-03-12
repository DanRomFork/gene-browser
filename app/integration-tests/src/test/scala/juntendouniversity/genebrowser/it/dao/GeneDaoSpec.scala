package juntendouniversity.genebrowser.it.dao

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import juntendouniversity.genebrowser.dao.GeneDao

class GeneDaoSpec extends DaoSpecBase:
  "GeneDao" should {
    "return all genes fully within the position range" in {
      GeneDao.make[IO](xa).findByRegion(180092, chromosomeIndex = 1, startPosition = 100, stopPosition = 500).map { result =>
        result should have size 3
        result.map(r => (r.startPosition, r.stopPosition)) shouldBe List((100, 300), (200, 400), (300, 500))
      }.unsafeToFuture()
    }

    "exclude genes outside the range" in {
      GeneDao.make[IO](xa).findByRegion(180092, chromosomeIndex = 1, startPosition = 200, stopPosition = 400).map { result =>
        result should have size 1
        result.head.startPosition shouldBe 200
        result.head.stopPosition shouldBe 400
      }.unsafeToFuture()
    }

    "return an empty list for a TSN with no genes" in {
      GeneDao.make[IO](xa).findByRegion(180366, chromosomeIndex = 1, startPosition = 0, stopPosition = 1000).map { result =>
        result shouldBe empty
      }.unsafeToFuture()
    }
  }
