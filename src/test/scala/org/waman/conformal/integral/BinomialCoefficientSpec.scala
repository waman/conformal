package org.waman.conformal.integral

import org.waman.conformal.ConformalCustomSpec

class BinomialCoefficientSpec extends ConformalCustomSpec{

  "apply() method should" - {

    "return the binomial coefficient value in the case n >= 0, 0 <= r <= n" in {
      val conversions = Table(
        ("n", "r", "expected"),
        (0, 0, 1),

        (1, 0, 1), (1, 1, 1),

        (3, 0, 1), (3, 1, 3), (3, 2, 3), (3, 3, 1),

        (5, 0, 1), (5, 1, 5), (5, 2, 10), (5, 3, 10), (5, 4, 5), (5, 5, 1),

        (100, 97, (100*99*98)/(3*2*1))
      )

      forAll(conversions){ (n: Int, r: Int, expected: Int) =>
        __Exercise__
        val sut = BinomialCoefficient(n, r)
        __Verify__
        sut should equal (expected)
      }
    }

    "return the binomial coefficient value of Long type" in {
      __SetUp__
      val expected = (100L * 99L * 98L * 97L * 96L) / (5L * 4L * 3L * 2L *1L)
      __Exercise__
      val sut = BinomialCoefficient(100L, 5L)
      __Verify__
      sut should equal (expected)
    }

    "throw an IllegalArgumentException when r < 0" in {
      val conversions = Table(
        ("n", "r"),
        (100, -2),
        (-5, 2),
        (3, 5)
      )

      forAll(conversions){ (n: Int, r: Int) =>
        __Verify__
        an [IllegalArgumentException] should be thrownBy{
          BinomialCoefficient(n, r)
        }
      }
    }
  }

  // For implementation interest
  "binomialCoefficientN() method should" - {

    val methodConversions = Table(
      "method",
      BinomialCoefficient.binomialCoefficient1(_, _),
      BinomialCoefficient.binomialCoefficient2(_, _),
      BinomialCoefficient.binomialCoefficient3(_: Int, _: Int))

    "return the binomial coefficient value in the case n >= 0, 0 <= r <= n" in {
      val conversions = Table(
        ("n", "r", "expected"),
        (0, 0, 1),

        (1, 0, 1), (1, 1, 1),

        (3, 0, 1), (3, 1, 3), (3, 2, 3), (3, 3, 1),

        (5, 0, 1), (5, 1, 5), (5, 2, 10), (5, 3, 10), (5, 4, 5), (5, 5, 1),

        (100, 97, (100 * 99 * 98) / (3 * 2 * 1))
      )

      forAll(methodConversions){ c: ((Int, Int) => Int) =>
        forAll(conversions) { (n: Int, r: Int, expected: Int) =>
          __Exercise__
          val sut = c(n, r)
          __Verify__
          sut should equal(expected)
        }
      }
    }
  }

  // For implementation interest
  "binomialCoefficient3() method should" - {

    "return the binomial coefficient value" in {
      val conversions = Table(
        ("n", "r", "expected"),
        (0, 0, 1),

        (1, 0, 1), (1, 1, 1),

        (3, 0, 1), (3, 1, 3), (3, 2, 3), (3, 3, 1),

        (5, 0, 1), (5, 1, 5), (5, 2, 10), (5, 3, 10), (5, 4, 5), (5, 5, 1),

        (100, 97, (100 * 99 * 98) / (3 * 2 * 1))
      )

      forAll(conversions) { (n: Int, r: Int, expected: Int) =>
        __Exercise__
        val sut = BinomialCoefficient.binomialCoefficient3(n, r)
        __Verify__
        sut should equal(expected)
      }
    }
  }
}
