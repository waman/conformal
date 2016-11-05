package org.waman.conformal.number.integer

import org.waman.conformal.ConformalCustomSpec

class MersenneModuloSpec extends ConformalCustomSpec{

  "MersenneModulo class" - {

    "apply method should" - {

      "create MersenneModuloNumber object" in {
        val conversions = Table(
          ("n", "p", "expected"),
          (0, 3, Seq(0, 0, 0)),  // p=3 => mod 7
          (1, 3, Seq(1, 0, 0)),
          (2, 3, Seq(0, 1, 0)),
          (3, 3, Seq(1, 1, 0)),
          (4, 3, Seq(0, 0, 1)),
          (5, 3, Seq(1, 0, 1)),
          (6, 3, Seq(0, 1, 1)),

          (7, 3, Seq(0, 0, 0)),  // <=  111
          (8, 3, Seq(1, 0, 0)),  // <= 1000 = 001 mmod 3
          (9, 3, Seq(0, 1, 0)),   // <= 1001 = 010 mmod 3
          (10, 3, Seq(1, 1, 0)),
          (11, 3, Seq(0, 0, 1)),
          (12, 3, Seq(1, 0, 1)),
          (13, 3, Seq(0, 1, 1)),

          (14, 3, Seq(0, 0, 0)),
          (15, 3, Seq(1, 0, 0)),

          (13, 13, Seq(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
          (524308, 19, Seq(1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        )

        forAll(conversions){ (n: Int, p: Int, exp: Seq[Int]) =>
          __SetUp__
          val mmod = MersenneModulo(p)
          val expected = MersenneModuloNumber(exp.map(_ == 1), p)
          __Exercise__
          val sut = mmod(n)
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }

  "MersenneModuloNumber class" - {

    "toInt method should" - {

      "return an Int representation of this object" in {
        val conversions = Table(
          ("n", "expected"),
          (0, 0),
          (1, 1),
          (2, 2),
          (3, 3),
          (4, 4),
          (5, 5),
          (6, 6),
          (7, 0),
          (8, 1),
          (9, 2)
        )

        forAll(conversions){ (n: Int, expected: Int) =>
          __SetUp__
          val mmod = MersenneModulo(3)
          val m = mmod(n)
          __Exercise__
          val sut = m.toInt
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }
}
