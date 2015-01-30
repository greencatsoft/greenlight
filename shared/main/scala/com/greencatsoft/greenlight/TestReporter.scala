package com.greencatsoft.greenlight

import com.greencatsoft.greenlight.grammar.Statement.Assertation
import com.greencatsoft.greenlight.grammar.Verb
import com.greencatsoft.greenlight.matcher.Matcher

trait TestReporter {

  def verify[A, V <: Verb, E](assertation: Assertation[A, V, E])(
    implicit matcher: Matcher[A, V, E]): Assertation[A, V, E]
}

object TestReporter {

  object Dummy extends TestReporter {

    override def verify[A, V <: Verb, E](assertation: Assertation[A, V, E])(
      implicit matcher: Matcher[A, V, E]): Assertation[A, V, E] = ???
  }
}