package com.greencatsoft.greenlight.build

import com.greencatsoft.greenlight.TestSuite

import sbt.testing.{ Event, EventHandler, Fingerprint, Logger, OptionalThrowable, Selector, Status, Task }

trait TestTask extends Task {

  def classLoader: ClassLoader

  override def tags: Array[String] = Array.empty

  protected def suite: TestSuite

  override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
    suite.run(new ConsoleReporter(taskDef, eventHandler, loggers))

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