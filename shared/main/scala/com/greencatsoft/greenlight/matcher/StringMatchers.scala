package com.greencatsoft.greenlight.matcher

import com.greencatsoft.greenlight.TestFailureException
import com.greencatsoft.greenlight.grammar.Predicates.Empty
import com.greencatsoft.greenlight.grammar.Verb.Be

object StringMatchers {

  trait Emptiness extends Matcher[String, Be, Empty] {

    def isEmpty(value: String): Boolean = Option(value).filter(_.length > 0).isEmpty

    override def matches(actual: String, expected: Empty): Unit =
      if (!isEmpty(actual))
        throw TestFailureException(s"Expected '$actual' to be empty.")

    override def notMatches(actual: String, expected: Empty): Unit =
      if (isEmpty(actual))
        throw TestFailureException(s"Expected '$actual' not to be empty.")
  }
}