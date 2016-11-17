package org.waman.conformal.number.integer

import spire.math.Integral

package object mod {

  implicit class ConformalModularNumber[I: Integral](n: I){

    def mod[J: Integral](modulus: J)(implicit ms: ModulusSupplier): ModularNumber =
      ms(modulus).apply(n)
  }

  //********** Modulo **********
  implicit object ModulusCache extends ModulusSupplier
}
