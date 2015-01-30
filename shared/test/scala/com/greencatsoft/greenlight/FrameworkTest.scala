package com.greencatsoft.greenlight

object FrameworkTest extends TestSuite {

  "Scala" should "be cool" in {

    (1 + 1) must be (2)

    ("A" + "B") must be("AB")
  }

  it must "be powerful to use" in {
    "Javascript" should not be ("better.")
  }
}