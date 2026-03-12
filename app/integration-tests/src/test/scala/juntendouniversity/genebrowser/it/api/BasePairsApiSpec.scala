package juntendouniversity.genebrowser.it.api

import cats.effect.unsafe.implicits.global

class BasePairsApiSpec extends ApiSpecBase:
  "GET /base-pairs" should {
    "return base pairs for a chromosome" in {
      get("/base-pairs?tsn=180092&chromosomeIndex=1").map { json =>
        val arr = json.asArray.get
        arr should not be empty

        val first = arr.head.hcursor
        first.get[Long]("position").toOption shouldBe Some(100L)
        first.get[String]("leftNucleotide").toOption shouldBe Some("A")
        first.get[String]("rightNucleotide").toOption shouldBe Some("T")
      }.unsafeToFuture()
    }

    "respect the offset parameter" in {
      get("/base-pairs?tsn=180092&chromosomeIndex=1&offset=2").map { json =>
        val arr = json.asArray.get
        val positions = arr.flatMap(_.hcursor.get[Long]("position").toOption)
        positions.head shouldBe 300L
      }.unsafeToFuture()
    }
  }
