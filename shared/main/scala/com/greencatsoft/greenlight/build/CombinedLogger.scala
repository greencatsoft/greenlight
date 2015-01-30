package com.greencatsoft.greenlight.build

import sbt.testing.Logger

case class CombinedLogger(loggers: Seq[Logger]) {

  def trace(throwable: => Throwable): Unit = loggers.foreach(_.trace(throwable))

  def debug(message: => String): Unit = loggers.foreach(_.debug(message))

  def info(message: => String): Unit = loggers.foreach(_.info(message))

  def warn(message: => String): Unit = loggers.foreach(_.warn(message))

  def error(message: => String): Unit = loggers.foreach(_.error(message))
}