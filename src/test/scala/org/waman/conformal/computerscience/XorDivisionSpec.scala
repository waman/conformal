package org.waman.conformal.computerscience

import org.waman.conformal.ConformalCustomSpec
import spire.math._
import spire.algebra._
import spire.implicits._

class XorDivisionSpec extends ConformalCustomSpec{

  def binary(s: String): Int = Integer.valueOf(s, 2)

  "remainder() method should" - {

    "return the remainder of xor division" in {
      println(Integer.bitCount(17))
      __SetUp__
      val dividend = binary("10110000")
      val divisor  = binary("1101")
      val expected = binary("101")
      __Exercise__
      val sut = XorDivision.remainder(dividend, divisor)
      __Verify__
      sut should equal (expected)
    }
  }

  "ConstantDividend" - {

    "remainder() method should" - {

      "return the remainder" in {
        __SetUp__
        val constantDividend = XorDivision.constantDividend(binary("10110000"))
        val divisor = binary("1101")
        val expected = binary("101")
        __Exercise__
        val sut = constantDividend.remainder(divisor)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "ConstantDivisor" - {

    "remainder() method should" - {

      "return the remainder" in {
        __SetUp__
        val dividend = binary("10110000")
        val constantDivisor  = XorDivision.constantDivisor(binary("1101"))
        val expected = binary("101")
        __Exercise__
        val sut = constantDivisor.remainder(dividend)
        __Verify__
        sut should equal (expected)
      }
    }
  }
}
