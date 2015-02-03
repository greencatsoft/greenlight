package com.greencatsoft.greenlight.matcher

import com.greencatsoft.greenlight.TestFailureException
import com.greencatsoft.greenlight.grammar.Predicates.Empty
import com.greencatsoft.greenlight.grammar.Verb.Be

trait OptionMatcher extends Matcher[Option[_], Be, Empty] {

  override def matches(actual: Option[_], expected: Empty): Unit =
    if (!actual.isEmpty)
      throw TestFailureException(s"Expected '$actual' to be empty.")

  override def notMatches(actual: Option[_], expected: Empty): Unit =
    if (actual.isEmpty)
      throw TestFailureException(s"Expected '$actual' not to be empty.")
}
