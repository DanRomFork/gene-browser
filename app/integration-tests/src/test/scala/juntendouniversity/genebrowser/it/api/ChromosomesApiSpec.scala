package juntendouniversity.genebrowser.it.api

import cats.effect.unsafe.implicits.global

class ChromosomesApiSpec extends ApiSpecBase:
  "GET /chromosomes" should {
    "return chromosomes for a known TSN" in {
      get("/chromosomes?tsn=180092").map { json =>
        val arr = json.asArray.get
        arr should have size 2

        val indices = arr.flatMap(_.hcursor.get[Int]("index").toOption)
        indices shouldBe Vector(1, 2)
      }.unsafeToFuture()
    }

    "return an empty array for an unknown TSN" in {
      get("/chromosomes?tsn=999999").map { json =>
        json.asArray.get shouldBe empty
      }.unsafeToFuture()
    }
  }
