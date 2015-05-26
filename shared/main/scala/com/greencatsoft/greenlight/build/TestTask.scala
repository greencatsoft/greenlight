package com.greencatsoft.greenlight.build

import org.scalajs.testinterface.TestUtils

import com.greencatsoft.greenlight.TestSuite

import sbt.testing.{ Event, EventHandler, Fingerprint, Logger, OptionalThrowable, Selector, Status, Task, TaskDef }

class TestTask(
      override val taskDef: TaskDef,
      val classLoader: ClassLoader) extends Task {

  override def tags: Array[String] = Array.empty

  override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
    val suite = TestUtils.loadModule(taskDef.fullyQualifiedName, classLoader).asInstanceOf[TestSuite]
    suite.run(new ConsoleReporter(taskDef, eventHandler, loggers))

    Array.empty
  }

  // Scala.js specific
  def execute(eventHandler: EventHandler, loggers: Array[Logger], continuation: Array[Task] => Unit): Unit =
    continuation(execute(eventHandler, loggers))

  case class TestResult(
    override val status: Status,
    override val duration: Long,
    override val throwable: OptionalThrowable = None) extends Event {

    override def fullyQualifiedName: String = taskDef.fullyQualifiedName

    override def selector: Selector = taskDef.selectors.head

    override def fingerprint: Fingerprint = taskDef.fingerprint
  }
}
