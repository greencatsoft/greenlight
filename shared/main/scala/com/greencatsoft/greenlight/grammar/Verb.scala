package com.greencatsoft.greenlight.grammar

import com.greencatsoft.greenlight.grammar.Object.Expectation
import com.greencatsoft.greenlight.grammar.Specification.WhatIsExpected
import com.greencatsoft.greenlight.grammar.Statement.{ Assertation, AssertationBuilder }
import com.greencatsoft.greenlight.matcher.Matcher

trait Verb extends Word

trait PassiveVerb extends Verb

object Verb {

  class Be extends Verb {

    override val description: String = "be"

    def apply[A, E](expectation: Expectation[E])(implicit matcher: Matcher[A, Be, E]) =
      WhatIsExpected(this, expectation)
  }

  class Contain extends Verb {

    override val description: String = "contain"

    def apply[A, E](expectation: Expectation[E])(implicit matcher: Matcher[A, Contain, E]) =
      WhatIsExpected(this, expectation)
  }

  class FollowedByNegation[A](val builder: AssertationBuilder[A]) {

    def be[E](expectation: Expectation[E])(implicit matcher: Matcher[A, Be, E]): Assertation[A, Be, E] =
      builder.assert(WhatIsExpected(Words.be, !expectation))

    def contain[E](expectation: Expectation[E])(implicit matcher: Matcher[A, Contain, E]): Assertation[A, Contain, E] =
      builder.assert(WhatIsExpected(Words.contain, !expectation))
  }

  trait Words {

    object be extends Be

    object contain extends Contain
  }

  object Words extends Words
}
