package com.greencatsoft.greenlight

import com.greencatsoft.greenlight.grammar.Statement

object LifeCycleTest extends TestSuite with BeforeAndAfter {

  var specification: Option[String] = None

  before { (s: Statement) =>
    this.specification = Some(s.description)
  }

  after {
    this.specification = None
  }

  "BeforeAndAfter trait" can "be used to be notified before and after each test" in {
    specification should not be (empty)
    specification foreach { it =>
      it should be ("BeforeAndAfter trait can be used to be notified before and after each test")
    }
  }
}