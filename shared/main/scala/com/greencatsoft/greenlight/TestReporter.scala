package com.greencatsoft.greenlight

import scala.scalajs.js.annotation.JSExportDescendentObjects

import com.greencatsoft.greenlight.grammar.ModalVerb.Might
import com.greencatsoft.greenlight.grammar.Statement.{ Assertation, CaseDefinition }
import com.greencatsoft.greenlight.grammar.Subject

trait TestReporter {

  def begin(suite: TestSuite): Boolean = true

  def begin(description: CaseDefinition[_]): Boolean =
    description.mode.mandatory

  def passed(assertation: Assertation[_, _, _]): Unit = Unit

  def failed(assertation: Assertation[_, _, _], e: TestFailureException): Unit = Unit

  def error(t: Throwable): Unit = Unit

  def end(description: CaseDefinition[_]): Unit = Unit

  def end(suite: TestSuite): Unit = Unit
}

trait StatefulTestReporter extends TestReporter {

  private var testSuite: Option[TestSuite] = None

  private var testCase: Option[CaseDefinition[_]] = None

  private var subject: Option[Subject[_]] = None

  private var error: Option[Throwable] = None

  def currentSuite: Option[TestSuite] = testSuite

  def currentCase: Option[CaseDefinition[_]] = testCase

  def lastSubject: Option[Subject[_]] = subject

  def lastError: Option[Throwable] = error

  override def begin(suite: TestSuite): Boolean = {
    val continue = super.begin(suite)

    this.testSuite = Some(suite)
    this.subject = None

    continue
  }

  override def begin(description: CaseDefinition[_]): Boolean = {
    val continue = super.begin(description)

    this.testCase = Some(description)
    this.subject = Some(description.subject)
    this.error = None

    continue
  }

  override def error(t: Throwable) {
    super.error(t)

    this.error = Some(t)
  }

  override def end(description: CaseDefinition[_]) {
    super.end(description)

    this.testCase = None
  }

  override def end(suite: TestSuite) {
    super.end(suite)

    this.testSuite = None
  }
}

trait TestStatisticsCollector extends StatefulTestReporter {

  case class SuiteStats(
    passed: Int = 0,
    failed: Int = 0,
    errors: Int = 0,
    ignored: Int = 0,
    time: Long = System.currentTimeMillis)

  case class CaseStats(
    passed: Int = 0,
    failures: Seq[TestFailureException] = Seq.empty,
    time: Long = System.currentTimeMillis)

  private var suiteStats = SuiteStats()

  private var caseStats = CaseStats()

  override def begin(suite: TestSuite): Boolean = {
    this.suiteStats = SuiteStats()

    super.begin(suite)
  }

  override def begin(description: CaseDefinition[_]): Boolean = {
    this.caseStats = CaseStats()

    super.begin(description)
  }

  override def passed(assertation: Assertation[_, _, _]) {
    this.caseStats = caseStats match {
      case CaseStats(p, f, t) => CaseStats(p + 1, f, t)
    }

    super.passed(assertation)
  }

  override def failed(assertation: Assertation[_, _, _], e: TestFailureException) {
    this.caseStats = caseStats match {
      case CaseStats(p, f, t) => CaseStats(p, f :+ e, t)
    }

    super.failed(assertation, e)
  }

  override def end(description: CaseDefinition[_]) {
    this.caseStats = caseStats match {
      case CaseStats(p, f, t) =>
        CaseStats(p, f, System.currentTimeMillis - t)
    }

    this.suiteStats = suiteStats match {
      case SuiteStats(p, f, e, i, t) if lastError.isDefined =>
        SuiteStats(p, f, e + 1, i, t)
      case SuiteStats(p, f, e, i, t) if !caseStats.failures.isEmpty =>
        SuiteStats(p, f + 1, e, i, t)
      case SuiteStats(p, f, e, i, t) if caseStats.passed == 0 =>
        SuiteStats(p, f, e, i + 1, t)
      case SuiteStats(p, f, e, i, t) => SuiteStats(p + 1, f, e, i, t)
    }

    end(description, caseStats)

    super.end(description)
  }

  def end(description: CaseDefinition[_], stats: CaseStats): Unit = Unit

  override def end(suite: TestSuite) {
    this.suiteStats = suiteStats match {
      case SuiteStats(p, f, e, i, t) =>
        SuiteStats(p, f, e, i, System.currentTimeMillis - t)
    }

    end(suite, suiteStats)

    super.end(suite)
  }

  def end(suite: TestSuite, stats: SuiteStats): Unit = Unit
}
