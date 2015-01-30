package com.greencatsoft.greenlight.grammar

import com.greencatsoft.greenlight.Describable

trait ModalVerb extends Word

object ModalVerb {

  object Should extends ModalVerb {
    override def description: String = "should"
  }

  object Must extends ModalVerb {
    override def description: String = "must"
  }

  object Can extends ModalVerb {
    override def description: String = "can"
  }
}