package com.greencatsoft.greenlight.matcher

import com.greencatsoft.greenlight.TestFailureException
import com.greencatsoft.greenlight.grammar.Emptiness
import com.greencatsoft.greenlight.grammar.Verb.Be

trait EmptyStringMatcher extends Matcher[String, Be, Emptiness] {

  def isEmpty(value: String): Boolean = Option(value).filter(_.length > 0).isEmpty

  override def matches(actual: String, expected: Emptiness): Unit =
    if (!isEmpty(actual))
      throw TestFailureException(s"Expected '$actual' to be empty.")

  override def notMatches(actual: String, expected: Emptiness): Unit =
    if (isEmpty(actual))
      throw TestFailureException(s"Expected '$actual' not to be empty.")
}
