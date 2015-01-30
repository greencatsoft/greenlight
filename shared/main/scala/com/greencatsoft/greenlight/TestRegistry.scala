package com.greencatsoft.greenlight

class TestRegistry {

  private var _testCases: Seq[TestCase[_]] = Nil

  def testCases: Seq[TestCase[_]] = _testCases

  def register[A](test: TestCase[A]): TestCase[A] = {
    this._testCases = _testCases.filterNot(_ == test) :+ test
    test
  }
}
