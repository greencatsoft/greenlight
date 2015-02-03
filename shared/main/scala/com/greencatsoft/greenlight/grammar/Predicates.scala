package com.greencatsoft.greenlight.grammar

object Predicates {

  trait Empty extends Word {
    override def description: String = "empty"
  }

  trait Words {

    object empty extends Empty
  }
}