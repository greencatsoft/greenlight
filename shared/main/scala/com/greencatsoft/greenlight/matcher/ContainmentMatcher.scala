package com.greencatsoft.greenlight.matcher

import com.greencatsoft.greenlight.TestFailureException
import scala.collection.SeqLike

//object ContainmentMatcher extends Matcher[SeqLike[_], SeqLike[_]] {
//
//  override def matches[T](expected: T, actual: T): Unit = if (actual != expected)
//    throw TestFailureException(s"Expected '$expected' but found '$actual'.")
//
//  override def notMatches[T](expected: T, actual: T): Unit = if (actual == expected)
//    throw TestFailureException(s"Expected '$actual' not to match '$expected'.")
//}