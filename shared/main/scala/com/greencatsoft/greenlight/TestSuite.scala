package com.greencatsoft.greenlight

import scala.scalajs.js.annotation.{ JSExport, JSExportDescendentObjects }

import com.greencatsoft.greenlight.grammar.Grammar
import com.greencatsoft.greenlight.grammar.ModalVerb.Might
import com.greencatsoft.greenlight.grammar.Statement.CaseDefinition
import com.greencatsoft.greenlight.matcher.Matchers

@JSExportDescendentObjects
trait TestSuite extends Grammar with Matchers {

  implicit val registry: TestRegistry = new TestRegistry

  @JSExport
  def name: String = getClass.getName

  def cases: Seq[CaseDefinition[_]] = registry.testCases.map(_.definition)

  def run(reporters: TestReporter*) {
    val runSuite =
      reporters.foldLeft(true) {
        (result, reporter) => result && reporter.begin(this)
      }

    this match {
      case callback: BeforeAndAfterAll =>
        callback.invokeBeforeAll()
      case _ =>
    }

    try {
      registry.testCases.foreach {
        case test =>
          this match {
            case callback: BeforeAndAfter =>
              callback.invokeBefore(test.definition)
            case _ =>
          }

          val TestCase(definition, content) = test

          val runCase = reporters.foldLeft(runSuite) {
            (result, reporter) => result && reporter.begin(definition)
          }

          if (runCase) {
            registry.reporters.withValue(reporters) {
              try {
                content()
              } catch {
                case t: Throwable => reporters.foreach(_.error(t))
              }
            }
          }

          reporters.foreach(_.end(definition))

          this match {
            case callback: BeforeAndAfter =>
              callback.invokeAfter(test.definition)
            case _ =>
          }
      }
    } finally {
      this match {
        case callback: BeforeAndAfterAll =>
          callback.invokeAfterAll()
        case _ =>
      }

      reporters.foreach(_.end(this))
    }
  }
}
