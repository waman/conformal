//package org.waman.conformal.number.integer.mod
//
//import org.waman.conformal.ConformalCustomSpec
//
//class MersenneModulusSpec extends ConformalCustomSpec{
//
//  "apply method should" - {
//
//    "create MersenneModularNumber object" in {
//      val conversions = Table(
//        ("p", "n", "expected"),
//        (3, 0, 0),  // p=3 => mod 7
//        (3, 1, 1),
//        (3, 2, 2),
//        (3, 3, 3),
//        (3, 4, 4),
//        (3, 5, 5),
//        (3, 6, 6),
//
//        (3, 7, 0),
//        (3, 8, 1),
//        (3, 9, 2),
//        (3, 10, 3),
//        (3, 11, 4),
//        (3, 12, 5),
//        (3, 13, 6),
//
//        (3, 14, 0),
//        (3, 15, 1),
//
//        (3, -1, 6),
//        (3, -2, 5),
//        (3, -3, 4),
//        (3, -4, 3),
//        (3, -5, 2),
//        (3, -6, 1),
//        (3, -7, 0),
//
//        (13, 13, 13),
//        (19, 524308, 21)
//      )
//
//      forAll(conversions){ (p: Int, n: Int, expected: Int) =>
//        __Exercise__
//        val sut = n mmod p
//        __Verify__
//        sut.p should equal (p)
//        sut.toInt should equal (expected)
//      }
//    }
//  }
//}
