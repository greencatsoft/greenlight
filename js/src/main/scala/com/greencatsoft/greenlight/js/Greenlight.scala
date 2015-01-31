package com.greencatsoft.greenlight.js

import org.scalajs.testinterface.TestUtils

import com.greencatsoft.greenlight.TestSuite
import com.greencatsoft.greenlight.build.{ TestFramework, TestRunner, TestTask }

import sbt.testing.{ EventHandler, Logger, Runner, Task, TaskDef }

class Greenlight extends TestFramework {

  override def runner(
    args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader): Runner =
    new RunnerImpl(args, remoteArgs, testClassLoader)

  override def slaveRunner(
    args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader, send: String => Unit): Runner =
    runner(args, remoteArgs, testClassLoader)

  class RunnerImpl(
      override val args: Array[String],
      override val remoteArgs: Array[String],
      override val classLoader: ClassLoader) extends TestRunner {

    override def tasks(list: Array[TaskDef]): Array[Task] =
      list.map(new TaskImpl(_, classLoader))

    override def receiveMessage(msg: String): Option[String] = None

    override def serializeTask(task: Task, serializer: TaskDef => String): String =
      serializer(task.taskDef)

    override def deserializeTask(task: String, deserializer: String => TaskDef): Task =
      new TaskImpl(deserializer(task), classLoader)
  }

  class TaskImpl(
      override val taskDef: TaskDef,
      override val classLoader: ClassLoader) extends TestTask {
    
  private def deepSelect(receiver: scalajs.js.Dynamic, name: String) =
    name.split('.').foldLeft(receiver)((obj, n) => obj.selectDynamic(n))
    
    override def suite: TestSuite = 
      TestUtils.loadModule(taskDef.fullyQualifiedName, classLoader).asInstanceOf[TestSuite]

    override def execute(eventHandler: EventHandler, loggers: Array[Logger], continuation: Array[Task] => Unit): Unit =
      continuation(execute(eventHandler, loggers))
  }
}

