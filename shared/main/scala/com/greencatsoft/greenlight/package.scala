package com.greencatsoft

import com.greencatsoft.greenlight.grammar.Statement

package object greenlight {

  case class TestFailureException(
    message: String, statement: Option[Statement] = None) extends Exception
}
