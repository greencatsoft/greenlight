package com.greencatsoft.greenlight.matcher

import scala.collection.SeqLike

import com.greencatsoft.greenlight.TestFailureException
import com.greencatsoft.greenlight.grammar.Predicates.Empty
import com.greencatsoft.greenlight.grammar.Verb.{ Be, Contain }

object CollectionMatchers {

  trait Emptiness extends Matcher[SeqLike[_, _], Be, Empty] {

    override def matches(actual: SeqLike[_, _], expected: Empty): Unit =
      if (!actual.isEmpty)
        throw TestFailureException(s"Expected '$actual' to be empty.")

    override def notMatches(actual: SeqLike[_, _], expected: Empty): Unit =
      if (actual.isEmpty)
        throw TestFailureException(s"Expected '$actual' not to be empty.")
  }

  trait Containment extends Matcher[SeqLike[_, _], Contain, Any] {

    override def matches(actual: SeqLike[_, _], expected: Any): Unit =
      if (!actual.contains(expected))
        throw TestFailureException(s"Expected '$actual' to contain '$expected'.")

    override def notMatches(actual: SeqLike[_, _], expected: Any): Unit =
      if (actual.contains(expected))
        throw TestFailureException(s"Expected '$actual' not to contain '$expected'.")
  }
}
