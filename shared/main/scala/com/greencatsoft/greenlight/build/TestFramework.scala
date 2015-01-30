package com.greencatsoft.greenlight.build

import sbt.testing.{ Fingerprint, Framework, SubclassFingerprint }

trait TestFramework extends Framework {

  override val name = "Greenlight"

  override def fingerprints: Array[Fingerprint] = Array {
    new SubclassFingerprint {

      override val isModule = true

      override def requireNoArgConstructor: Boolean = true

      override def superclassName: String = "com.greencatsoft.greenlight.TestSuite"
    }
  }
}
