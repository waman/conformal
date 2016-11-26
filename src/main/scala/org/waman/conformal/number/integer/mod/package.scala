package org.waman.conformal.number.integer

import spire.math.Integral

package object mod {

  implicit class ConformalModularNumber[I: Integral](n: I){

    def mod[J: Integral](modulus: J)(implicit ms: ModulusSupplier): ModularNumber =
      ms(modulus).apply(n)

    /** This method is equivalent to mod() method (defined to be usable to BigInt) */
    def modulo[J: Integral](modulus: J)(implicit ms: ModulusSupplier): ModularNumber =
      mod(modulus)

    def mmod(modulus: Int)(implicit ms: ModulusSupplier): MersenneModularNumber =
      ms.mersenne(modulus).apply(n)

    def ≡(m: I): ResultOfCongruence[I] = equiv(m)
    def equiv(m: I): ResultOfCongruence[I] = new ResultOfCongruence(n, m)
  }

  //********** Modulo **********
  implicit object ModulusCache extends ModulusSupplier
}
