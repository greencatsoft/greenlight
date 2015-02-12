package com.greencatsoft.greenlight.grammar

import com.greencatsoft.greenlight.{ Describable, TestCase, TestFailureException, TestRegistry }
import com.greencatsoft.greenlight.grammar.Object.Expectation
import com.greencatsoft.greenlight.grammar.Specification.{ CaseDescription, WhatIsExpected }
import com.greencatsoft.greenlight.matcher.Matcher

trait Statement extends Describable {

  val subject: Subject[_]

  val mode: ModalVerb

  val specification: Specification

  override def description: String = s"$subject $mode $specification"
}

object Statement {

  case class CaseDefinition[A](
    override val subject: Subject[A],
    override val mode: ModalVerb,
    override val specification: CaseDescription) extends Statement {

    def in(content: => Any)(implicit registry: TestRegistry): TestCase[A] =
      registry.register(TestCase[A](this, new CodeBlock(() => content)))

    def in[T](content: T => Any)(implicit registry: TestRegistry, fixture: () => T): TestCase[A] =
      registry.register(TestCase[A](this, new CodeBlock(() => content.apply(fixture()))))
  }

  case class Assertation[A, V <: Verb, E](
    override val subject: Subject[A],
    override val mode: ModalVerb,
    override val specification: WhatIsExpected[V, E])(implicit matcher: Matcher[A, V, E]) extends Statement {
println("########### " + matcher)
    @throws[TestFailureException]
    def verify(): Assertation[A, V, E] =
      specification match {
        case WhatIsExpected(_, Expectation(value, negation)) =>
          try {
            if (negation)
              matcher.notMatches(subject.value, value)
            else
              matcher.matches(subject.value, value)
          } catch {
            case TestFailureException(msg, _) =>
              throw TestFailureException(msg, Some(this))
          }

          this
      }
  }

  case class AssertationBuilder[A](subject: Subject[A], mode: ModalVerb) {

    def assert[V <: Verb, E](spec: WhatIsExpected[V, E])(implicit matcher: Matcher[A, V, E]): Assertation[A, V, E] =
      subject.registry.register(Assertation[A, V, E](subject, mode, spec))
  }
}