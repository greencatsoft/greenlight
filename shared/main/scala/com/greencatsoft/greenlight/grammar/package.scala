package com.greencatsoft.greenlight

package object grammar {

  trait Word extends Describable

  trait Grammar
    extends Verb.Words
    with Article.Words
    with Negation.Words
    with Subject.Conversions
    with Object.Conversions
    with Specification.Conversions
}