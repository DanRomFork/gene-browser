package juntendouniversity.genebrowser.it.api

import cats.effect.unsafe.implicits.global

class GenesApiSpec extends ApiSpecBase:
  "GET /genes" should {
    "return all genes within the position range" in {
      get("/genes?tsn=180092&chromosomeIndex=1&startPosition=100&stopPosition=500").map { json =>
        json.asArray.get should have size 3
      }.unsafeToFuture()
    }

    "filter to genes fully contained in the range" in {
      get("/genes?tsn=180092&chromosomeIndex=1&startPosition=200&stopPosition=400").map { json =>
        val arr = json.asArray.get
        arr should have size 1

        val gene = arr.head.hcursor
        gene.get[Long]("startPosition").toOption shouldBe Some(200L)
        gene.get[Long]("stopPosition").toOption shouldBe Some(400L)
      }.unsafeToFuture()
    }

    "return an empty array for a TSN with no genes" in {
      get("/genes?tsn=180366&chromosomeIndex=1&startPosition=0&stopPosition=1000").map { json =>
        json.asArray.get shouldBe empty
      }.unsafeToFuture()
    }
  }
