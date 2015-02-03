package com.greencatsoft.greenlight

package object matcher {

  trait Matchers
    extends HighPriorityImplicits
    with ExceptionMatcher.Words
    with ExceptionMatcher.Conversions

  trait LowPriorityImplicits {

    implicit object matchEquality extends EqualityMatcher
  }

  trait DefaultPriorityImplicits extends LowPriorityImplicits {

    implicit object matchOptionEmpiness extends OptionMatchers.Emptiness

    implicit object matchOptionDefinition extends OptionMatchers.Definition

    implicit object matchException extends ExceptionMatcher

    implicit object matchStringEmptiness extends StringMatchers.Emptiness
  }

  trait HighPriorityImplicits extends DefaultPriorityImplicits {

    implicit object matchCollectionEmptiness extends CollectionMatchers.Emptiness

    implicit object matchNullity extends NullityMatcher
  }
}