package com.greencatsoft.greenlight.grammar

import scala.language.implicitConversions

trait Object extends Word

object Object {

  case class Expectation[E](value: E, negation: Boolean = false) extends Object {

    def unary_!(): Expectation[E] = Expectation(value, !negation)

    override def description: String = toString(value)
  }

  trait Conversions {

    implicit def toExpectation[E](value: E): Expectation[E] = Expectation(value)
  }
}