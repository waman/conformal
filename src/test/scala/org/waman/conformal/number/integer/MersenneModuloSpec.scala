package org.waman.conformal.number.integer

import org.waman.conformal.ConformalCustomSpec
import spire.implicits._

class MersenneModuloSpec extends ConformalCustomSpec{

  "MersenneModulo class" - {

    "apply method should" - {

      "create MersenneModuloNumber object" in {
        val conversions = Table(
          ("p", "n", "expected"),
          (3, 0, Seq(0, 0, 0)),  // p=3 => mod 7
          (3, 1, Seq(1, 0, 0)),
          (3, 2, Seq(0, 1, 0)),
          (3, 3, Seq(1, 1, 0)),
          (3, 4, Seq(0, 0, 1)),
          (3, 5, Seq(1, 0, 1)),
          (3, 6, Seq(0, 1, 1)),

          (3, 7, Seq(0, 0, 0)),  // <=  111
          (3, 8, Seq(1, 0, 0)),  // <= 1000 = 001 mmod 3
          (3, 9, Seq(0, 1, 0)),   // <= 1001 = 010 mmod 3
          (3, 10, Seq(1, 1, 0)),
          (3, 11, Seq(0, 0, 1)),
          (3, 12, Seq(1, 0, 1)),
          (3, 13, Seq(0, 1, 1)),

          (3, 14, Seq(0, 0, 0)),
          (3, 15, Seq(1, 0, 0)),

          (3, -1, Seq(0, 1, 1)),
          (3, -2, Seq(1, 0, 1)),
          (3, -3, Seq(0, 0, 1)),
          (3, -4, Seq(1, 1, 0)),
          (3, -5, Seq(0, 1, 0)),
          (3, -6, Seq(1, 0, 0)),
          (3, -7, Seq(0, 0, 0)),

          (13, 13, Seq(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
          (19, 524308, Seq(1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        )

        forAll(conversions){ (p: Int, n: Int, e: Seq[Int]) =>
          __SetUp__
          val mmod = MersenneModulo(p)
          val expected = e.map(_ == 1)
          __Exercise__
          val sut = mmod(n)
          __Verify__
          sut.p should equal (p)
          sut.bits should equal (expected)
        }
      }
    }
  }

  "MersenneModuloNumber class" - {

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
