//package org.waman.conformal.number.integer.mod
//
//import org.waman.conformal.ConformalCustomSpec
//import spire.implicits._
//
//class MersenneModularNumberSpec extends ConformalCustomSpec{
//
//  "toInt method should" - {
//
//    "return an Int representation of this object" in {
//      val conversions = Table(
//        ("p", "n"),
//        (3, 0),
//        (3, 1),
//        (3, 2),
//        (3, 3),
//        (3, 4),
//        (3, 5),
//        (3, 6),
//        (3, 7),
//        (3, 8),
//        (3, 9)
//      )
//
//      forAll(conversions){ (p: Int, n: Int) =>
//        __SetUp__
//        val mmod = MersenneModulus(p)
//        val m = mmod(n)
//        val expected = n % (2**p-1)
//        __Exercise__
//        val sut = m.toInt
//        __Verify__
//        sut should equal (expected)
//      }
//    }
//  }
//
//  "unary_- method should" - {
//
//    "return the additive-inverse element of the argument" in {
//      val conversions = Table(
//        ("p", "n"),
//        (3, 0),
//        (3, 1),
//        (3, 2),
//        (3, 3),
//        (3, 4),
//        (3, 5),
//        (3, 6),
//
//        (3, 7),
//        (3, 8),
//        (3, 9)
//      )
//
//      forAll(conversions){ (p: Int, n: Int) =>
//        __SetUp__
//        val mmod = MersenneModulus(p)
//        val m = mmod(n)
//        val expected = mmod(2**p-1 - n)
//        __Exercise__
//        val sut = -m
//        __Verify__
//        sut should equal (expected)
//      }
//    }
//
//    "return zero when added by the operand" in {
//      val conversions = Table(
//        ("p", "n"),
//        (3, 0),
//        (3, 1),
//        (3, 2),
//        (3, 3),
//        (3, 4),
//        (3, 5),
//        (3, 6),
//        (3, 7),
//        (3, 8),
//        (3, 9)
//      )
//
//      forAll(conversions){ (p: Int, n: Int) =>
//        __SetUp__
//        val mmod = MersenneModulus(p)
//        val m = mmod(n)
//        val expected = mmod(0)
//        __Exercise__
//        val sut = -m
//        __Verify__
//        (sut+m) should equal (expected)
//      }
//    }
//  }
//
//  "+ operator should" - {
//
//    "return the sum of this and the argument" in {
//      val conversions = Table(
//        ("p", "x", "y"),
//        (3, 0, 2),
//        (3, 3, 0),
//        (3, 1, 2),
//        (3, 5, 4),
//        (3, 2, 6),
//        (3, 3, 4),
//
//        (5, 15, 19)
//      )
//
//      forAll(conversions){ (p: Int, x: Int, y: Int) =>
//        __SetUp__
//        val mmod = MersenneModulus(p)
//        val (xm, ym, expected) = (mmod(x), mmod(y), mmod(x+y))
//        __Exercise__
//        val sut = xm + ym
//        __Verify__
//        sut should equal (expected)
//      }
//    }
//  }
//
//  "- operator should" - {
//
//    "return the difference of the argument by this" in {
//      val conversions = Table(
//        ("p", "x", "y"),
//        (3, 0, 2),
//        (3, 3, 0),
//        (3, 1, 2),
//        (3, 5, 4),
//        (3, 2, 6),
//        (3, 3, 4),
//
//        (5, 15, 19)
//      )
//
//      forAll(conversions){ (p: Int, x: Int, y: Int) =>
//        __SetUp__
//        val mmod = MersenneModulus(p)
//        val (xm, ym, expected) = (mmod(x), mmod(y), mmod(x-y))
//        __Exercise__
//        val sut = xm - ym
//        __Verify__
//        sut should equal (expected)
//      }
//    }
//  }
//
//  "* operator should" - {
//
//    "return the product of this and the argument" in {
//      val conversions = Table(
//        ("p", "x", "y"),
//        (3, 0, 2),
//        (3, 3, 0),
//        (3, 1, 2),
//        (3, 5, 4),
//        (3, 2, 6),
//        (3, 3, 4),
//
//        (5, 15, 19)
//      )
//
//      forAll(conversions){ (p: Int, x: Int, y: Int) =>
//        __SetUp__
//        val mmod = MersenneModulus(p)
//        val (xm, ym, expected) = (mmod(x), mmod(y), mmod(x*y))
//        __Exercise__
//        val sut = xm * ym
//        __Verify__
//        sut should equal (expected)
//      }
//    }
//  }
//}
