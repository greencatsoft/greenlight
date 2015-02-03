package com.greencatsoft.greenlight.grammar

object Predicates {

  trait Empty extends Word {
    override def description: String = "empty"
  }

  trait Defined extends Word {
    override def description: String = "defined"
  }

  trait Words {

    object empty extends Empty

    object defined extends Defined
  }
}