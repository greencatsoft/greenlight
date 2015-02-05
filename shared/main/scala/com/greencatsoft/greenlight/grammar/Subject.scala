package com.greencatsoft.greenlight.grammar

import scala.language.implicitConversions

import com.greencatsoft.greenlight.TestRegistry
import com.greencatsoft.greenlight.grammar.ModalVerb.{ Can, Might, Must, Should }
import com.greencatsoft.greenlight.grammar.Specification.{ CaseDescription, WhatIsExpected }
import com.greencatsoft.greenlight.grammar.Statement.{ Assertation, CaseDefinition }
import com.greencatsoft.greenlight.grammar.Verb.FollowedByNegation
import com.greencatsoft.greenlight.matcher.Matcher

trait Subject[A] extends Word {

  val value: A

  private[grammar] def registry: TestRegistry

  def should(desc: CaseDescription): CaseDefinition[A] = Should.makeCase(this, desc)

  def should[V <: Verb, E](spec: WhatIsExpected[V, E])(implicit matcher: Matcher[A, V, E]): Assertation[A, V, E] =
    Should.makeAssertion(this, spec)

  def should(not: Negation): FollowedByNegation[A] = Should.makeNegativeAssertion(this, not)

  def must(desc: CaseDescription): CaseDefinition[A] = Must.makeCase(this, desc)

  def must[V <: Verb, E](spec: WhatIsExpected[V, E])(implicit matcher: Matcher[A, V, E]): Assertation[A, V, E] =
    Must.makeAssertion(this, spec)

  def must(not: Negation): FollowedByNegation[A] = Must.makeNegativeAssertion(this, not)

  def can(desc: CaseDescription): CaseDefinition[A] = Can.makeCase(this, desc)

  def can[V <: Verb, E](spec: WhatIsExpected[V, E])(implicit matcher: Matcher[A, V, E]): Assertation[A, V, E] =
    Can.makeAssertion(this, spec)

  def can(not: Negation): FollowedByNegation[A] = Can.makeNegativeAssertion(this, not)

  def might(desc: CaseDescription): CaseDefinition[A] = Might.makeCase(this, desc)

  override def description: String = toString(value)
}

object Subject {

  case class WhatToTest[A](
    override val value: A,
    override private[grammar] val registry: TestRegistry) extends Subject[A]

  trait Conversions {

    implicit def toSubject[T](value: T)(implicit registry: TestRegistry): Subject[T] = WhatToTest(value, registry)

    def It(implicit registry: TestRegistry): Subject[_] = lastSubject(registry)

    def They(implicit registry: TestRegistry): Subject[_] = lastSubject(registry)

    protected def lastSubject(implicit registry: TestRegistry): Subject[_] =
      registry.testCases.lastOption match {
        case Some(test) => test.definition.subject
        case None => throw new IllegalArgumentException(
          "Unable to find previous test declarations.")
      }
  }
}