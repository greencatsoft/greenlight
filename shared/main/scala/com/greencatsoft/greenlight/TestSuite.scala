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
    reporters.foreach(_.begin(this))

    try {
      registry.testCases.foreach {
        case test =>
          this match {
            case callback: BeforeAndAfter =>
              callback.invokeBefore(test.definition)
            case _ =>
          }

          val TestCase(definition, content) = test

          reporters.foreach(_.begin(definition))

          if (definition.mode != Might) {
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
      reporters.foreach(_.end(this))
    }
  }
}
