package org.waman.conformal.number.integer.mod

import org.scalatest.prop.TableFor1
import org.waman.conformal.ConformalCustomSpec
import spire.implicits._
import org.waman.conformal.number.integer._

class ModularNumberSpec extends ConformalCustomSpec{

  val maxInt = BigInt(Int.MaxValue)
  val minInt = BigInt(Int.MinValue)
  val maxLong = BigInt(Long.MaxValue)
  val minLong = BigInt(Long.MinValue)

  val moduli: TableFor1[BigInt] = Table("moduli",
    BigInt(2), BigInt(7),
    maxInt-7, maxInt-1, maxInt, maxInt+1, maxInt+7,
    maxLong-7, maxLong-1, maxLong, maxLong+1, maxLong+7
  )

  val xs: TableFor1[BigInt] = Table("xs",
    BigInt(-7), BigInt(-2), BigInt(-1), BigInt(0), BigInt(1), BigInt(2), BigInt(7),
    maxInt-7, maxInt-1, maxInt, maxInt+1,maxInt+7,
    minInt-7, minInt-1, minInt, minInt+1, minInt+7,
    maxLong-7, maxLong-1, maxLong, maxLong+1, maxLong+7,
    minLong-7, minLong-1, minLong, minLong+1, minLong+7
  )

  val ys: TableFor1[BigInt] = Table("ys",
    BigInt(-7), BigInt(-2), BigInt(-1), BigInt(0), BigInt(1), BigInt(2), BigInt(7),
    maxInt-7, maxInt-1, maxInt, maxInt+1,maxInt+7,
    minInt-7, minInt-1, minInt, minInt+1, minInt+7,
    maxLong-7, maxLong-1, maxLong, maxLong+1, maxLong+7,
    minLong-7, minLong-1, minLong, minLong+1, minLong+7
  )

