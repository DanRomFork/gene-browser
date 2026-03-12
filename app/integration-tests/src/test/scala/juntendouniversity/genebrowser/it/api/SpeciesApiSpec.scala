package juntendouniversity.genebrowser.it.api

import cats.effect.unsafe.implicits.global

class SpeciesApiSpec extends ApiSpecBase:
  "GET /species" should {
    "return all species with their scientific names and TSNs" in {
      get("/species").map { json =>
        val arr = json.asArray.get
        arr should have size 2

        val names = arr.flatMap(_.hcursor.get[String]("scientificName").toOption)
        names should contain theSameElementsAs Vector("Homo sapiens", "Mus musculus")

        val tsns = arr.flatMap(_.hcursor.get[Int]("tsn").toOption)
        tsns should contain theSameElementsAs Vector(180092, 180366)
      }.unsafeToFuture()
    }
  }
