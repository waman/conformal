package org.waman.conformal.function

import org.waman.conformal.ConformalCustomSpec
import spire.math._

class HermitePolynomialSpec extends ConformalCustomSpec{

  "apply(Int) factory method" - {

    "create n-th Hermite Polynomial (physics)" in {
      val conversions = Table(
        ("n", "Hn(x)"),
        (0, Polynomial("1")),
        (1, Polynomial("2x")),
        (2, Polynomial("4x^2 - 2")),
        (3, Polynomial("8x^3 - 12x")),
        (4, Polynomial("16x^4 - 48x^2 + 12")),
        (5, Polynomial("32x^5 - 160x^3 + 120x"))
      )

      forAll(conversions){ (n: Int, expected: Polynomial[Rational]) =>
        __Exercise__
        val sut = HermitePolynomial[Rational](n)
        __Verify__
        sut should be (expected)
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
}
