package org.waman.conformal.algebra

import org.waman.conformal.ConformalCustomSpec

class SymmetricGroupSpec extends ConformalCustomSpec{

  "order property should" - {

    "return the number of elements" in {
      val conversions =
        Table(
          ("degree", "order"),
          (1, BigInt(1)),
          (2, BigInt(2)),
          (3, BigInt(6)),
          (4, BigInt(24)),
          (5, BigInt(120))
        )

      forAll(conversions){ (degree: Int, order: BigInt)  =>
        __SetUp__
        val sg = SymmetricGroup(degree)
        __Exercise__
        val sut = sg.order
        __Verify__
        sut should equal (order)
      }
    }
  }
}
