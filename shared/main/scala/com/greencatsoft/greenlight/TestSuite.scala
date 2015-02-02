package com.greencatsoft.greenlight

import scala.scalajs.js.annotation.JSExportDescendentObjects
import scala.util.DynamicVariable

import com.greencatsoft.greenlight.grammar.Grammar
import com.greencatsoft.greenlight.matcher.Matchers

@JSExportDescendentObjects
trait TestSuite extends Grammar with Matchers {

  val reporter: DynamicVariable[TestReporter] =
    new DynamicVariable(TestReporter.Dummy)

  implicit def currentReporter: TestReporter = reporter.value

  implicit val registry: TestRegistry = new TestRegistry
}
