package com.greencatsoft.greenlight.build

import scala.Console.{ BOLD, BLUE, CYAN, GREEN, MAGENTA, RED, RESET, YELLOW }
import scala.scalajs.js.annotation.{ JSExport, JSExportDescendentObjects }

import com.greencatsoft.greenlight.{ TestStatisticsCollector, TestFailureException, TestSuite }
import com.greencatsoft.greenlight.grammar.Statement.CaseDefinition

import sbt.testing.{ Event, EventHandler, Fingerprint, Logger, OptionalThrowable, Selector, Status }
import sbt.testing.Status.{ Error, Failure, Skipped, Success }
import sbt.testing.TaskDef

class ConsoleReporter(
  val task: TaskDef,
  val eventHandler: EventHandler,
  val loggers: Array[Logger]) extends TestStatisticsCollector {

  private def indent(size: Int = 1) = "  " * size

  override def begin(suite: TestSuite) {
    super.begin(suite)

    loggers.info(s"${CYAN}Running ${suite.name}:$RESET")
  }

  override def begin(description: CaseDefinition[_]) {
    val subject = description.subject

    def printSubject() = {
      loggers.info("")
      loggers.info(s"${MAGENTA}* $RESET$BOLD$subject$RESET")
    }

    lastSubject match {
      case Some(last) if subject != last => printSubject()
      case None => printSubject()
      case _ =>
    }

    super.begin(description)
  }

  override def end(description: CaseDefinition[_], stats: CaseStats) {
    val CaseDefinition(_, mode, spec) = description

    def print(passed: Int, failed: Int) =
      s"$BLUE(${passed + failed} spec(s), $failed failure(s))$RESET"

    val event = stats match {
      case CaseStats(passed, failures, elapsed) if !lastError.isEmpty =>
        loggers.error(s"$RED${indent()}X $RESET$mode $spec $RED(error)$RESET")

        lastError foreach { e =>
          loggers.error(s"$RED${indent(2)}Error occurred while running the test: $e$RESET}")
          loggers.trace(e)
        }

        TestResult(Error, elapsed)
      case CaseStats(passed, failures, elapsed) if !failures.isEmpty =>
        loggers.warn(s"$RED${indent()}X $RESET$mode $spec ${print(passed, failures.size)}")

        failures collect {
          case TestFailureException(message, statement) => statement match {
            case Some(assertation) =>
              loggers.warn(s"$RED${indent(2)}: $message ('$assertation')$RESET")
            case None =>
              loggers.warn(s"$RED${indent(2)}$message$RESET")
          }
        }

        TestResult(Failure, elapsed)
      case CaseStats(passed, _, _) if passed == 0 =>
        loggers.info(s"$YELLOW${indent()}? $RESET$mode $spec $YELLOW(ignored)$RESET")

        TestResult(Skipped, 0)
      case CaseStats(passed, _, elapsed) =>
        loggers.info(s"$GREEN${indent()}+ $RESET$mode $spec ${print(passed, 0)}")

        TestResult(Success, elapsed)
    }

    eventHandler.handle(event)
  }

  override def end(suite: TestSuite, stats: SuiteStats) {
    super.end(suite, stats)

    stats match {
      case SuiteStats(passed, failed, errors, ignored, _) =>
        val total = passed + errors + failed + ignored

        loggers.info(s"${MAGENTA}Summary: Total $total, Passed $passed, Failed $failed, Error(s) $errors, Ignored $ignored.$RESET")
        loggers.info("")
    }
  }

  case class TestResult(
    override val status: Status,
    override val duration: Long,
    override val throwable: OptionalThrowable = None) extends Event {

    override def fullyQualifiedName: String = task.fullyQualifiedName

    override def selector: Selector = task.selectors.head

    override def fingerprint: Fingerprint = task.fingerprint
  }
}