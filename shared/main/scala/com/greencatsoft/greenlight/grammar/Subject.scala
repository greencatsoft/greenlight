package com.greencatsoft.greenlight.grammar

import scala.language.implicitConversions

import com.greencatsoft.greenlight.{ TestRegistry, TestReporter }
import com.greencatsoft.greenlight.grammar.ModalVerb.{ Can, Must, Should }
import com.greencatsoft.greenlight.grammar.Specification.{ CaseDescription, WhatIsExpected }
import com.greencatsoft.greenlight.grammar.Statement.{ Assertation, CaseDefinition }
import com.greencatsoft.greenlight.grammar.Verb.FollowedByNegation
import com.greencatsoft.greenlight.matcher.Matcher

trait Subject[A] extends Word {

  val value: A

  def should(desc: CaseDescription): CaseDefinition[A] = CaseDefinition(this, Should, desc)

  def should[V <: Verb, E](spec: WhatIsExpected[V, E])(implicit reporter: TestReporter, matcher: Matcher[A, V, E]): Assertation[A, V, E] =
    AssertationBuilder(this, Should).assert(spec)

  def should(not: Negation)(implicit reporter: TestReporter): FollowedByNegation[A] =
    new FollowedByNegation(AssertationBuilder(this, Should))

  def must(desc: CaseDescription): CaseDefinition[A] = CaseDefinition(this, Must, desc)

  def must[V <: Verb, E](spec: WhatIsExpected[V, E])(implicit reporter: TestReporter, matcher: Matcher[A, V, E]): Assertation[A, V, E] =
    AssertationBuilder(this, Must).assert(spec)

  def must(not: Negation)(implicit reporter: TestReporter): FollowedByNegation[A] =
    new FollowedByNegation(AssertationBuilder(this, Must))

  def can(desc: CaseDescription): CaseDefinition[A] = CaseDefinition(this, Can, desc)

  def can[V <: Verb, E](spec: WhatIsExpected[V, E])(implicit reporter: TestReporter, matcher: Matcher[A, V, E]): Assertation[A, V, E] =
    AssertationBuilder(this, Can).assert(spec)

  def can(not: Negation)(implicit reporter: TestReporter): FollowedByNegation[A] =
    new FollowedByNegation(AssertationBuilder(this, Can))

  override def description: String = toString(value)
}

case class AssertationBuilder[A](subject: Subject[A], mode: ModalVerb) {

  def assert[V <: Verb, E](spec: WhatIsExpected[V, E])(implicit reporter: TestReporter, matcher: Matcher[A, V, E]): Assertation[A, V, E] =
    reporter.verify(Assertation[A, V, E](subject, mode, spec))
}

object Subject {

  case class WhatToTest[A](override val value: A, reporter: TestReporter) extends Subject[A]

  trait Conversions {

    implicit def toSubject[T](value: T)(implicit reporter: TestReporter): Subject[T] =
      WhatToTest(value, reporter)

    def it(implicit registry: TestRegistry): Subject[_] = previousSubject(registry)

    def they(implicit registry: TestRegistry): Subject[_] = previousSubject(registry)

    protected def previousSubject(implicit registry: TestRegistry): Subject[_] =
      registry.testCases.headOption match {
        case Some(test) => test.definition.subject
        case None => throw new IllegalArgumentException(
          "Unable to find previous test declarations.")
      }
  }
}