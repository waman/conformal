package org.waman.conformal.number.integer

import org.scalactic.TypeCheckedTripleEquals
import org.waman.conformal.ConformalCustomSpec
import org.waman.conformal.tags.ForImplementationInterest
import spire.implicits._
import spire.math.UInt

class UIntMersenneModuloSpec
    extends ConformalCustomSpec with TypeCheckedTripleEquals{

  "UIntMersenneNumber object" - {

    "isPrime method should" - {

      "return true for the following integers" taggedAs ForImplementationInterest in {
        val conversions = Table("p", 2, 3, 5, 7, 13, 17, 19, 31)

        forAll(conversions){ p: Int =>
          __Exercise__
          val sut = UIntMersenneNumber.isPrime(p)
          __Verify__
          sut should be (true)
        }
      }

      "return false for the following integers" taggedAs ForImplementationInterest in {
        val conversions = Table("p", 11)

        forAll(conversions){ p: Int =>
          __Exercise__
          val sut = UIntMersenneNumber.isPrime(p)
          __Verify__
          sut should be (false)
        }
      }
    }
  }

  "UIntMersenneModulo class" - {

    "modulus method should" - {

      "return the modulus" in {
        val conversions = Table(
          ("p", "expected"),
          (3, 7),
          (5, 31)
        )

        forAll(conversions){ (p: Int, expected: Int) =>
          __SetUp__
          val mmod = UIntMersenneModulo(p)
          __Exercise__
          val sut = mmod.modulus
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "fromUInt" - {

      "create MersenneModuloUInt object by a UInt Parameter" in {
        val conversions = Table(
          ("p", "n"),
          (3, 0),  // p=3 => mod 7
          (3, 1),
          (3, 2),
          (3, 3),
          (3, 4),
          (3, 5),
          (3, 6),

          (3, 7),  // <=  111
          (3, 8),  // <= 1000 = 001 mmod 3
          (3, 9),   // <= 1001 = 010 mmod 3
          (3, 10),
          (3, 11),
          (3, 12),
          (3, 13),

          (3, 14),
          (3, 15),

          (13, 13),
          (19, 524308)
        )

        forAll(conversions){ (p: Int, n: Int) =>
          __SetUp__
          val mmod = UIntMersenneModulo(p)
          val expected = UInt(n % (2**p-1))
          __Exercise__
          val sut = mmod.fromUInt(UInt(n))
          __Verify__
          sut.p should equal (p)
          sut.digit should === (expected)
        }
      }
    }


    "apply factory method should" - {

      "create MersenneModuloUInt object by a positive Int Parameter" in {
        val conversions = Table(
          ("p", "n"),
          (3, 0),  // p=3 => mod 7
          (3, 1),
          (3, 2),
          (3, 3),
          (3, 4),
          (3, 5),
          (3, 6),

          (3, 7),  // <=  111
          (3, 8),  // <= 1000 = 001 mmod 3
          (3, 9),   // <= 1001 = 010 mmod 3
          (3, 10),
          (3, 11),
          (3, 12),
          (3, 13),

          (3, 14),
          (3, 15),

          (13, 13),
          (19, 524308)
        )

        forAll(conversions){ (p: Int, n: Int) =>
          __SetUp__
          val mmod = UIntMersenneModulo(p)
          val expected = UInt(n % (2**p-1))
          __Exercise__
          val sut = mmod(n)
          __Verify__
          sut.p should equal (p)
          sut.digit should === (expected)
        }
      }

      "create UIntMersenneModuloNumber object by a negative Int Parameter" in {
        val conversions = Table(
          ("p", "n"),
          (3, -1),
          (3, -2),
          (3, -3),
          (3, -4),
          (3, -5),
          (3, -6),
          (3, -7),
          (3, -8),
          (3, -9),
          (3, -10)
        )

        forAll(conversions){ (p: Int, n: Int) =>
          __SetUp__
          val mmod = UIntMersenneModulo(p) // for p = 3, n = -2
          val mn = 2**p-1                  // mn = 2^3-1 = 7
          val e = mn - (-n) % mn     // e = 7-(2%7) = 5
          val expected = if(e == mn) UInt(0) else UInt(e)
          __Exercise__
          val sut = mmod(n)
          __Verify__
          sut.p should equal (p)
          sut.digit should === (expected)
        }
      }

      "create UIntMersenneModuloNumber object by a positive Long parameter" in {
        val conversions = Table(
          ("p", "n"),
          (3, 0), // p=3 => mod 7
          (3, 1),
          (3, 2),
          (3, 3),
          (3, 4),
          (3, 5),
          (3, 6),

          (3, 7), // <=  111
          (3, 8), // <= 1000 = 001 mmod 3
          (3, 9), // <= 1001 = 010 mmod 3
          (3, 10),
          (3, 11),
          (3, 12),
          (3, 13),

          (3, 14),
          (3, 15),

          (13, 13),
          (19, 524308)
        )

        forAll(conversions) { (p: Int, n: Int) =>
          __SetUp__
          val mmod = UIntMersenneModulo(p)
          val expected = UInt(n % (2 ** p - 1))
          __Exercise__
          val sut = mmod(n.toLong)
          __Verify__
          sut.p should equal(p)
          sut.digit should ===(expected)
        }
      }

      "create UIntMersenneModuloNumber object by a negative Long parameter" in {
        val conversions = Table(
          ("p", "n"),
          (3, -1),
          (3, -2),
          (3, -3),
          (3, -4),
          (3, -5),
          (3, -6),
          (3, -7)
        )

        forAll(conversions) { (p: Int, n: Int) =>
          __SetUp__
          val mmod = UIntMersenneModulo(p) // for p = 3, n = -2
          val mn = 2L**p-1L                // mn = 2^3-1 = 7
          val e = mn - (-n) % mn           // e = 7-(2%7) = 5
          val expected = if(e == mn) UInt(0) else UInt(e)
          __Exercise__
          val sut = mmod(n.toLong)
          __Verify__
          sut.p should equal (p)
          sut.digit should === (expected)
        }
      }
    }
  }

  "MersenneModuloUInt class" - {

    "toInt method should" - {

      "return an Int representation of this object" in {
        val conversions = Table(
          ("p", "n"),
          (3, 0),
          (3, 1),
          (3, 2),
          (3, 3),
          (3, 4),
          (3, 5),
          (3, 6),
          (3, 7),
          (3, 8),
          (3, 9)
        )

        forAll(conversions){ (p: Int, n: Int) =>
          __SetUp__
          val mmod = MersenneModulo(p)
          val m = mmod(n)
          val expected = n % (2**p-1)
          __Exercise__
          val sut = m.toInt
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "unary_- method should" - {

      "return the additive-inverse element of the argument" in {
        val conversions = Table(
          ("p", "n"),
          (3, 0),
          (3, 1),
          (3, 2),
          (3, 3),
          (3, 4),
          (3, 5),
          (3, 6),

          (3, 7),
          (3, 8),
          (3, 9)
        )

        forAll(conversions){ (p: Int, n: Int) =>
          __SetUp__
          val mmod = MersenneModulo(p)
          val m = mmod(n)
          val expected = mmod(2**p-1 - n)
          __Exercise__
          val sut = -m
          __Verify__
          sut should equal (expected)
        }
      }

      "return zero when added by the operand" in {
        val conversions = Table(
          ("p", "n"),
          (3, 0),
          (3, 1),
          (3, 2),
          (3, 3),
          (3, 4),
          (3, 5),
          (3, 6),
          (3, 7),
          (3, 8),
          (3, 9)
        )

        forAll(conversions){ (p: Int, n: Int) =>
          __SetUp__
          val mmod = MersenneModulo(p)
          val m = mmod(n)
          val expected = mmod(0)
          __Exercise__
          val sut = -m
          __Verify__
          (sut+m) should equal (expected)
        }
      }
    }

    "+ operator should" - {

      "return the sum of this and the argument" in {
        val conversions = Table(
          ("p", "x", "y"),
          (3, 0, 2),
          (3, 3, 0),
          (3, 1, 2),
          (3, 5, 4),
          (3, 2, 6),
          (3, 3, 4),

          (5, 15, 19)
        )

        forAll(conversions){ (p: Int, x: Int, y: Int) =>
          __SetUp__
          val mmod = MersenneModulo(p)
          val (xm, ym, expected) = (mmod(x), mmod(y), mmod(x+y))
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
          (3, 0, 2),
          (3, 3, 0),
          (3, 1, 2),
          (3, 5, 4),
          (3, 2, 6),
          (3, 3, 4),

          (5, 15, 19)
        )

        forAll(conversions){ (p: Int, x: Int, y: Int) =>
          __SetUp__
          val mmod = MersenneModulo(p)
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
          (3, 0, 2),
          (3, 3, 0),
          (3, 1, 2),
          (3, 5, 4),
          (3, 2, 6),
          (3, 3, 4),

          (5, 15, 19)
        )

        forAll(conversions){ (p: Int, x: Int, y: Int) =>
          __SetUp__
          val mmod = MersenneModulo(p)
          val (xm, ym, expected) = (mmod(x), mmod(y), mmod(x*y))
          __Exercise__
          val sut = xm * ym
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "Methods of Any" - {

      val mmod = MersenneModulo(3)
      val m = mmod(4)

      val conversions = Table(
        ("x", "y", "expected"),
        (m, m, true),
        (m, mmod(4), true),
        (m, mmod(11), true),
        (m, mmod(-3), true),
        (m, mmod(3), false),

        (mmod(0), mmod.Zero, true),
        (m, mmod.Zero, false),
        (m, mmod(0), false)
      )

      "equals method should" - {

        "work well" in {

          forAll(conversions){ (x: MersenneModuloNumber, y: MersenneModuloNumber, expected: Boolean) =>
            __Exercise__
            val sut = x == y
            __Verify__
            sut should equal (expected)
          }
        }
      }

      "hashCode method should" - {

        "work well" in {

          forAll(conversions){ (x: MersenneModuloNumber, y: MersenneModuloNumber, expected: Boolean) =>
            if(expected){
              __Exercise__
              val sut = x.hashCode == y.hashCode
              __Verify__
              sut should equal (expected)
            }
          }
        }
      }
    }
  }
}
