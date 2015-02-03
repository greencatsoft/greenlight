package com.greencatsoft.greenlight.matcher

import com.greencatsoft.greenlight.TestFailureException
import com.greencatsoft.greenlight.grammar.Predicates.Defined
import com.greencatsoft.greenlight.grammar.Verb.Be

trait NullityMatcher extends Matcher[AnyRef, Be, Defined] {

  def isDefined(value: AnyRef): Boolean = Option(value).isDefined

  override def matches(actual: AnyRef, expected: Defined): Unit =
    if (!isDefined(actual))
      throw TestFailureException(s"Expected the value to be defined but it wasn't.")

  override def notMatches(actual: AnyRef, expected: Defined): Unit =
    if (isDefined(actual))
      throw TestFailureException(s"Expected the value to be undefined, but found '${actual}'.")
}
