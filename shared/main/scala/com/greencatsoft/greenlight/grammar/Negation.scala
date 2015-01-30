package com.greencatsoft.greenlight.grammar

trait Negation extends Word

object Negation {

  trait Words {

    object not extends Negation {
      override def description: String = "not"
    }
  }
}