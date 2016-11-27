package org.waman.conformal.number.integer.mod

import org.scalatest.words.ResultOfATypeInvocation
import org.waman.conformal.ConformalCustomSpec
import org.waman.conformal.number.integer._

class ModulusSpec extends ConformalCustomSpec{

  val maxInt = BigInt(Int.MaxValue)
  val minInt = BigInt(Int.MinValue)
  val maxLong = BigInt(Long.MaxValue)
  val minLong = BigInt(Long.MinValue)

  val testValues = Table("n",
    BigInt(-7) ,BigInt(-1),BigInt(0), BigInt(1), BigInt(7),
    maxInt-7, maxInt-1, maxInt, maxInt+1, maxInt+7,
    minInt-7, minInt-1, minInt, minInt+1, minInt+7,
    maxLong-7, maxLong-1, maxLong, maxLong+1, maxLong+7,
    minLong-7, minLong-1, minLong, minLong+1, minLong+7
  )

  "apply method should" - {

    "create ModularNumber object (Int)" in {
      forAllCases[BigInt, Int](testValues, m => m > 0 && m.isValidInt, _.toInt){ m =>
        forAllCases[BigInt, Int](testValues, _.isValidInt, _.toInt){ n =>
          __SetUp__
          val expected = (n |%| m) mod m
          __Exercise__
          val sut = n mod m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create IntModularNumber from Long" in {
      forAllCases[BigInt, Int](testValues, m => m > 0 && m.isValidInt, _.toInt){ m =>
        forAllCases[BigInt, Long](testValues, _.isValidInt, _.toLong){ n =>
          __SetUp__
          val expected = (n |%| m) mod m
          __Exercise__
          val sut = n mod m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create IntModularNumber from BigInt" in {
      forAllCases[BigInt, Int](testValues, m => m > 0 && m.isValidInt, _.toInt){ m =>
        forAllCases[BigInt](testValues, _.isValidInt){ n =>
          __SetUp__
          val expected = (n |%| m) modulo m
          __Exercise__
          val sut = n modulo m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create ModularNumber object (Long)" in {
      forAllCases[BigInt, Long](testValues, m => m > 0 && m.isValidInt, _.toLong){ m =>
        forAllCases[BigInt, Long](testValues, _.isValidInt, _.toLong){ n =>
          __SetUp__
          val expected = (n |%| m) mod m
          __Exercise__
          val sut = n mod m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create LongModularNumber from Int" in {
      forAllCases[BigInt, Long](testValues, m => m > 0 && m.isValidInt, _.toLong){ m =>
        forAllCases[BigInt, Int](testValues, _.isValidInt, _.toInt){ n =>
          __SetUp__
          val expected = (n.toLong |%| m) mod m
          __Exercise__
          val sut = n mod m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create LongModularNumber from BigInt" in {
      forAllCases[BigInt, Long](testValues, m => m > 0 && m.isValidInt, _.toLong) { m =>
        forAllCases[BigInt](testValues, _.isValidInt) { n =>
          __SetUp__
          val expected = (n |%| m) modulo m
          __Exercise__
          val sut = n modulo m
          __Verify__
          sut should equal(expected)
        }
      }
    }

    "create ModularNumber object (BigInt)" in {
      forAllCases[BigInt](testValues, m => m > 0 && m.isValidInt){ m =>
        forAllCases[BigInt](testValues, _.isValidInt){ n =>
          __SetUp__
          val expected = (n |%| m) modulo m
          __Exercise__
          val sut = n modulo m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create BigIntModularNumber from Int" in {
      forAllCases[BigInt](testValues, m => m > 0 && m.isValidInt){ m =>
        forAllCases[BigInt, Int](testValues, _.isValidInt, _.toInt){ n =>
          __SetUp__
          val expected = (BigInt(n) |%| m) modulo m
          __Exercise__
          val sut = n modulo m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create BigIntModularNumber from Long" in {
      forAllCases[BigInt](testValues, m => m > 0 && m.isValidInt){ m =>
        forAllCases[BigInt, Long](testValues, _.isValidInt, _.toLong){ n =>
          __SetUp__
          val expected = (BigInt(n) |%| m) modulo m
          __Exercise__
          val sut = n modulo m
          __Verify__
          sut should equal(expected)
        }
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
