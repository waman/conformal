package org.waman.conformal.number.integral.mod

import org.scalatest.prop.TableFor1
import org.waman.conformal.ConformalCustomSpec
import spire.math.Integral
import spire.implicits._
import org.waman.conformal.number.integral._

class MersenneModularNumberSpec extends ConformalCustomSpec{

  val maxInt = BigInt(Int.MaxValue)
  val minInt = BigInt(Int.MinValue)
  val maxLong = BigInt(Long.MaxValue)
  val minLong = BigInt(Long.MinValue)

  val moduli: TableFor1[Int] = 
    Table("ps", 2, 3, 30, 31, 32, 33, 62, 63, 64, 65)

  val xs: TableFor1[BigInt] = Table("xs",
    BigInt(-7), BigInt(-2), BigInt(-1), BigInt(0), BigInt(1), BigInt(2), BigInt(7),
    maxInt-7, maxInt-1, maxInt, maxInt+1, maxInt+7,
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

  def genExpected(n: Int, p: Int) = n mod (2**p-1) mmod p
  def genExpected(n: Long, p: Int) = n mod (2L**p-1L) mmod p
  def genExpected(n: BigInt, p: Int) = n mod (BigInt(2)**1) mmod p
  
  "apply method (mod function in mod package) should" - {

    "create a MersenneModularNumber object" in {
      forAll(moduli){ p: Int =>
        forAll(xs){ n: BigInt =>
          __SetUp__
          val expected = genExpected(n, p)
          __Exercise__
          val sut = n mmod p
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create a MersenneModularNumber object from Int" in {
      forAll(moduli){ p: Int =>
        forCases[BigInt, Int](xs, _.isValidInt, _.toInt){ n =>
          __SetUp__
          val expected = genExpected(n, p)
          __Exercise__
          val sut = n mmod p
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "create a MersenneModularNumber object from Long" in {
      forAll(moduli){ p: Int =>
        forCases[BigInt, Long](xs, _.isValidLong, _.toLong){ n =>
          __SetUp__
          val expected = genExpected(n, p)
          __Exercise__
          val sut = n mmod p
          __Verify__
          sut should equal(expected)
        }
      }
    }
  }

  "unary_- method should" - {

    "return the additive-inverse element of the argument" in {
      forAll(moduli){ p: Int =>
        forAll(xs){ n: BigInt =>
          __SetUp__
          val mn = n mmod p
          val expected = genExpected(-n, p)
          __Exercise__
          val sut = -mn
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }

  "+ operator should" - {

    "return the sum of this and the argument" in {
      forAll(moduli){ p: Int =>
        forAll(xs){ x: BigInt =>
          forAll(ys){ y: BigInt =>
            __SetUp__
            val mx = x mmod p
            val my = y mmod p
            val expected = genExpected(x + y, p)
            __Exercise__
            val sut = mx + my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "throw an Exception if the moduli are different" in {
      forAll(moduli){ p: Int =>
        __SetUp__
        val mx = 0 mmod p
        val my = 0 mmod (p+1)
        __Verify__
        an [IllegalArgumentException] should be thrownBy{
          mx + my
        }
      }
    }
  }

  "- operator should" - {

    "return the difference of this and the argument" in {
      forAll(moduli){ p: Int =>
        forAll(xs){ x: BigInt =>
          forAll(ys){ y: BigInt =>
            __SetUp__
            val mx = x mmod p
            val my = y mmod p
            val expected = genExpected(x - y, p)
            __Exercise__
            val sut = mx - my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "throw an Exception if the moduli are different" in {
      forAll(moduli){ p: Int =>
        __SetUp__
        val mx = 0 mmod p
        val my = 0 mmod (p+1)
        __Verify__
        an [IllegalArgumentException] should be thrownBy{
          mx - my
        }
      }
    }
  }

  "* operator should" - {

    "return the product of this and the argument" in {
      forAll(moduli){ p: Int =>
        forAll(xs){ x: BigInt =>
          forAll(ys){ y: BigInt =>
            __SetUp__
            val mx = x mmod p
            val my = y mmod p
            val expected = genExpected(x * y, p)
            __Exercise__
            val sut = mx * my
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "throw an Exception if the moduli are different" in {
      forAll(moduli){ p: Int =>
        __SetUp__
        val mx = 0 mmod p
        val my = 0 mmod (p+1)
        __Verify__
        an [IllegalArgumentException] should be thrownBy{
          mx * my
        }
      }
    }
  }

  "Methods of Any" - {

    val n = 3 mmod 7

    "equals() method should" - {

      "return true when the arg is the equivalent value even if they are the different objects" in {
        val conversions = Table(
          ("x", "y", "expected"),
          (n, n, true),
          (n, 3 mmod 7, true),
          (n, 3 mmod 11, false),
          (n, 4 mmod 7, false),
          (n, 4 mmod 11, false),
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
          (n, 3 mmod 7),
          (5 mmod 11, 5 mmod 11)
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
