package com.greencatsoft.greenlight.grammar

import scala.language.implicitConversions
import com.greencatsoft.greenlight.Describable
import com.greencatsoft.greenlight.grammar.Object.Expectation
import com.greencatsoft.greenlight.matcher.Matcher

trait Specification extends Describable

object Specification {

  case class CaseDescription(override val description: String) extends Specification

  case class WhatIsExpected[V <: Verb, E](verb: V, expectation: Expectation[E])
      extends Specification {

    override def description: String = s"$verb $expectation"
  }

  trait Conversions {

    implicit def toCaseDescription(description: String): CaseDescription =
      CaseDescription(description)
  }
}