package org.waman.conformal.number.integer.mod

import org.waman.conformal.ConformalCustomSpec

class PackageSpec extends ConformalCustomSpec{

  "ConformalModulo" - {

    "mod method should" - {

      "create ModuloNumber object" in {
        __Exercise__
        val sut = 13 mod 7
        __Verify__
        sut should be (a [ModuloNumber[_]])
        sut.module should equal (7)
        sut.value should equal (6)
      }

      "memoize Modulo object" in {
        __SetUp__
        val x = 3 mod 11
        val y = 5 mod 11
        __Exercise__
        val sut = x.modulo eq y.modulo
        __Verify__
        sut should be (true)
      }
    }
  }
}
