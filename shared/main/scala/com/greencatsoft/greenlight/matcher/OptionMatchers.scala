package com.greencatsoft.greenlight.matcher

import com.greencatsoft.greenlight.TestFailureException
import com.greencatsoft.greenlight.grammar.Predicates.{ Defined, Empty }
import com.greencatsoft.greenlight.grammar.Verb.Be

object OptionMatchers {

  trait Emptiness extends Matcher[Option[_], Be, Empty] {

    override def matches(actual: Option[_], expected: Empty): Unit =
      if (!actual.isEmpty)
        throw TestFailureException(s"Expected '$actual' to be empty.")

    override def notMatches(actual: Option[_], expected: Empty): Unit =
      if (actual.isEmpty)
        throw TestFailureException(s"Expected '$actual' not to be empty.")
  }

  trait Definition extends Matcher[Option[_], Be, Defined] {

    override def matches(actual: Option[_], expected: Defined): Unit =
      if (!actual.isDefined)
        throw TestFailureException(s"Expected '$actual' to be defined.")

    override def notMatches(actual: Option[_], expected: Defined): Unit =
      if (actual.isDefined)
        throw TestFailureException(s"Expected '$actual' not to be defined.")
  }
}
