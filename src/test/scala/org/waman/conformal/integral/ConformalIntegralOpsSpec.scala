package org.waman.conformal.integral

import org.waman.conformal.integral.ConformalIntegralOps._
import org.waman.conformal.ConformalCustomSpec

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
}
