package com.greencatsoft.greenlight.grammar

import com.greencatsoft.greenlight.TestReporter
import com.greencatsoft.greenlight.grammar.Object.Expectation
import com.greencatsoft.greenlight.grammar.Specification.WhatIsExpected
import com.greencatsoft.greenlight.grammar.Statement.Assertation
import com.greencatsoft.greenlight.matcher.Matcher

trait Verb extends Word

trait PassiveVerb extends Verb

object Verb {

  abstract class AbstractVerb(override val description: String) extends Verb

  abstract class Be extends AbstractVerb("be") {

    def apply[A, E](expectation: Expectation[E])(implicit matcher: Matcher[A, Be, E]) =
      WhatIsExpected(this, expectation)
  }

  abstract class BeThrownIn extends AbstractVerb("be thrown in") with PassiveVerb {

    def apply[A](block: => Any)(implicit matcher: Matcher[A, BeThrownIn, CodeBlock[_]]) =
      WhatIsExpected(this, Expectation(CodeBlock(() => block)))
  }

  class FollowedByNegation[A](builder: AssertationBuilder[A])(implicit reporter: TestReporter) {

    def be[E](expectation: Expectation[E])(implicit matcher: Matcher[A, Be, E]): Assertation[A, Be, E] =
      builder.assert(WhatIsExpected(Words.be, !expectation))

    def be_thrown_in(block: => Any)(
      implicit matcher: Matcher[A, BeThrownIn, CodeBlock[_]]): Assertation[A, BeThrownIn, CodeBlock[_]] =
      builder.assert(WhatIsExpected(Words.be_thrown_in, !Expectation(CodeBlock(() => block))))
  }

  trait Words {

    object be extends Be

    object be_thrown_in extends BeThrownIn
  }

  object Words extends Words
}

