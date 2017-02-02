package org.waman.conformal.math

import org.waman.conformal.ConformalCustomSpec
import spire.math._
import spire.implicits._
import org.waman.conformal.{math => conformal}

class MathPackageSpec extends ConformalCustomSpec{

  "convertToComplex method" - {

    "convert Double to Complex[Double] implicitly" in {
      __SetUp__
      val d: Double = 3.0
      __Exercise__
      val sut = convertImplicitly[Complex[Double]](d)
      __Verify__
      sut should equal (Complex(d, 0.0))
    }

    "convert Real to Complex[Real] implicitly" in {
      __SetUp__
      val r = Real(3)
      __Exercise__
      val sut = convertImplicitly[Complex[Real]](r)
      __Verify__
      sut should equal (Complex(r, 0))
    }
  }

  "NRoot functions" - {

    "sqrt method" - {

      "for Double" in {
        __SetUp__
        val d: Double = 3.0
        val expected = Math.sqrt(d)
        __Exercise__
        val sut = sqrt(d)
        __Verify__
        sut should equal (expected)
      }
    }

    "for Real" in {
      __SetUp__
      val r = Real(3)
      val expected = r.sqrt()
      __Exercise__
      val sut = sqrt(r)
      __Verify__
      sut should equal (expected)
    }

    "for Complex[Double]" in {
      __SetUp__
      val c = Complex(3.0)
      val expected = c.sqrt
      __Exercise__
      val sut = sqrt(c)
      __Verify__
      sut should equal (expected)
    }
  }

  "Trig constants and functions" - {

    "e method" - {

      "for Double" in {
        __Exercise__
        val sut = conformal.e[Double]
        __Verify__
        sut should equal (Math.E)
      }

      "for Real" in {
        __Exercise__
        val sut = conformal.e[Real]
        __Verify__
        sut should equal (Real.e)
      }

      "for Complex[Double]" in {
        __Exercise__
        val sut = conformal.e[Complex[Double]]
        __Verify__
        sut should equal (Complex(Math.E))
      }
    }

    "exp method" - {

      "for Double" in {
        __SetUp__
        val d: Double = 3.0
        val expected = Math.exp(d)
        __Exercise__
        val sut = exp(d)
        __Verify__
        sut should equal (expected)
      }
    }

    "for Real" in {
      __SetUp__
      val r = Real(3)
      val expected = Real.exp(r)
      __Exercise__
      val sut = exp(r)
      __Verify__
      sut should equal (expected)
    }

    "for Complex[Double]" in {
      __SetUp__
      val c = Complex(3.0)
      val expected = c.exp
      __Exercise__
      val sut = exp(c)
      __Verify__
      sut should equal (expected)
    }
  }
}
