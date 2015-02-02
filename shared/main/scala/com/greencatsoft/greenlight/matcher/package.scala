package com.greencatsoft.greenlight

package object matcher {

  trait Matchers
    extends HighPriorityImplicits
    with ExceptionMatcher.Words
    with ExceptionMatcher.Conversions

  trait LowPriorityImplicits {

    implicit object equalityMatcher extends EqualityMatcher
  }

  trait DefaultPriorityImplicits extends LowPriorityImplicits {

    implicit object optionMatcher extends OptionMatcher

    implicit object exceptionMatcher extends ExceptionMatcher

    implicit object emptyStringMatcher extends EmptyStringMatcher
  }

  trait HighPriorityImplicits extends DefaultPriorityImplicits {

    implicit object emptySeqMatcher extends EmptySeqMatcher
  }
}