package org.waman.conformal.number.integer.mod

import org.waman.conformal.ConformalCustomSpec

class PackageSpec extends ConformalCustomSpec{

  "ConformalModulus" - {

    "mod method should" - {

      "create ModularNumber object" in {
        __Exercise__
        val sut = 13 mod 7
        __Verify__
        sut should be (a [ModularNumber])
        sut.modulusAs[Int] should equal (7)
        sut.valueAs[Int] should equal (6)
      }

      "memoize Modulo object" in {
        __SetUp__
        val x = 3 mod 11
        val y = 5 mod 11
        __Exercise__
        val sut = x.modulus eq y.modulus
        __Verify__
        sut should be (true)
      }
    }
  }
}
