# Greenlight
_Greenlight_ is a BDD style testing framework for Scala and Scala.js, which provides an extensible matcher API with fluent syntax.

## Status

The project is at a very early stage of development, so it should be used with caution.

## How to Use

### SBT Settings

Add the following lines to your ```sbt``` build definition:

```scala
libraryDependencies += "com.greencatsoft" %%% "greenlight" % "0.2"
```

And override the default testing framework like below:

```scala
testFrameworks := Seq(new TestFramework("com.greencatsoft.greenlight.Greenlight"))
```

If you want to test the latest snapshot version instead, change the version to 
```0.3-SNAPSHOT``` and add Sonatype snapshot repository to the resolver as follows: 

```scala
resolvers += 
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
```

Or more simply as,
```scala
resolvers += Resolver.sonatypeRepo("snapshots")
```

## Code Example

```scala
object FrameworkTest extends TestSuite {

  "The framework" should "be able to test equality" in {

    (1 + 1) must be (2)

    ("A" + "B") should be ("AB")

    "Scala.js" must not be ("overlooked!")
  }

  It should "be able to check if a collection is empty" in {

    List(1, 2, 3) must not be (empty)

    Seq.empty must be (empty)
  }

  It can "check if an option is defined or not" in {

    val value = Some("value")

    value must be (defined)
    value should not be (empty)

    None must be (empty)
    None should not be (defined)
  }

  It can "also check if a string value is empty" in {

    val emptyValue = ""

    emptyValue must be (defined)
    emptyValue must be (empty)

    val nullValue: String = null

    nullValue should be (empty)
    nullValue should not be (defined)

    val value = "Scala"

    value must not be (empty)
    value should be (defined)
  }

  It can "also check if a collection contains an item or not" in {

    val collection = Seq("a", "b", "c", "d")

    collection must contain ("a")
    collection must contain ("b")
    
    collection must not contain ("e")
  }

  // You can skip a test case by using 'might' instead of 'should/must/can'.
  It might "not be ready for prime time" in {
    "You" should be ("joking!")
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

object LifeCycleTest extends TestSuite with BeforeAndAfter {

  var specification: Option[String] = None

  before { (s: Statement) =>
    this.specification = Some(s.description)
  }

  after {
    this.specification = None
  }

  "BeforeAndAfter trait" can "be used to be notified before and after a specification" in {
    specification should not be (empty)
    specification foreach { it =>
      it should be ("BeforeAndAfter trait can be used to be notified before and after a specification")
    }
  }
}
```
