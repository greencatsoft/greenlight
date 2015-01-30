package com.greencatsoft.greenlight.jvm

import scala.reflect.runtime.universe

import com.greencatsoft.greenlight.TestSuite
import com.greencatsoft.greenlight.build.{ TestFramework, TestRunner, TestTask }

import sbt.testing.{ Runner, Task, TaskDef }

class Greenlight extends TestFramework {

  override def runner(
    args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader): Runner =
    new RunnerImpl(args, remoteArgs, testClassLoader)

  class RunnerImpl(
      override val args: Array[String],
      override val remoteArgs: Array[String],
      override val classLoader: ClassLoader) extends TestRunner {

    override def tasks(list: Array[TaskDef]): Array[Task] =
      list.map(new TaskImpl(_, classLoader))
  }

  class TaskImpl(
      override val taskDef: TaskDef,
      override val classLoader: ClassLoader) extends TestTask {

    override def suite: TestSuite = {
      import scala.reflect.runtime.universe

      val mirror = universe.runtimeMirror(classLoader)
      val symbol = mirror.staticModule(taskDef.fullyQualifiedName)

      mirror.reflectModule(symbol).instance.asInstanceOf[TestSuite]
    }
  }
}
