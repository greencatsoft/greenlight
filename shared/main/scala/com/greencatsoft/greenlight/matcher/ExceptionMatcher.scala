package com.greencatsoft.greenlight.matcher

import scala.reflect.ClassTag

import com.greencatsoft.greenlight.TestFailureException
import com.greencatsoft.greenlight.grammar.CodeBlock
import com.greencatsoft.greenlight.grammar.Verb.BeThrownIn

object ExceptionMatcher extends Matcher[ClassTag[_], BeThrownIn, CodeBlock[_]] {

  override def matches(actual: ClassTag[_], expected: CodeBlock[_]) {
    val expectedType = actual.runtimeClass

    try {
      expected()

      throw TestFailureException(s"Expected '$expectedType' to be thrown but it wasn't.")
    } catch {
      case t: Throwable if !expectedType.isAssignableFrom(t.getClass) =>
        t match {
          case e: RuntimeException => throw e
          case e: Exception => throw new RuntimeException(e)
        }
      case _: Throwable =>
    }
  }

  override def notMatches(actual: ClassTag[_], expected: CodeBlock[_]) {
    val expectedType = actual.runtimeClass

    try {
      expected()
    } catch {
      case t: Throwable if expectedType.isAssignableFrom(t.getClass) =>
        throw TestFailureException(s"Expected '$expectedType' not to be thrown but it was.")
      case e: RuntimeException => throw e
      case e: Exception => throw new RuntimeException(e)
    }
  }
}