package juntendouniversity.genebrowser.it.dao

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import juntendouniversity.genebrowser.dao.SpeciesDao

class SpeciesDaoSpec extends DaoSpecBase:
  "SpeciesDao" should {
    "return all seeded species" in {
      SpeciesDao.make[IO](xa).findAll.map { result =>
        result.map(_.scientificName) should contain theSameElementsAs List("Homo sapiens", "Mus musculus")
        result.map(_.tsn) should contain theSameElementsAs List(180092, 180366)
      }.unsafeToFuture()
    }
  }
