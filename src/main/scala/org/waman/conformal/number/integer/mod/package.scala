package org.waman.conformal.number.integer

import spire.math.Integral

package object mod {

  implicit class ConformalModularNumber[I: Integral](n: I){

    /** This method is equivalent to mod() method (defined to be usable to BigInt) */
//    def modulo[J: Integral](m: J): ModularNumber = ModularNumber(n, m.toBigInt)  // StackOverflow
    def modulo(m: Int): ModularNumber = ModularNumber(n, m)
    def modulo(m: Long): ModularNumber = ModularNumber(n, m)
    def modulo(m: BigInt): ModularNumber = ModularNumber(n, m)

    /** ~% is equivalent to modulo (mod). */
    final def ~%(m: Int): ModularNumber = modulo(m)
    final def ~%(m: Long): ModularNumber = modulo(m)
    final def ~%(m: BigInt): ModularNumber = modulo(m)

//    def mmod(modulus: Int): MersenneModularNumber =
//      ms.mersenne(modulus).apply(n)

    def ≡(m: I): ResultOfCongruence[I] = equiv(m)
    def equiv(m: I): ResultOfCongruence[I] = new ResultOfCongruence(n, m)
  }
}
