package org.waman.conformal.integral

import org.waman.conformal.ConformalCustomSpec

class FactorialRepresentationSpec extends ConformalCustomSpec{

  "apply() factory method should" - {

    "throw Exception if some coefficient is out of the range" in {
      __Verify__
      an [IllegalArgumentException] should be thrownBy{
        FactorialRepresentation(3, 1)
      }
    }
  }

  "toInt method should" - {

    "convert a factorial representation value to an Int value" in {
      val conversions =
        Table(
          ("coefficients", "expected"),
          (Seq(), 0),
          (Seq(0), 0),
          (Seq(1), 1),
          (Seq(1, 2, 0, 1), 37)
        )

      forAll(conversions){ (cs: Seq[Int], expected: Int) =>
        __SetUp__
        val value = FactorialRepresentation(cs:_*)
        __Exercise__
        val sut = value.toInt
        __Verify__
        sut should equal (expected)
      }
    }
  }
}
