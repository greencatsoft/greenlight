package com.greencatsoft.greenlight

object FrameworkTest extends TestSuite {

  "The framework" should "be able to test equality" in {

    (1 + 1) must be(2)

    ("A" + "B") should be ("AB")

    "Scala.js" should not be ("overlooked.")
  }

  It should "be able to check emptiness" in {

    Seq.empty must be (empty)

    List(1, 2, 3) must not be (empty)
  }

  It can "be used to test if an exception is thrown" in {

    A_[NullPointerException] should be_thrown_in {
      val value = null
      value.toString
    }

    A_[NullPointerException] should not be_thrown_in {
      println("Scala.js rocks!")
    }
  }
}