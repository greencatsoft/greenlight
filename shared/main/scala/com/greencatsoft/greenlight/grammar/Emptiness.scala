package com.greencatsoft.greenlight.grammar

trait Emptiness extends Word

object Emptiness {

  trait Words {

    object empty extends Emptiness {
      override def description: String = "empty"
    }
  }
}