  "apply method (mod function in mod package) should" - {

    "create ModularNumber object (Int)" in {
      forCases[BigInt, Int](moduli, _.isValidInt, _.toInt){ m =>
        forCases[BigInt, Int](xs, _.isValidInt, _.toInt){ n =>
          __SetUp__
          val expected = (n mod m) ~% m
          __Exercise__
          val sut = n ~% m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create IntModularNumber from Long" in {
      forCases[BigInt, Int](moduli, _.isValidInt, _.toInt){ m =>
        forCases[BigInt, Long](xs, _.isValidLong, _.toLong){ n =>
          __SetUp__
          val expected = (n mod m) ~% m
          __Exercise__
          val sut = n ~% m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create IntModularNumber from BigInt" in {
      forCases[BigInt, Int](moduli, _.isValidInt, _.toInt){ m =>
        forAll(xs){ n: BigInt =>
          __SetUp__
          val expected = (n mod m) ~% m
          __Exercise__
          val sut = n ~% m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create ModularNumber object (Long)" in {
      forCases[BigInt, Long](moduli, _.isValidLong, _.toLong){ m =>
        forCases[BigInt, Long](xs, _.isValidLong, _.toLong){ n =>
          __SetUp__
          val expected = (n mod m) ~% m
          __Exercise__
          val sut = n ~% m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create LongModularNumber from Int" in {
      forCases[BigInt, Long](moduli, _.isValidLong, _.toLong){ m =>
        forCases[BigInt, Int](xs, _.isValidInt, _.toInt){ n =>
          __SetUp__
          val expected = (n.toLong mod m) ~% m
          __Exercise__
          val sut = n ~% m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create LongModularNumber from BigInt" in {
      forCases[BigInt, Long](moduli, _.isValidLong, _.toLong) { m =>
        forAll(xs){ n: BigInt =>
          __SetUp__
          val expected = (n mod m) ~% m
          __Exercise__
          val sut = n ~% m
          __Verify__
          sut should equal(expected)
        }
      }
    }

    "create ModularNumber object (BigInt)" in {
      forAll(moduli){ m: BigInt =>
        forAll(xs){ n: BigInt =>
          __SetUp__
          val expected = (n mod m) ~% m
          __Exercise__
          val sut = n ~% m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create BigIntModularNumber from Int" in {
      forAll(moduli){ m: BigInt =>
        forCases[BigInt, Int](xs, _.isValidInt, _.toInt){ n =>
          __SetUp__
          val expected = (BigInt(n) mod m) ~% m
          __Exercise__
          val sut = n ~% m
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create BigIntModularNumber from Long" in {
      forAll(moduli){ m: BigInt =>
        forCases[BigInt, Long](xs, _.isValidLong, _.toLong){ n =>
          __SetUp__
          val expected = (BigInt(n) mod m) ~% m
          __Exercise__
          val sut = n ~% m
          __Verify__
          sut should equal(expected)
        }
      }
    }
  }

  "unary_- method should" - {

    "return the additive-inverse element of the argument (Int)" in {
      forCases[BigInt, Int](moduli, _.isValidInt, _.toInt){ m =>
        forCases[BigInt, Int](xs, _.isValidInt, _.toInt){ n =>
          __SetUp__
          val mn = n ~% m
          val expected = (-n.toLong mod m) ~% m
          __Exercise__
          val sut = -mn
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "return the additive-inverse element of the argument (Long modulus)" in {
      forCases[BigInt, Long](moduli, _.isValidLong, _.toLong){ m =>
        forCases[BigInt, Long](xs, _.isValidLong, _.toLong){ n =>
          __SetUp__
          val mn = n ~% m
          val expected = (-BigInt(n) mod m) ~% m
          __Exercise__
          val sut = -mn
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "return the additive-inverse element of the argument (BigInt modulus)" in {
      forAll(moduli){ m: BigInt =>
        forAll(xs){ n: BigInt =>
          __SetUp__
          val mn = n ~% m
          val expected = (-n mod m) ~% m
          __Exercise__
          val sut = -mn
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }

  "+ operator should" - {

    "return the sum of this and the argument (Int)" in {
      forCases[BigInt, Int](moduli, _.isValidInt, _.toInt){ m =>
        forCases[BigInt, Int](xs, _.isValidInt, _.toInt){ x =>
          forCases[BigInt, Int](ys, _.isValidInt, _.toInt){ y =>
            __SetUp__
            val mx = x ~% m
            val my = y ~% m
            val expected = ((x.toLong + y.toLong) mod m) ~% m
            __Exercise__
            val sut = mx + my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "return the sum of this and the argument (Long)" in {
      forCases[BigInt, Long](moduli, _.isValidLong, _.toLong){ m =>
        forCases[BigInt, Long](xs, _.isValidLong, _.toLong){ x =>
          forCases[BigInt, Long](ys, _.isValidLong, _.toLong){ y =>
            __SetUp__
            val mx = x ~% m
            val my = y ~% m
            val expected = ((BigInt(x) + BigInt(y)) mod m) ~% m
            __Exercise__
            val sut = mx + my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "return the sum of this and the argument (BigInt)" in {
      forAll(moduli){ m: BigInt =>
        forAll(xs){ x: BigInt =>
          forAll(ys){ y: BigInt =>
            __SetUp__
            val mx = x ~% m
            val my = y ~% m
            val expected = ((x + y) mod m) ~% m
            __Exercise__
            val sut = mx + my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "throw an Exception if the moduli are different" in {
      forAll(moduli){ m: BigInt =>
        __SetUp__
        val mx = 0 ~% m
        val my = 0 ~% (m+1)
        __Verify__
        an [IllegalArgumentException] should be thrownBy{
          mx + my
        }
      }
    }
  }

  "- operator should" - {

    "return the difference of the argument by this" in {
      forCases[BigInt, Int](moduli, _.isValidInt, _.toInt){ m =>
        forCases[BigInt, Int](xs, _.isValidInt, _.toInt){ x =>
          forCases[BigInt, Int](ys, _.isValidInt, _.toInt){ y =>
            __SetUp__
            val mx = x ~% m
            val my = y ~% m
            val expected = ((x.toLong - y.toLong) mod m) ~% m
            __Exercise__
            val sut = mx - my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "return the difference of this and the argument (Long)" in {
      forCases[BigInt, Long](moduli, _.isValidLong, _.toLong){ m =>
        forCases[BigInt, Long](xs, _.isValidLong, _.toLong){ x =>
          forCases[BigInt, Long](ys, _.isValidLong, _.toLong){ y =>
            __SetUp__
            val mx = x ~% m
            val my = y ~% m
            val expected = ((BigInt(x) - BigInt(y)) mod m) ~% m
            __Exercise__
            val sut = mx - my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "return the difference of this and the argument (BigInt)" in {
      forAll(moduli){ m: BigInt =>
        forAll(xs){ x: BigInt =>
          forAll(ys){ y: BigInt =>
            __SetUp__
            val mx = x ~% m
            val my = y ~% m
            val expected = ((x - y) mod m) ~% m
            __Exercise__
            val sut = mx - my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "throw an Exception if the moduli are different" in {
      forAll(moduli){ m: BigInt =>
        __SetUp__
        val mx = 0 ~% m
        val my = 0 ~% (m+1)
        __Verify__
        an [IllegalArgumentException] should be thrownBy{
          mx - my
        }
      }
    }
  }

  "* operator should" - {

    "return the product of this and the argument (Int)" in {
      forCases[BigInt, Int](moduli, _.isValidInt, _.toInt){ m =>
        forCases[BigInt, Int](xs, _.isValidInt, _.toInt){ x =>
          forCases[BigInt, Int](ys, _.isValidInt, _.toInt){ y =>
            __SetUp__
            val mx = x ~% m
            val my = y ~% m
            val expected = ((x.toLong * y.toLong) mod m) ~% m
            __Exercise__
            val sut = mx * my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "return the product of this and the argument (Long)" in {
      forCases[BigInt, Long](moduli, _.isValidLong, _.toLong){ m =>
        forCases[BigInt, Long](xs, _.isValidLong, _.toLong){ x =>
          forCases[BigInt, Long](ys, _.isValidLong, _.toLong){ y =>
            __SetUp__
            val mx = x ~% m
            val my = y ~% m
            val expected = ((BigInt(x) * BigInt(y)) mod m) ~% m
            __Exercise__
            val sut = mx * my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "return the product of this and the argument (BigInt)" in {
      forAll(moduli){ m: BigInt =>
        forAll(xs){ x: BigInt =>
          forAll(ys){ y: BigInt =>
            __SetUp__
            val mx = x ~% m
            val my = y ~% m
            val expected = ((x * y) mod m) ~% m
            __Exercise__
            val sut = mx * my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "throw an Exception if the moduli are different" in {
      forAll(moduli){ m: BigInt =>
        __SetUp__
        val mx = 0 ~% m
        val my = 0 ~% (m+1)
        __Verify__
        an [IllegalArgumentException] should be thrownBy{
          mx * my
        }
      }
    }
  }

  "Methods of Any" - {

    val n = 3 ~% 7

    "equals() method should" - {

      "return true when the arg is the equivalent value even if they are the different objects" in {
        val conversions = Table(
          ("x", "y", "expected"),
          (n, n, true),
          (n, 3 ~% 7, true),
          (n, 3 ~% 11, false),
          (n, 4 ~% 7, false),
          (n, 4 ~% 11, false),
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
          (n, 3 ~% 7),
          (5 ~% 11, 5 ~% 11)
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
