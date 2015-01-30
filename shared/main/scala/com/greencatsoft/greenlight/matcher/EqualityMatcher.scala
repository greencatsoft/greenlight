package com.greencatsoft.greenlight.matcher

import com.greencatsoft.greenlight.TestFailureException
import com.greencatsoft.greenlight.grammar.Verb.Be

object EqualityMatcher extends Matcher[Any, Be, Any] {

  override def matches(actual: Any, expected: Any): Unit = if (actual != expected)
    throw TestFailureException(s"Expected '$expected' but found '$actual'.")

  override def notMatches(actual: Any, expected: Any): Unit = if (actual == expected)
    throw TestFailureException(s"Expected '$actual' not to match '$expected'.")
}