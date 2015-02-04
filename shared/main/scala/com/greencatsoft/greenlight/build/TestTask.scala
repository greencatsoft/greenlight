package com.greencatsoft.greenlight.build

import scala.Console.{ BLUE, BOLD, CYAN, GREEN, MAGENTA, RED, RESET, YELLOW }

import com.greencatsoft.greenlight.{ BeforeAndAfter, TestCase, TestFailureException, TestSuite }
import com.greencatsoft.greenlight.grammar.ModalVerb.Might
import com.greencatsoft.greenlight.grammar.Statement.CaseDefinition

import sbt.testing.{ Event, EventHandler, Fingerprint, Logger, OptionalThrowable, Selector, Status }
import sbt.testing.Status.{ Error, Failure, Skipped, Success }
import sbt.testing.Task

trait TestTask extends Task {

  def classLoader: ClassLoader

  override def tags: Array[String] = Array.empty

  protected def suite: TestSuite

  override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
    val currentSuite = suite

    def indent(size: Int = 1) = "  " * size

    loggers.info(s"${CYAN}Running ${taskDef.fullyQualifiedName}:$RESET")

    var lastSubject: Option[String] = None

    var success = 0
    var failure = 0
    var error = 0
    var ignored = 0

    suite.registry.testCases foreach { test =>
      suite match {
        case callback: BeforeAndAfter => callback.invokeBefore(test.definition)
        case _ =>
      }

      test match {
        case TestCase(CaseDefinition(subject, mode, spec), content) if mode == Might =>
          lastSubject = Some(subject.description)

          loggers.info(s"$YELLOW${indent()}? $RESET$mode $spec $YELLOW(ignored)$RESET")

          eventHandler.handle(TestResult(Skipped, 0))

          ignored += 1
        case TestCase(CaseDefinition(subject, mode, spec), content) =>
          try {
            def printSubject() = {
              loggers.info("")
              loggers.info(s"${MAGENTA}* $RESET$BOLD$subject$RESET")
            }

            lastSubject match {
              case Some(s) if subject.description != s => printSubject()
              case None => printSubject()
              case _ =>
            }

            lastSubject = Some(subject.description)

            val reporter = new TestResultCollector

            def stats = s"$BLUE(${reporter.total} specs, ${reporter.failure} failures)$RESET"

            def current = System.currentTimeMillis
            val started = current

            currentSuite.reporter.withValue(reporter) {
              content()
            }

            val elapsed = current - started

            val result = if (reporter.failure == 0) {
              loggers.info(s"$GREEN${indent()}+ $RESET$mode $spec $stats")

              success += 1
              TestResult(Success, elapsed)
            } else {
              loggers.info(s"$RED${indent()}X $RESET$mode $spec $stats")

              reporter.failures collect {
                case TestFailureException(message, statement) => statement match {
                  case Some(assertation) =>
                    loggers.info(s"$RED${indent(2)}: $message ('$assertation')$RESET")
                  case None =>
                    loggers.info(s"$RED${indent(2)}$message$RESET")
                }
              }

              reporter.errors.headOption match {
                case Some(t) =>
                  loggers.trace(t)

                  error += 1
                  TestResult(Error, elapsed, Some(t))
                case None =>
                  failure += 1
                  TestResult(Failure, elapsed)
              }
            }

            eventHandler.handle(result)
          } finally {
            suite match {
              case callback: BeforeAndAfter => callback.invokeAfter(test.definition)
              case _ =>
            }
          }
      }
    }

    val total = success + failure + error + ignored

    loggers.info(s"${MAGENTA}Summary: Total $total, Passed $success, Failed $failure, Error $error, Ignored $ignored.$RESET")
    loggers.info("")

    Array.empty
  }

  case class TestResult(
    override val status: Status,
    override val duration: Long,
    override val throwable: OptionalThrowable = None) extends Event {

    override def fullyQualifiedName: String = taskDef.fullyQualifiedName

    override def selector: Selector = taskDef.selectors.head

    override def fingerprint: Fingerprint = taskDef.fingerprint
  }
}