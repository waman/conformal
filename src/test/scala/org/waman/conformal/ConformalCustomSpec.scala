package org.waman.conformal

import org.scalatest.prop.{PropertyChecks, TableFor1}
import org.waman.scalatest_util.WamanCustomSpec

class ConformalCustomSpec extends WamanCustomSpec with PropertyChecks{

  def forAllCases[A](conversions: TableFor1[A],
                filter: A => Boolean)(consumer: A => Unit): Unit =
    forAll(conversions) { a =>
      whenever(filter(a)){
        consumer(a)
      }
    }

  def forAllCases[A, B](conversions: TableFor1[A],
                   filter: A => Boolean,
                   map: A => B)(consumer: B => Unit): Unit =
    forAll(conversions){ a =>
      whenever(filter(a)){
        consumer(map(a))
      }
    }
}