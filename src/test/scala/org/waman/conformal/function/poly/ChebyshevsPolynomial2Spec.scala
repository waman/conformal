package org.waman.conformal.function.poly

import org.waman.conformal.ConformalCustomSpec
import spire.math._
import spire.math.poly.SpecialPolynomials

class ChebyshevsPolynomial2Spec extends ConformalCustomSpec{

  val chebyshevs: Stream[Polynomial[Rational]] = SpecialPolynomials.chebyshevsSecondKind(21)

  "apply(Int) factory method" - {

    "create n-th Hermite Polynomial (physics)" in {
      val conversions = Table("n", 0, 1, 2, 3, 4, 10, 20)

      forAll(conversions) { n: Int =>
        __SetUp__
        val expected = chebyshevs(n)
        __Exercise__
        val sut = ChebyshevsPolynomial2[Rational](n)
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
        val expected = chebyshevs(n).coeffsArray
        __Exercise__
        val sut = ChebyshevsPolynomial2.coeffsArray[Rational](n)
        __Verify__
        sut should be (expected)
      }
    }
  }
}
