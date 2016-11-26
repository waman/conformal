package org.waman.conformal.number.integer.mod

import org.scalatest.words.ResultOfATypeInvocation
import org.waman.conformal.ConformalCustomSpec

class ModulusSpec extends ConformalCustomSpec{

  val maxInt = BigInt(Int.MaxValue)
  val minInt = BigInt(Int.MinValue)
  val maxLong = BigInt(Long.MaxValue)
  val minLong = BigInt(Long.MinValue)

  "apply method should" - {

    "create ModularNumber object (Int)" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (7, 0, 0),
        (7, 1, 1),
        (7, 6, 6),

        (7, 7, 0),
        (7, 8, 1),
        (7, 14, 0),
        (7, 15, 1),
        (7, Int.MaxValue, Int.MaxValue % 7),

        (7, -1, 6),
        (7, -2, 5),
        (7, -7, 0),
        (7, Int.MinValue, 6 - Int.MaxValue % 7),

        (Int.MaxValue, 0, 0),
        (Int.MaxValue, 1, 1),
        (Int.MaxValue, -1, Int.MaxValue - 1),

        (Int.MaxValue, Int.MaxValue, 0),
        (Int.MaxValue, Int.MinValue, Int.MaxValue - 1)
      )

      forAll(conversions){ (m: Int, n: Int, expected: Int) =>
        __Exercise__
        val sut = n mod m
        __Verify__
        sut.toInt should equal (expected)
      }
    }

    "create IntModularNumber from Long" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (11, 0L, 0),
        (11, 1L, 1),
        (11, -1L, 10),

        (11, Int.MaxValue.toLong, Int.MaxValue % 11),
        (11, Int.MaxValue + 1L  , Int.MaxValue % 11 + 1),
        (11, Int.MinValue.toLong, 10 - Int.MaxValue % 11),
        (11, Int.MinValue - 1L  , 9 - Int.MaxValue % 11),

        (11, Long.MaxValue, (Long.MaxValue % 11).toInt),
        (11, Long.MinValue, (10 - Long.MaxValue % 11).toInt)
      )

      forAll(conversions){ (m: Int, n: Long, expected: Int) =>
        __Exercise__
        val sut = n mod m
        __Verify__
        sut.toInt should equal (expected)
      }
    }

    "create IntModularNumber from BigInt" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (13, BigInt(0), 0),
        (13, BigInt(1), 1),
        (13, BigInt(-1), 12),

        (13, maxInt     , Int.MaxValue % 13),
        (13, maxInt + 1 , Int.MaxValue % 13 + 1),
        (13, minInt     , 12 - Int.MaxValue % 13),
        (13, minInt - 1 , 11 - Int.MaxValue % 13),

        (13, maxLong    , (Long.MaxValue % 13).toInt),
        (13, maxLong + 1, (Long.MaxValue % 13).toInt + 1),
        (13, minLong    , (12 - Long.MaxValue % 13).toInt),
        (13, minLong - 1, (11 - Long.MaxValue % 13).toInt)
      )

      forAll(conversions){ (m: Int, n: BigInt, expected: Int) =>
        __Exercise__
        val sut = n mod m
        __Verify__
        sut.toInt should equal (expected)
      }
    }

    "create ModularNumber object (Long)" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (Int.MaxValue + 7L, 0L, 0L),
        (Int.MaxValue + 7L, 1L, 1L),
        (Int.MaxValue + 7L, -1L, Int.MaxValue + 6L),

        (Int.MaxValue + 7L, Int.MaxValue.toLong, Int.MaxValue.toLong),
        (Int.MaxValue + 7L, Int.MaxValue + 1L  , Int.MaxValue + 1L),
        (Int.MaxValue + 7L, Int.MinValue.toLong, 6L),
        (Int.MaxValue + 7L, Int.MinValue - 1L  , 5L),

        (Int.MaxValue + 7L, Long.MaxValue, Long.MaxValue % (Int.MaxValue + 7L)),
        (Int.MaxValue + 7L, Long.MinValue, Int.MaxValue + 6L - Long.MaxValue % (Int.MaxValue + 7L)),


        (Long.MaxValue, 0L, 0L),
        (Long.MaxValue, 1L, 1L),
        (Long.MaxValue, -1L, Long.MaxValue - 1L),

        (Long.MaxValue, Int.MaxValue.toLong, Int.MaxValue.toLong),
        (Long.MaxValue, Int.MaxValue + 1L  , Int.MaxValue + 1L),
        (Long.MaxValue, Int.MinValue.toLong, Long.MaxValue + Int.MinValue),
        (Long.MaxValue, Int.MinValue - 1L  , Long.MaxValue + Int.MinValue - 1L),

        (Long.MaxValue, Long.MaxValue, 0L),
        (Long.MaxValue, Long.MinValue, Long.MaxValue - 1L)
      )

      forAll(conversions){ (m: Long, n: Long, expected: Long) =>
        __Exercise__
        val sut = n mod m
        __Verify__
        sut.toLong should equal (expected)
      }
    }

    "create LongModularNumber from Int" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (Long.MaxValue - 3L, 0, 0L),
        (Long.MaxValue - 3L, 1, 1L),
        (Long.MaxValue - 3L, -1, Long.MaxValue - 4L),

        (Long.MaxValue - 3L, Int.MaxValue, Int.MaxValue.toLong),
        (Long.MaxValue - 3L, Int.MinValue, Long.MaxValue + Int.MinValue - 3L)
      )

      forAll(conversions){ (m: Long, n: Int, expected: Long) =>
        __Exercise__
        val sut = n mod m
        __Verify__
        sut.toLong should equal (expected)
      }
    }

    "create LongModularNumber from BigInt" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (Long.MaxValue - 5L, BigInt(0), 0L),
        (Long.MaxValue - 5L, BigInt(1), 1L),
        (Long.MaxValue - 5L, BigInt(-1), Long.MaxValue - 6L),

        (Long.MaxValue - 5L, maxInt     , Int.MaxValue.toLong),
        (Long.MaxValue - 5L, maxInt + 1 , Int.MaxValue + 1L),
        (Long.MaxValue - 5L, minInt     , Long.MaxValue + Int.MinValue - 5L),
        (Long.MaxValue - 5L, minInt - 1 , Long.MaxValue + Int.MinValue - 6L),

        (Long.MaxValue - 5L, maxLong    , 5L),
        (Long.MaxValue - 5L, maxLong + 1, 6L),
        (Long.MaxValue - 5L, minLong    , Long.MaxValue - 11L),
        (Long.MaxValue - 5L, minLong - 1, Long.MaxValue - 12L)
      )

      forAll(conversions){ (m: Long, n: BigInt, expected: Long) =>
        __Exercise__
        val sut = n modulo m
        __Verify__
        sut.toLong should equal (expected)
      }
    }

    "create ModularNumber object (BigInt)" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (maxLong + 13, BigInt(0), BigInt(0)),
        (maxLong + 13, BigInt(1), BigInt(1)),
        (maxLong + 13, BigInt(-1), maxLong + 12),

        (maxLong + 13, maxInt     , maxInt),
        (maxLong + 13, maxInt + 1 , maxInt + 1),
        (maxLong + 13, minInt     , maxLong - maxInt + 12),
        (maxLong + 13, minInt - 1 , maxLong - maxInt + 11),

        (maxLong + 13, maxLong    , maxLong),
        (maxLong + 13, maxLong + 1, maxLong + 1),
        (maxLong + 13, minLong    , BigInt(12)),
        (maxLong + 13, minLong - 1, BigInt(11))
      )

      forAll(conversions){ (m: BigInt, n: BigInt, expected: BigInt) =>
        __Exercise__
        val sut = n modulo m
        __Verify__
        sut.toBigInt should equal (expected)
      }
    }

    "create BigIntModularNumber from Int" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (maxLong + 13, 0, BigInt(0)),
        (maxLong + 13, 1, BigInt(1)),
        (maxLong + 13, -1, maxLong + 12),

        (maxLong + 13, Int.MaxValue, maxInt),
        (maxLong + 13, Int.MinValue, maxLong + minInt + 13)
      )

      forAll(conversions){ (m: BigInt, n: Int, expected: BigInt) =>
        __Exercise__
        val sut = n mod m
        __Verify__
        sut.toBigInt should equal (expected)
      }
    }

    "create BigIntModularNumber from Long" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (maxLong + 17, 0L, BigInt(0)),
        (maxLong + 17, 1L, BigInt(1)),
        (maxLong + 17, -1L, maxLong + 16),

        (maxLong + 17, Int.MaxValue.toLong, maxInt),
        (maxLong + 17, Int.MaxValue + 1L  , maxInt + 1),
        (maxLong + 17, Int.MinValue.toLong, maxLong + minInt + 17),
        (maxLong + 17, Int.MinValue - 1L  , maxLong + minInt + 16),

        (maxLong + 17, Long.MaxValue, maxLong),
        (maxLong + 17, Long.MinValue, BigInt(16))
      )

      forAll(conversions){ (m: BigInt, n: Long, expected: BigInt) =>
        __Exercise__
        val sut = n mod m
        __Verify__
        sut.toBigInt should equal (expected)
      }
    }
  }

  "Methods of Any" - {

      val intM  = Modulus(7)
      val longM = Modulus(Long.MaxValue - 7)
      val biM   = Modulus(BigInt(Long.MaxValue) + 7)

      "== operator should" - {

        "evaluate equality of two objects" in {
          val conversions = Table(
            ("x", "y", "expected"),
            (intM, intM, true),
            (intM, Modulus(7), true),
            (intM, Modulus(7L), true),
            (intM, Modulus(BigInt(7)), true),
            (intM, Modulus(3), false),
            (intM, 7, false),

            (longM, longM, true),
            (longM, Modulus(Long.MaxValue - 7), true),
            (longM, Modulus(BigInt(Long.MaxValue)-7), true),
            (longM, Modulus(3), false),
            (longM, Modulus(BigInt(Long.MaxValue)+7), false),
            (longM, 7, false),

            (biM, biM, true),
            (biM, Modulus(BigInt(Long.MaxValue)+7), true),
            (biM, 7, false)
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
            (intM, intM),
            (intM, Modulus(7)),

            (longM, longM),
            (longM, Modulus(Long.MaxValue-7)),

            (biM, biM),
            (biM, Modulus(BigInt(Long.MaxValue)+7))
          )

          forAll(conversions){ (x: Modulus, y: Modulus) =>
            __Exercise__
            val sut = x.hashCode == y.hashCode
            __Verify__
            sut should be (true)
          }
        }
      }
    }

  "Modulus companion object" - {

    "apply() factory method should" - {

      "return a proper Modulus object if the argument is an Int value" in {
        val conversions = Table("m", 1, 2, Int.MaxValue)

        forAll(conversions){ m: Int =>
          __Exercise__
          val sut = Modulus(m)
          __Verify__
          sut should be (an [Modulus.IntModulus])
        }
      }

      "return a proper Modulus object if the argument is a Long value" in {
        val conversions = Table(
          ("m", "expected"),
          (7L                , a [Modulus.IntModulus]),
          (Int.MaxValue + 7L , a [Modulus.LongModulus]),
          (Long.MaxValue - 7L, a [Modulus.LongModulus])
        )

        forAll(conversions){ (m: Long, aType: ResultOfATypeInvocation[_]) =>
          __Exercise__
          val sut = Modulus(m)
          __Verify__
          sut should be (aType)
        }
      }

      "return a proper Modulus object if the argument is a BigInt value" in {
        val conversions = Table(
          ("m", "expected"),
          (BigInt(1)                , a [Modulus.IntModulus]),
          (BigInt(Int.MaxValue) + 1 , a [Modulus.LongModulus]),
          (BigInt(Long.MaxValue)    , a [Modulus.LongModulus]),
          (BigInt(Long.MaxValue) + 1, a [Modulus.BigIntModulus])
        )

        forAll(conversions){ (m: BigInt, aType: ResultOfATypeInvocation[_]) =>
          __Exercise__
          val sut = Modulus(m)
          __Verify__
          sut should be (aType)
        }
      }

      "throw an Exception if the argument is not positive" in {
        val conversions = Table("m", 0, -1)

        forAll(conversions){ m: Int =>
          __Verify__
          an [IllegalArgumentException] should be thrownBy{
            Modulus(m)
          }
        }
      }
    }
  }
}
