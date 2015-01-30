package com.greencatsoft.greenlight

import scala.language.implicitConversions

import com.greencatsoft.greenlight.build.CombinedLogger

import sbt.testing.{ Logger, OptionalThrowable }

package object build {

  implicit def toOptionalThrowable(throwable: Option[Throwable]): OptionalThrowable = throwable match {
    case Some(t) => new OptionalThrowable(t)
    case None => new OptionalThrowable
  }

  implicit def toCombinedLogger(loggers: Array[Logger]): CombinedLogger = CombinedLogger(loggers)
}