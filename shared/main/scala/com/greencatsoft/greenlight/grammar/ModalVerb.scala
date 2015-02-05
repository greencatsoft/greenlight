package com.greencatsoft.greenlight.grammar

import com.greencatsoft.greenlight.TestReporter
import com.greencatsoft.greenlight.grammar.Specification.{ CaseDescription, WhatIsExpected }
import com.greencatsoft.greenlight.grammar.Statement.{ Assertation, CaseDefinition }
import com.greencatsoft.greenlight.grammar.Verb.FollowedByNegation
import com.greencatsoft.greenlight.matcher.Matcher

trait ModalVerb extends Word {

  def makeCase[A](subject: Subject[A], desc: CaseDescription): CaseDefinition[A] =
    CaseDefinition(subject, this, desc)

  def makeAssertion[A, V <: Verb, E](subject: Subject[A], spec: WhatIsExpected[V, E])(implicit reporter: TestReporter, matcher: Matcher[A, V, E]): Assertation[A, V, E] =
    AssertationBuilder(subject, this).assert(spec)

  def makeNegativeAssertion[A](subject: Subject[A], not: Negation)(implicit reporter: TestReporter): FollowedByNegation[A] =
    new FollowedByNegation(AssertationBuilder(subject, this))
}

object ModalVerb {

  object Should extends ModalVerb {
    override def description: String = "should"
  }

  object Must extends ModalVerb {
    override def description: String = "must"
  }

  object Can extends ModalVerb {
    override def description: String = "can"
  }

  object Might extends ModalVerb {
    override def description: String = "might"
  }
}