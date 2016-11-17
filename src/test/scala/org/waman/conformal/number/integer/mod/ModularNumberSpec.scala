package org.waman.conformal.number.integer.mod

import org.waman.conformal.ConformalCustomSpec
import spire.implicits._

class ModularNumberSpec extends ConformalCustomSpec{

  //    "toInt method should" - {
  //
  //      "return an Int representation of this object" in {
  //        val conversions = Table(
  //          ("p", "n"),
  //          (7, 0),
  //          (7, 1),
  //          (7, 2),
  //          (7, 3),
  //          (7, 4),
  //          (7, 5),
  //          (7, 6),
  //          (7, 7),
  //          (7, 8),
  //          (7, 9)
  //        )
  //
  //        forAll(conversions){ (p: Int, n: Int) =>
  //          __SetUp__
  //          val mmod = Modulo(p)
  //          val m = mmod(n)
  //          val expected = n % (2**p-1)
  //          __Exercise__
  //          val sut = m.toInt
  //          __Verify__
  //          sut should equal (expected)
  //        }
  //      }
  //    }

  "unary_- method should" - {

    "return the additive-inverse element of the argument" in {
      val conversions = Table(
        ("p", "n"),
        (7, 0),
        (7, 1),
        (7, 2),
        (7, 3),
        (7, 4),
        (7, 5),
        (7, 6),

        (7, 7),
        (7, 8),
        (7, 9)
      )

      forAll(conversions){ (p: Int, n: Int) =>
        __SetUp__
        val mmod = Modulus(p)
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
        (7, 0),
        (7, 1),
        (7, 2),
        (7, 3),
        (7, 4),
        (7, 5),
        (7, 6),
        (7, 7),
        (7, 8),
        (7, 9)
      )

      forAll(conversions){ (p: Int, n: Int) =>
        __SetUp__
        val mmod = Modulus(p)
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

  "Methods of Any" - {

    val m = Modulus(7)
    val n = m(3)

    "equals() method should" - {

      "return true when the arg is the equivalent value even if they are the different objects" in {
        val conversions = Table(
          ("x", "y", "expected"),
          (n, n, true),
          (n, Modulus(7).apply(3), true),
          (n, Modulus(11).apply(3), false),
          (n, Modulus(7).apply(4), false),
          (n, Modulus(11).apply(4), false)
        )

        forAll(conversions){ (x: ModularNumber, y: ModularNumber, expected: Boolean) =>
          __Exercise__
          val sut = x == y
          __Verify__
          sut should be (expected)
        }
      }

      "return false when two Modulo objects have the same value of module but integral types are different" in {
        __SetUp__
        val x = Modulus(7).apply(3)
        val y = Modulus(7L).apply(3L)
        __Exercise__
        val sut = x == y
        __Verify__
        sut should be (false)
      }
    }

    "hashCode method should" - {

      "return the same value when the two objects are the equivalent value even if different objects" in {
        val conversions = Table(
          ("x", "y"),
          (n, n),
          (n, Modulus(7).apply(3)),
          (Modulus(11).apply(5), Modulus(11).apply(5))
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
