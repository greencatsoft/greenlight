package com.greencatsoft.greenlight.matcher

import scala.collection.SeqLike

import com.greencatsoft.greenlight.TestFailureException
import com.greencatsoft.greenlight.grammar.Predicates.Empty
import com.greencatsoft.greenlight.grammar.Verb.Be

trait EmptySeqMatcher extends Matcher[SeqLike[_, _], Be, Empty] {

  override def matches(actual: SeqLike[_, _], expected: Empty): Unit =
    if (!actual.isEmpty)
      throw TestFailureException(s"Expected '$actual' to be empty.")

  override def notMatches(actual: SeqLike[_, _], expected: Empty): Unit =
    if (actual.isEmpty)
      throw TestFailureException(s"Expected '$actual' not to be empty.")
}
