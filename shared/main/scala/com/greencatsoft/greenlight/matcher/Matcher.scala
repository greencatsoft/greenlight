package com.greencatsoft.greenlight.matcher

import com.greencatsoft.greenlight.TestFailureException
import com.greencatsoft.greenlight.grammar.Verb

trait Matcher[-A, V <: Verb, -E] {

  @throws[TestFailureException]
  def matches(actual: A, expected: E): Unit

  @throws[TestFailureException]
  def notMatches(actual: A, expected: E): Unit
}

object Matcher {

  trait LowerPriorityMatchers {
    implicit val equalityMatcher = EqualityMatcher
  }

  trait Matchers extends LowerPriorityMatchers {

    implicit val emptinessMatcher = EmptinessMatcher

    implicit val exceptionMatcher = ExceptionMatcher
  }
}