package tbd.genebrowser

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DummyTest extends AnyFlatSpec with Matchers:

  "A simple test" should "pass with basic assertions" in {
    val result = 2 + 2
    result should be(4)
    result shouldEqual 4
    result should ===(4)
  }

  "String matchers" should "work correctly" in {
    val text = "Hello, World!"
    text should startWith("Hello")
    text should endWith("!")
    text should include("World")
    text should have length 13
  }

  "Collection matchers" should "work correctly" in {
    val list = List(1, 2, 3, 4, 5)
    list should have size 5
    list should contain(3)
    list should not contain 10
    list shouldBe sorted
  }

  "Option matchers" should "work correctly" in {
    val someValue = Some(42)
    val noneValue = None

    someValue should be(defined)
    someValue.value should be(42)
    noneValue should be(empty)
  }

  "Boolean matchers" should "work correctly" in {
    val isTrue  = true
    val isFalse = false

    isTrue should be(true)
    isFalse should be(false)
    isTrue shouldBe true
  }

  "Numeric matchers" should "work correctly" in {
    val number = 10
    number should be > 5
    number should be < 20
    number should be >= 10
    number should be <= 10
  }
