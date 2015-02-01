# Greenlight
_Greenlight_ is a BDD style testing framework for Scala and Scala.js, which provides an extensible matcher API with fluent syntax.

## Status

The project is at a very early stage of development, so it should be used with caution.

## How to Use

```scala
object FrameworkTest extends TestSuite {

  "The framework" should "be able to test equality" in {

    (1 + 1) must be (2)

    ("A" + "B") must be ("AB")

    "Scala.js" should not be ("overlooked.")
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
  ```
