package com.greencatsoft.greenlight.grammar

import com.greencatsoft.greenlight.Describable

class CodeBlock[+A](block: () => A) extends Function0[A] with Describable {

  override def apply(): A = block()

  override def description: String = "[code block]"
}
