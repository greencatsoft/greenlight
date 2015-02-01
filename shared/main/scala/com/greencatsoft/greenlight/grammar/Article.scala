package com.greencatsoft.greenlight.grammar

import scala.reflect.ClassTag

object Article {

  trait Words {

    def A_[A](implicit tag: ClassTag[A]): ClassTag[A] = tag

    def An_[A](implicit tag: ClassTag[A]): ClassTag[A] = tag

    def a_[A](implicit tag: ClassTag[A]): ClassTag[A] = tag

    def an_[A](implicit tag: ClassTag[A]): ClassTag[A] = tag
  }
}