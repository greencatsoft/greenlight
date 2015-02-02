package com.greencatsoft.greenlight.matcher

import scala.collection.SeqLike

import com.greencatsoft.greenlight.TestFailureException
import com.greencatsoft.greenlight.grammar.Emptiness
import com.greencatsoft.greenlight.grammar.Verb.Be

object EmptinessMatcher extends Matcher[SeqLike[_, _], Be, Emptiness] {

  override def matches(actual: SeqLike[_, _], expected: Emptiness): Unit = if (!actual.isEmpty)
    throw TestFailureException(s"Expected '$actual' to be empty.")

  override def notMatches(actual: SeqLike[_, _], expected: Emptiness): Unit = if (actual.isEmpty)
    throw TestFailureException(s"Expected '$actual' not to be empty.")
}
