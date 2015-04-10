package com.greencatsoft.greenlight

import com.greencatsoft.greenlight.grammar.Statement

trait BeforeAndAfter {
  this: TestSuite =>

  private type Callback = Statement => Any

  private var beforeCallbacks = Seq.empty[Callback]

  private var afterCallbacks = Seq.empty[Callback]

  def before(callback: => Any): Unit =
    this.beforeCallbacks :+= ((s: Statement) => callback)

  def before(callback: Callback): Unit =
    this.beforeCallbacks :+= callback

  def after(callback: => Any): Unit =
    this.afterCallbacks +:= ((s: Statement) => callback)

  def after(callback: Callback): Unit =
    this.afterCallbacks +:= callback

  private[greenlight] def invokeBefore(statement: Statement): Unit =
    beforeCallbacks.foreach(_.apply(statement))

  private[greenlight] def invokeAfter(statement: Statement): Unit =
    afterCallbacks.foreach(_.apply(statement))
}