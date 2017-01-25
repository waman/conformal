package org.waman.conformal.number.integral

import org.waman.conformal.ConformalCustomSpec
import org.waman.conformal.number.integral.combinatorics.{Combination, Permutation, WithRepetition}
import org.waman.conformal.tags.ForImplementationInterest

import scala.language.postfixOps

class PackageSpec extends ConformalCustomSpec{

  "flatFactorize(I) method should" - {

    "factorize the specified integer with prime factors included flatly in the result seq" in {
      val conversions = Table(
        ("n", "expected"),
        (1, Seq()),
        (2*3*4*5, Seq(2, 2, 2, 3, 5)),
        (10*11*12*13*14*15, Seq(2, 2, 2, 2, 3, 3, 5, 5, 7, 11, 13)),
        (1024, Seq.fill(10)(2))
      )

      forAll(conversions){ (n: Int, expected: Seq[Int]) =>
        __Exercise__
        val sut = flatFactorize(n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "factorize(I) method should" - {

    "factorize the specified integer and return Seq of (prime factor, duplication) pairs" in {
      val conversions = Table(
        ("n", "expected"),
        (1, Seq()),
        (2*3*4*5, Seq((2, 3), (3, 1), (5, 1))),
        (10*11*12*13*14*15, Seq((2, 4), (3, 2), (5, 2), (7, 1), (11, 1), (13, 1))),
        (1024, Seq((2, 10)))
      )

      forAll(conversions){ (n: Int, expected: Seq[(Int, Int)]) =>
        __Exercise__
        val sut = factorize(n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "factorial(Int) method should" - {

    "return a factorial n!" in {
      val conversions = Table(
        ("n", "expected"),
        (0, 1),
        (1, 1),
        (2, 2),
        (3, 6),
        (4, 24),
        (5, 120),
        (6, 720)
      )

      forAll(conversions){ (n: Int, expected: Int) =>
        __Exercise__
        val sut = factorial(n)
        __Verify__
        sut should equal (expected)
      }
    }

    "throw an IllegalArgumentException when the argument is negative" in {
      an [IllegalArgumentException] should be thrownBy{
        factorial(-1)
      }
    }
  }

  "factorial1(Int) method should" - {

    "return a factorial n!" taggedAs ForImplementationInterest in {
      val conversions = Table(
        ("n", "expected"),
        (0L, 1L),
        (1L, 1L),
        (2L, 2L),
        (3L, 6L),
        (4L, 24L),
        (5L, 120L),
        (6L, 720L)
      )

      forAll(conversions){ (n: Long, expected: Long) =>
        __Exercise__
        val sut = factorial1(n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "doubleFactorial(Int) method should" - {

    "return a factorial n!!" in {
      val conversions = Table(
        ("n", "expected"),
        (0, 1),
        (1, 1),
        (2, 2),
        (3, 3),
        (4, 8),
        (5, 15),
        (6, 48)
      )

      forAll(conversions){ (n: Int, expected: Int) =>
        __Exercise__
        val sut = doubleFactorial(n)
        __Verify__
        sut should equal (expected)
      }
    }

    "throw an IllegalArgumentException when the argument is negative" in {
      an [IllegalArgumentException] should be thrownBy{
        factorial(-1)
      }
    }
  }

  "doubleFactorial1(Int) method should" - {

    "return a factorial n!!" in {
      val conversions = Table(
        ("n", "expected"),
        (0L, 1L),
        (1L, 1L),
        (2L, 2L),
        (3L, 3L),
        (4L, 8L),
        (5L, 15L),
        (6L, 48L)
      )

      forAll(conversions){ (n: Long, expected: Long) =>
        __Exercise__
        val sut = doubleFactorial1(n)
        __Verify__
        sut should equal (expected)
      }
    }

    "throw an IllegalArgumentException when the argument is negative" in {
      an [IllegalArgumentException] should be thrownBy{
        factorial(-1)
      }
    }
  }


  "gcd(Int, Int) method should" - {

    "return gcd of the specified Int values" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (18, 24, 6),
        (21, 91, 7),
        (51, 13, 1),
        (18, -24, 6),
        (-18, 24, 6),
        (-18, -24, 6),
        (0, 24, 24),
        (18, 0, 18),
        (0, 0, 0)
      )

      forAll(conversions){ (m: Int, n: Int, expected: Int) =>
        __Exercise__
        val sut = gcd(m, n)
        __Verify__
        sut should equal (expected)
      }
    }

    "return gcd of the specified Long values" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (18L, 24L, 6L),
        (21L, 91L, 7L),
        (51L, 13L, 1L)
      )

      forAll(conversions){ (m: Long, n: Long, expected: Long) =>
        __Exercise__
        val sut = gcd(m, n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "gcd1(Int, Int) method should" - {

    "return gcd of the specified Int values" taggedAs ForImplementationInterest in {
      val conversions = Table(
        ("m", "n", "expected"),
        (18, 1, 1),
        (18, 24, 6),
        (21, 91, 7),
        (51, 13, 1),
//        (18, -24, 6),
//        (-18, 24, 6),
//        (-18, -24, 6),
        (0, 24, 24),
        (18, 0, 18),
        (0, 0, 0)
      )

      forAll(conversions){ (m: Int, n: Int, expected: Int) =>
        __Exercise__
        val sut = gcd1(m, n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "gcd2(Int, Int) method should" - {

    "return gcd of the specified Int values" taggedAs ForImplementationInterest in {
      val conversions = Table(
        ("m", "n", "expected"),
        (18, 1, 1),
        (18, 24, 6),
        (21, 91, 7),
        (51, 13, 1)
      )

      forAll(conversions){ (m: Int, n: Int, expected: Int) =>
        __Exercise__
        val sut = gcd2(m, n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "nGCD(Int, Int*)/ngcd(Seq[Int]) method should" - {

    "accept Int values as vararg" in {
      noException should be thrownBy{
        nGCD(1, 2, 3, 4)
      }
    }

    "return the same result as each other" in {
      __Exercise__
      val sut = nGCD(18, 24, 6)
      val expected = nGCD(Seq(18, 24, 6))
      __Verify__
      sut should equal (expected)
    }

    "return gcd of the specified Int values" in {
      val conversions = Table(
        ("arg", "expected"),
        (Seq(18, 24, 6), 6),
        (Seq(21, 91), 7),
        (Seq(51, 13, 26, 101), 1),
        (Seq(-3), 3),
        (Seq(18, -24, 6), 6)
      )

      forAll(conversions){ (arg: Seq[Int], expected: Int) =>
        __Exercise__
        val sut = nGCD(arg)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "lcm(Int, Int) method should" - {

    "return lcm of the specified Int values" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (18, 24, 72),
        (21, 91, 273),
        (51, 13, 663)
      )

      forAll(conversions){ (m: Int, n: Int, expected: Int) =>
        __Exercise__
        val sut = lcm(m, n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "nLCM(Int, Int*)/nlcm(Seq[Int]) method should" - {

    "accept Int values as vararg" in {
      noException should be thrownBy{
        nLCM(1, 2, 3, 4)
      }
    }

    "return the same result as each other" in {
      __Exercise__
      val sut = nLCM(18, 24, 6)
      val expected = nLCM(18, 24, 6)
      __Verify__
      sut should equal (expected)
    }

    "return gcd of the specified Int values" in {
      val conversions = Table(
        ("arg", "expected"),
        (Seq(18, 24, 6), 72),
        (Seq(21, 91), 273),
        (Seq(51, 13, 26, 101), 133926)
      )

      forAll(conversions){ (arg: Seq[Int], expected: Int) =>
        __Exercise__
        val sut = nLCM(arg)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "reduceIntegralFraction() method should" - {

    "reduce a fraction to an integer" in {
      __SetUp__
      val ns = Seq(100, 99, 98, 97)
      val ds = Seq(4, 3, 2, 1)
      __Exercise__
      val sut = reduceIntegralFraction(ns, ds)
      __Verify__
      sut should equal (25 * 33 * 49 * 97)
    }

    "throw an IllegalArgumentException if the reduction result is not an integer" in {
      an [IllegalArgumentException] should be thrownBy {
        reduceIntegralFraction(Seq(2, 3, 4), Seq(5))
      }
    }
  }

  "ConformalIntegral class" - {

    "Euclidean ring" - {

      "|/| method should" - {

        "return the quotient in terms of modular division" in {

          val conversions = Table(
            ("dividend", "divisor", "expected"),
            (91, 35, 2),
            (-91, 35, -3),

            (91, 7, 13),
            (-91, 7, -13),

            (91, 103, 0),
            (-91, 103, -1),

            (91, 91, 1),
            (-91, 91, -1)
          )

          forAll(conversions){ (dividend: Int, divisor: Int, expected: Int) =>
            __Exercise__
            val sut = dividend |/| divisor
            __Verify__
            sut should equal (expected)
          }
        }

        "throw an Exception when divisor is negative" in {
          __Verify__
          an [IllegalArgumentException] should be thrownBy{
            10 |/| -2
          }
        }
      }

      "|%| method should" - {

        "return the remainder in terms of modular division" in {

          val conversions = Table(
            ("dividend", "divisor", "expected"),
            (91, 35, 21),
            (-91, 35, 14),

            (91, 7, 0),
            (-91, 7, 0),

            (91, 103, 91),
            (-91, 103, 12),

            (91, 91, 0),
            (-91, 91, 0)
          )

          forAll(conversions){ (dividend: Int, divisor: Int, expected: Int) =>
            __Exercise__
            val sut = dividend |%| divisor
            __Verify__
            sut should equal (expected)
          }
        }

        "throw an Exception when divisor is negative" in {
          __Verify__
          an [IllegalArgumentException] should be thrownBy{
            10 |%| -2
          }
        }
      }

      "The result of |/| and |%| methods should" - {

        "satisfy the relation a = bq + r" in {

          val conversions = Table(
            ("dividend", "divisor"),
            (91, 35),
            (-91, 35),

            (91, 7),
            (-91, 7),

            (91, 103),
            (-91, 103),

            (91, 91),
            (-91, 91)
          )

          forAll(conversions){ (dividend: Int, divisor: Int) =>
            __SetUp__
            val q = dividend |/| divisor
            val r = dividend |%| divisor
            val sut = q * divisor + r
            __Verify__
            sut should equal (dividend)
          }
        }
      }

      "|/%| method should" - {

        "return the tuple of quotient and remainder in terms of modular division" in {

          val conversions = Table(
            ("dividend", "divisor", "expected"),
            (91, 35, (2, 21)),
            (-91, 35, (-3, 14)),

            (91, 7, (13, 0)),
            (-91, 7, (-13, 0)),

            (91, 103, (0, 91)),
            (-91, 103, (-1, 12)),

            (91, 91, (1, 0)),
            (-91, 91, (-1, 0))
          )

          forAll(conversions){ (dividend: Int, divisor: Int, expected: (Int, Int)) =>
            __Exercise__
            val sut = dividend |/%| divisor
            __Verify__
            sut should equal (expected)
          }
        }

        "throw an Exception when divisor is negative" in {
          __Verify__
          an [IllegalArgumentException] should be thrownBy{
            10 |/%| -2
          }
        }
      }
    }

    "Combinatorial" - {

      val conversions = Table("n", 0, 1, 2, 3, 4, 5, 6)

      "! operator should" - {

        "return factorial of the specified integer" in {
          //import spire.implicits._
          forAll(conversions){ n: Int=>
            __SetUp__
            val expected = factorial(n)
            __Exercise__
            val sut = n!;
            __Verify__
            sut should equal (expected)
          }
        }
      }

      "!! operator should" - {

        "return factorial of the specified integer" in {

          forAll(conversions){ n: Int=>
            __SetUp__
            val expected = doubleFactorial(n)
            __Exercise__
            val sut = n!!;
            __Verify__
            sut should equal (expected)
          }
        }
      }

      "P(Int) method should" - {

        "return nPr (permutation count)" in {
          forAll(conversions) { n: Int =>
            (0 to n).foreach { r =>
              __SetUp__
              val expected = Permutation.permutationCount(n, r)
              __Exercise__
              val sut = n P r
              __Verify__
              sut should equal(expected)
            }
          }
        }
      }

      "C(Int) method should" - {

        "return nCr (combination count)" - {

          forAll(conversions){ n: Int =>
            (0 to n).foreach{ r =>
              __SetUp__
              val expected = Combination.combinationCount(n, r)
              __Exercise__
              val sut = n C r
              __Verify__
              sut should equal (expected)
            }
          }
        }
      }

      "H(Int) method should" - {

        "return nHr (the number of combinations with repetition)" - {

          forAll(conversions){ n: Int =>
            (0 to (n+2)).foreach{ r =>
              __SetUp__
              val expected = WithRepetition.combinationCount(n, r)
              __Exercise__
              val sut = n H r
              __Verify__
              sut should equal (expected)
            }
          }
        }
      }
    }
  }
}
