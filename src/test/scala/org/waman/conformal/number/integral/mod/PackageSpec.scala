package org.waman.conformal.number.integral.mod

import org.waman.conformal.ConformalCustomSpec

class PackageSpec extends ConformalCustomSpec{

  "mod method should" - {

    "create ModularNumber object" in {
      __Exercise__
      val sut = 13 ~% 7
      __Verify__
      sut should be (a [ModularNumber])
      sut.modulusAs[Int] should equal (7)
      sut.valueAs[Int] should equal (6)
    }
  }

  "equiv (≡) method should" - {

    "evaluate congruence with the argument in mathematical notation" in {
      __Exercise__
      val sut = 7 ≡ 13 mod 6
      __Verify__
      sut should be (true)
    }

    "evaluate congruence with the argument" in {
      val conversions = Table(
        ("x", "y", "m", "expected"),
        (7, 13, 6, true),
        (13, 7, 6, true),
        (21, 10, 5, false)
      )

      forAll(conversions){ (x: Int, y: Int, m: Int, expected: Boolean) =>
        __Exercise__
        val sut = x ≡ y mod m
        __Verify__
        sut should be (expected)
      }
    }
  }
}
