package com.greencatsoft.greenlight.build

import com.greencatsoft.greenlight.{ TestFailureException, TestReporter }
import com.greencatsoft.greenlight.grammar.Statement.Assertation
import com.greencatsoft.greenlight.grammar.Verb
import com.greencatsoft.greenlight.matcher.Matcher

class TestResultCollector extends TestReporter {

  private var _total = 0

  private var _failures: Seq[TestFailureException] = Nil

  private var _errors: Seq[Throwable] = Nil

  def total: Int = _total

  def success: Int = total - failure

  def failure: Int = _failures.size + _errors.size

  def failures: Seq[TestFailureException] = _failures

  def errors: Seq[Throwable] = _errors

  def verify[A, V <: Verb, E](assertation: Assertation[A, V, E])(
    implicit matcher: Matcher[A, V, E]): Assertation[A, V, E] = {
    try {
      this._total += 1

      assertation.verify()
    } catch {
      case e: TestFailureException =>
        this._failures +:= e
      case t: Throwable =>
        this._errors +:= t
    }

    assertation
  }
}