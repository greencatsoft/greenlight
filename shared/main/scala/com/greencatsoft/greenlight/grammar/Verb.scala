package com.greencatsoft.greenlight.grammar

import com.greencatsoft.greenlight.TestReporter
import com.greencatsoft.greenlight.grammar.Object.Expectation
import com.greencatsoft.greenlight.grammar.Specification.WhatIsExpected
import com.greencatsoft.greenlight.grammar.Statement.Assertation
import com.greencatsoft.greenlight.matcher.Matcher

trait Verb extends Word

object Verb {

  trait Be extends Verb {

    def apply[A, E](expectation: Expectation[E])(implicit matcher: Matcher[A, Be, E]): WhatIsExpected[Be, E] =
      WhatIsExpected(this, expectation)

    override def description: String = "be"
  }

  class FollowedByNegation[A](builder: AssertationBuilder[A])(implicit reporter: TestReporter) {

    def be[E](expectation: Expectation[E])(implicit matcher: Matcher[A, Be, E]): Assertation[A, Be, E] =
      builder.assert(WhatIsExpected(Words.be, !expectation))
  }

  trait Words {

    object be extends Be
  }

  object Words extends Words
}

