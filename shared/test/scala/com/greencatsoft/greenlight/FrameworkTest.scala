package com.greencatsoft.greenlight

object FrameworkTest extends TestSuite {

  "The framework" should "be able to test equality" in {

    (1 + 1) must be (2)

    ("A" + "B") should be ("AB")

    "Scala.js" must not be ("overlooked!")
  }

  It should "be able to check emptiness of collections and options" in {

    List(1, 2, 3) must not be (empty)

    Seq.empty must be (empty)

    Some("value") should not be (empty)

    None must be (empty)
  }

  It can "be used to test if an exception is thrown" in {

    A_[NullPointerException] should be_thrown_in {
      (null).toString
    }

    A_[NullPointerException] should not be_thrown_in {
      println("Scala.js rocks!")
    }
  }
}