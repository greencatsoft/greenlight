package com.greencatsoft

import scala.language.higherKinds

package object greenlight {

  case class TestFailureException(message: String) extends Exception
}
