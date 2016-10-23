package org.waman.conformal.number.integer

import org.waman.conformal.ConformalCustomSpec

class GeneralizedBinomialCoefficientSpec extends ConformalCustomSpec{

  "apply() method should" - {

    "return the binomial coefficient value in the case n >= 0, 0 <= r <= n" in {
      val conversions = Table(
        ("n", "r", "expected"),
        // n = 0
        (0, -1, 0), (0, 0, 1), (0, 1, 0),

        // n = 1
        (1, -1, 0), (1, 0, 1), (1, 1, 1), (1, 2, 0),

        // n = 5
        (5, -1, 0), (5, 0, 1), (5, 1, 5), (5, 2, 10), (5, 3, 10), (5, 4, 5), (5, 5, 1), (5, 6, 0),

        // n = -1
        (-1, -1, 0), (-1, 0, 1), (-1, 1, -1), (-1, 2, 1),

        // n = -2
        (-2, -1, 0), (-2, 0, 1), (-2, 1, -2), (-2, 2, 3), (-2, 3, -4),

        // n = -3
        (-3, -1, 0), (-3, 0, 1), (-3, 1, -3), (-3, 2, 6), (-3, 3, -10), (-3, 4, 15)
      )

      forAll(conversions){ (n: Int, r: Int, expected: Int) =>
        __Exercise__
        val sut = GeneralizedBinomialCoefficient(n, r)
        __Verify__
        sut should equal (expected)
      }
    }

    "return the binomial coefficient value of Long type" in {
      __SetUp__
      val expected = (100L * 99L * 98L * 97L * 96L) / (5L * 4L * 3L * 2L *1L)
      __Exercise__
      val sut = GeneralizedBinomialCoefficient(100L, 5L)
      __Verify__
      sut should equal (expected)
    }
  }
}
