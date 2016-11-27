package org.waman.conformal.number.integer.mod

import org.waman.conformal.ConformalCustomSpec
import spire.implicits._
import spire.syntax.literals.si._

class ModularNumberSpec extends ConformalCustomSpec{

  val maxInt = BigInt(Int.MaxValue)
  val minInt = BigInt(Int.MinValue)
  val maxLong = BigInt(Long.MaxValue)
  val minLong = BigInt(Long.MinValue)

  "unary_- method should" - {

    "return the additive-inverse element of the argument (Int)" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (7, 0, 0),
        (7, 1, 6),
        (7, 2, 5),
        (7, 3, 4),
        (7, 4, 3),
        (7, 5, 2),
        (7, 6, 1),

        (7, 7, 0),
        (7, 8, 6),

        (7, -1, 1),
        (7, -2, 2),

        (7, Int.MaxValue, Int.MaxValue % 7),
        (7, Int.MinValue, 6 - Int.MaxValue % 7),


        (Int.MaxValue, 0, 0),
        (Int.MaxValue, 1, 1),
        (Int.MaxValue, -1, Int.MaxValue - 1),

        (Int.MaxValue, Int.MaxValue, 0),
        (Int.MaxValue, Int.MinValue, 6)
      )

      forAll(conversions){ (m: Int, n: Int, expected: Int) =>
        __SetUp__
        val mn = n mod m
        __Exercise__
        val sut = -mn
        __Verify__
        sut.toInt should equal (expected)
      }
    }

    "return the additive-inverse element of the argument (Long modulus)" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (Int.MaxValue + 7L, 0L, 0L),
        (Int.MaxValue + 7L, 1L, Int.MaxValue + 6L),
        (Int.MaxValue + 7L, -1L, 1L),

        (Int.MaxValue + 7L, Int.MaxValue.toLong, 7L),
        (Int.MaxValue + 7L, Int.MaxValue + 1L  , 6L),
        (Int.MaxValue + 7L, Int.MinValue.toLong, Int.MaxValue + 1L),
        (Int.MaxValue + 7L, Int.MinValue - 1L  , Int.MaxValue + 2L),

        (Int.MaxValue + 7L, Long.MaxValue, Int.MaxValue + 7L - Long.MaxValue % (Int.MaxValue + 7L)),
        (Int.MaxValue + 7L, Long.MinValue, Long.MaxValue % (Int.MaxValue + 7L) + 1L),


        (Long.MaxValue, 0L, 0L),
        (Long.MaxValue, 1L, Long.MaxValue - 1L),
        (Long.MaxValue, -1L, 1L),

        (Long.MaxValue, Int.MaxValue.toLong, Long.MaxValue - Int.MaxValue),
        (Long.MaxValue, Int.MaxValue + 1L  , Long.MaxValue - Int.MaxValue - 1L),
        (Long.MaxValue, Int.MinValue.toLong, Int.MaxValue + 1L),
        (Long.MaxValue, Int.MinValue - 1L  , Int.MaxValue + 2L),

        (Long.MaxValue, Long.MaxValue, 0L),
        (Long.MaxValue, Long.MinValue, 1L)
      )

      forAll(conversions){ (m: Long, n: Long, expected: Long) =>
        __SetUp__
        val mn = n mod m
        __Exercise__
        val sut = -mn
        __Verify__
        sut.toLong should equal (expected)
      }
    }

    "return the additive-inverse element of the argument (BigInt modulus)" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (maxLong + 7, BigInt(0), BigInt(0)),
        (maxLong + 7, BigInt(1), maxLong + 6),
        (maxLong + 7, BigInt(-1), BigInt(1)),

        (maxLong + 7, maxInt, maxLong - maxInt + 7),
        (maxLong + 7, maxInt + 1, maxLong - maxInt + 6),
        (maxLong + 7, minInt, maxInt + 1),
        (maxLong + 7, minInt - 1, maxInt + 2),

        (maxLong + 7, maxLong, BigInt(7)),
        (maxLong + 7, maxLong + 1, BigInt(6)),
        (maxLong + 7, minLong, maxLong + 1),
        (maxLong + 7, minLong - 1, maxLong + 2)
      )

      forAll(conversions) { (m: BigInt, n: BigInt, expected: BigInt) =>
        __SetUp__
        val mn = n modulo m
        __Exercise__
        val sut = -mn
        __Verify__
        sut.toBigInt should equal(expected)
      }
    }
  }

  "+ operator should" - {

    "return the sum of this and the argument (Int)" in {
      val conversions = Table(
        ("m", "x", "y", "e"),
        (7, 0, 2, 2),
        (7, 3, 0, 3),
        (7, 1, 2, 3),
        (7, 5, 4, 2),

        (Int.MaxValue, Int.MaxValue - 7, 10, 3),
        (Int.MaxValue, Int.MaxValue - 7, Int.MaxValue - 10, Int.MaxValue - 17)
      )

      forAll(conversions){ (m: Int, x: Int, y: Int, e: Int) =>
        __SetUp__
        val xm = x mod m
        val ym = y mod m
        val expected = e mod m
        __Exercise__
        val sut = xm + ym
        __Verify__
        sut should equal (expected)
      }
    }

    "return the sum of this and the argument (Long)" in {
      val conversions = Table(
        ("m", "x", "y", "e"),
        (Int.MaxValue + 7L, 0L, 2L, 2L),
        (Int.MaxValue + 7L, 3L, 0L, 3L),
        (Int.MaxValue + 7L, 1L, 2L, 3L),
        (Int.MaxValue + 7L, Int.MaxValue + 5L, 4L, 2L),
        (Int.MaxValue + 7L, 5L, Int.MaxValue + 4L, 2L),
        (Int.MaxValue + 7L, Int.MaxValue + 5L, Int.MaxValue + 4L, Int.MaxValue + 2L),

        (Long.MaxValue, Long.MaxValue - 7L, 10L, 3L),
        (Long.MaxValue, Long.MaxValue - 7L, Long.MaxValue - 10L, Long.MaxValue - 17L)
      )

      forAll(conversions){ (m: Long, x: Long, y: Long, e: Long) =>
        __SetUp__
        val xm = x mod m
        val ym = y mod m
        val expected = e mod m
        __Exercise__
        val sut = xm + ym
        __Verify__
        sut should equal (expected)
      }
    }

    "return the sum of this and the argument (BigInt)" in {
      val conversions = Table(
        ("m", "x", "y", "e"),
        (maxLong + 7, big"0", big"2", big"2"),
        (maxLong + 7, big"3", big"0", big"3"),
        (maxLong + 7, big"1", big"2", big"3"),
        (maxLong + 7, big"3", maxLong + 10, big"6"),
        (maxLong + 7, maxLong + 3, big"10", big"6"),
        (maxLong + 7, maxLong + 3, maxLong + 10, maxLong + big"6")
      )

      forAll(conversions){ (m: BigInt, x: BigInt, y: BigInt, e: BigInt) =>
        __SetUp__
        val xm = x modulo m
        val ym = y modulo m
        val expected = e modulo m
        __Exercise__
        val sut = xm + ym
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "- operator should" - {

    "return the difference of the argument by this" in {
      val conversions = Table(
        ("p", "x", "y"),
        (7, 0, 2),
        (7, 3, 0),
        (7, 1, 2),
        (7, 5, 4),
        (7, 2, 6),
        (7, 3, 4),

        (5, 15, 19)
      )

      forAll(conversions){ (p: Int, x: Int, y: Int) =>
        __SetUp__
        val mmod = Modulus(p)
        val (xm, ym, expected) = (mmod(x), mmod(y), mmod(x-y))
        __Exercise__
        val sut = xm - ym
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "* operator should" - {

    "return the product of this and the argument" in {
      val conversions = Table(
        ("p", "x", "y"),
        (7, 0, 2),
        (7, 3, 0),
        (7, 1, 2),
        (7, 5, 4),
        (7, 2, 6),
        (7, 3, 4),

        (5, 15, 19)
      )

      forAll(conversions){ (p: Int, x: Int, y: Int) =>
        __SetUp__
        val mmod = Modulus(p)
        val (xm, ym, expected) = (mmod(x), mmod(y), mmod(x*y))
        __Exercise__
        val sut = xm * ym
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "Type Conversions" - {

    "toInt method should" - {

      "return an Int representation of this object" in {
        val conversions = Table(
          ("m", "n"),
          (7, 0),
          (7, 1)
        )

        forAll(conversions){ (m: Int, n: Int) =>
          __SetUp__
          val x = n mod m
          val expected = n % m.toInt
          __Exercise__
          val sut = x.toInt
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }

  "Methods of Any" - {

    val n = 3 mod 7

    "equals() method should" - {

      "return true when the arg is the equivalent value even if they are the different objects" in {
        val conversions = Table(
          ("x", "y", "expected"),
          (n, n, true),
          (n, 3 mod 7, true),
          (n, 3 mod 11, false),
          (n, 4 mod 7, false),
          (n, 4 mod 11, false),
          (n, 4, false)
        )

        forAll(conversions){ (x: Any, y: Any, expected: Boolean) =>
          __Exercise__
          val sut = x == y
          __Verify__
          sut should be (expected)
        }
      }
    }

    "hashCode method should" - {

      "return the same value when the two objects are the equivalent value even if different objects" in {
        val conversions = Table(
          ("x", "y"),
          (n, n),
          (n, 3 mod 7),
          (5 mod 11, 5 mod 11)
        )

        forAll(conversions){ (x: ModularNumber, y: ModularNumber) =>
          __Exercise__
          val sut = x.hashCode == y.hashCode
          __Verify__
          sut should be (true)
        }
      }
    }
  }
}
