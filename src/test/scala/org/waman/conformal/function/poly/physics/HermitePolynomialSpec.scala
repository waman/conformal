package org.waman.conformal.function.poly.physics

import org.waman.conformal.ConformalCustomSpec
import spire.math._
import spire.math.poly.SpecialPolynomials

class HermitePolynomialSpec extends ConformalCustomSpec{

  val hermites: Stream[Polynomial[Rational]] = SpecialPolynomials.physHermites(21)

  "apply(Int) factory method" - {

    "create n-th Hermite Polynomial (physics)" in {
      val conversions = Table("n", 0, 1, 2, 3, 4, 10, 20)

      forAll(conversions) { n: Int =>
        __SetUp__
        val expected = hermites(n)
        __Exercise__
        val sut = HermitePolynomial[Rational](n)
        __Verify__
        sut should be(expected)
      }
    }
  }

  "coeffsArray(Int) method" - {

    "create n-th Hermite Polynomial (physics)" in {
      val conversions = Table("n", 0, 1, 2, 3, 4, 10, 20)

      forAll(conversions) { n: Int =>
        __SetUp__
        val expected = hermites(n).coeffsArray
        __Exercise__
        val sut = HermitePolynomial.coeffsArray[Rational](n)
        __Verify__
        sut should be (expected)
      }
    }
  }

//    "performance test" - {
//
//      "conformal" in {
//        println(HermitePolynomial[Rational](1000))
//      }
//
//      "spire" in {
//        println(poly.SpecialPolynomials.physHermites[Rational](1001).apply(1000))
//      }
//    }
}
