package org.waman.conformal.integral

import org.waman.conformal.ConformalCustomSpec

import org.waman.conformal.integral.ConformalIntegralOps._

import scala.language.postfixOps

class ConformalIntegralOpsSpec extends ConformalCustomSpec{

  "! should" - {

    "return factorial of an integer" in {
      val conversions =
        Table(
          ("n", "expected"),
          (0, 1),
          (1, 1),
          (2, 2),
          (3, 6),
          (4, 24),
          (5, 120),
          (6, 720)
        )

      forAll(conversions){ (n: Int, expected: Int) =>
        __Exercise__
        val sut = n!;
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "P(Int) method should" - {

    "return nPr (permutation count)" - {
      val conversions = Table(
        ("n", "r", "expected"),
        (0, 0, 1),

        (1, 0, 1),
        (1, 1, 1),

        (3, 0, 1),
        (3, 1, 3),
        (3, 2, 6),
        (3, 3, 6),

        (5, 0, 1),
        (5, 1, 5),
        (5, 2, 20),
        (5, 3, 60),
        (5, 4, 120),
        (5, 5, 120)
      )

      forAll(conversions){ (n: Int, r: Int, expected: Int) =>
        __Exercise__
        val sut = n P r
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "C(Int) method should" - {

    "return nCr (combination count)" - {
      val conversions = Table(
        ("n", "r", "expected"),
        (0, 0, 1),

        (1, 0, 1),
        (1, 1, 1),

        (3, 0, 1),
        (3, 1, 3),
        (3, 2, 3),
        (3, 3, 1),

        (5, 0, 1),
        (5, 1, 5),
        (5, 2, 10),
        (5, 3, 10),
        (5, 4, 5),
        (5, 5, 1)
      )

      forAll(conversions){ (n: Int, r: Int, expected: Int) =>
        __Exercise__
        val sut = n C r
        __Verify__
        sut should equal (expected)
      }
    }
  }
}
