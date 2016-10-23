package org.waman.conformal.number.integer

import org.waman.conformal.ConformalCustomSpec

class IntegralPackageSpec extends ConformalCustomSpec{

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

  // For implementation interest
  "factorial1(Int) method should" - {

    "return a factorial n!" in {
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

  // For implementation interest
  "gcd1(Int, Int) method should" - {

    "return gcd of the specified Int values" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (18, 1, 1),
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
        val sut = gcd1(m, n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  // For implementation interest
  "gcd2(Int, Int) method should" - {

    "return gcd of the specified Int values" in {
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
}
