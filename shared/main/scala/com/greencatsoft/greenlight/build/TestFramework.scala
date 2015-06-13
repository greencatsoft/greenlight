package com.greencatsoft.greenlight.build

import sbt.testing.{ Fingerprint, Framework, SubclassFingerprint, Runner }

trait TestFramework extends Framework {

  override val name = "Greenlight"

  override def fingerprints: Array[Fingerprint] = Array {
    new SubclassFingerprint {

      override def isModule = true

      override def requireNoArgConstructor: Boolean = true

      override def superclassName: String = "com.greencatsoft.greenlight.TestSuite"
    }
  }

  override def runner(
    args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader): Runner =
    new TestRunner(args, remoteArgs, testClassLoader)

  // Scala.js specific

  def slaveRunner(
    args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader, send: String => Unit): Runner =
    runner(args, remoteArgs, testClassLoader)
}
