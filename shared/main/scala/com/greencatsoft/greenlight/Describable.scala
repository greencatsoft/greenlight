package com.greencatsoft.greenlight

trait Describable {

  def description: String

  protected def toString(value: Any): String =
    Option(value).map(_.toString) getOrElse "(null)"

  override def toString(): String = description
}
