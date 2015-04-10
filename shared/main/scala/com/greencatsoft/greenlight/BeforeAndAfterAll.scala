package com.greencatsoft.greenlight

trait BeforeAndAfterAll {
  this: TestSuite =>

  private type Callback = () => Any

  private var beforeAllCallbacks = Seq.empty[Callback]

  private var afterAllCallbacks = Seq.empty[Callback]

  def beforeAll(callback: => Any): Unit =
    this.beforeAllCallbacks :+= (() => callback)

  def afterAll(callback: => Any): Unit =
    this.afterAllCallbacks +:= (() => callback)

  private[greenlight] def invokeBeforeAll(): Unit =
    beforeAllCallbacks.foreach(_.apply())

  private[greenlight] def invokeAfterAll(): Unit =
    afterAllCallbacks.foreach(_.apply())
}