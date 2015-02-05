package com.greencatsoft.greenlight

import scala.util.DynamicVariable
import com.greencatsoft.greenlight.grammar.Statement.Assertation
import com.greencatsoft.greenlight.grammar.Verb
import com.greencatsoft.greenlight.matcher.Matcher

class TestRegistry {

  private var tests: Seq[TestCase[_]] = Nil

  private[greenlight] val reporters: DynamicVariable[Seq[TestReporter]] = new DynamicVariable(Seq.empty)

  def testCases: Seq[TestCase[_]] = tests

  def register[A](test: TestCase[A]): TestCase[A] = {
    this.tests = tests.filterNot(_ == test) :+ test
    test
  }

  def register[A, V <: Verb, E](assertation: Assertation[A, V, E])(
    implicit matcher: Matcher[A, V, E]): Assertation[A, V, E] = {

    try {
      assertation.verify()
      reporters.value.foreach(_.passed(assertation))
    } catch {
      case e: TestFailureException =>
        reporters.value.foreach(_.failed(assertation, e))
    }

    assertation
  }
}
