# Greenlight
_Greenlight_ is a BDD style testing framework for Scala and Scala.js, which provides an extensible matcher API with fluent syntax.

## Status

The project is at a very early stage of development, so it should be used with caution.

## How to Use

### SBT Settings

Add the following lines to your ```sbt``` build definition:

```scala
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "com.greencatsoft" %%% "greenlight" % "0.1-SNAPSHOT"
```

```scala
// In case of a Scala project
testFrameworks := Seq(new TestFramework("com.greencatsoft.greenlight.jvm.Greenlight"))

// In case of a Scala.js project
testFrameworks := Seq(new TestFramework("com.greencatsoft.greenlight.jvm.Greenlight"))
```

## Code Example

```scala
object FrameworkTest extends TestSuite {

  "The framework" should "be able to test equality" in {

    (1 + 1) must be (2)

    ("A" + "B") should not be ("ABC")

    "Scala.js" must not be ("overlooked!")
  }

  It should "be able to check emptiness of various types" in {

    List(1, 2, 3) must not be (empty)

    Seq.empty must be (empty)

    Some("value") should not be (empty)

    None must be (empty)

    "" must be (empty)

    (null: String) must be (empty)

    "Scala" must not be (empty)
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
  ```
