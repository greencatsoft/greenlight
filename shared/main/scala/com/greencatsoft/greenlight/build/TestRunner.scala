package com.greencatsoft.greenlight.build

import sbt.testing.Runner

trait TestRunner extends Runner {

  private var isDone = false

  def classLoader: ClassLoader

  override def done(): String = {
    if (isDone) throw new IllegalStateException("")

    this.isDone = false
    ""
  }
}
