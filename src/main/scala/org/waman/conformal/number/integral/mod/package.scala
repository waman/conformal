package org.waman.conformal.number.integral

import spire.math.Integral
import spire.implicits._

package object mod {

  implicit class ConformalModularNumber[I: Integral](n: I){

    /** This method is equivalent to mod() method (defined to be usable to BigInt) */
    def modulo(m: Int): ModularNumber = ModularNumber(n, m)
    def modulo(m: Long): ModularNumber = ModularNumber(n, m)
    def modulo(m: BigInt): ModularNumber = ModularNumber(n, m)

    /** ~% is equivalent to modulo (mod). */
    final def ~%(m: Int): ModularNumber = modulo(m)
    final def ~%(m: Long): ModularNumber = modulo(m)
    final def ~%(m: BigInt): ModularNumber = modulo(m)

    def mmod(p: Int): MersenneModularNumber = n match {
      case i: Int => MersenneModularNumber(BigInt(i), p)
      case i: Long => MersenneModularNumber(BigInt(i), p)
      case i: BigInt => MersenneModularNumber(i, p)
      case _ => MersenneModularNumber(n.toBigInt, p)
    }

    /** ~%^^ is equivalent to mmod (mod 2**p-1). */
    final def ~%^(p: Int): MersenneModularNumber = mmod(p)

    def ≡(m: I): ResultOfCongruence[I] = equiv(m)
    def equiv(m: I): ResultOfCongruence[I] = new ResultOfCongruence(n, m)
  }
}
