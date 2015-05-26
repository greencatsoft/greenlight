package com.greencatsoft.greenlight.build

import sbt.testing.{ Runner, Task, TaskDef }

class TestRunner(
      override val args: Array[String],
      override val remoteArgs: Array[String],
      val classLoader: ClassLoader) extends Runner {

  private var isDone = false

  override def tasks(list: Array[TaskDef]): Array[Task] =
    list.map(new TestTask(_, classLoader))

  override def done(): String = {
    if (isDone) throw new IllegalStateException(
      "The runner has already been executed.")

    this.isDone = true
    ""
  }

  // Scala.js specific

  def receiveMessage(msg: String): Option[String] = None

  def serializeTask(task: Task, serializer: TaskDef => String): String =
    serializer(task.taskDef)

  def deserializeTask(task: String, deserializer: String => TaskDef): Task =
    new TestTask(deserializer(task), classLoader)

}
