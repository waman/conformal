package org.waman.conformal.number.integer.mod

import org.waman.conformal.memoize
import spire.math.Integral


abstract class ModulusSupplier{

  private lazy val cache: Any => Modulus = memoize{
    case n: Int => Modulus(n)
    case n: Long => Modulus(n)
    case n: BigInt => Modulus(n)
  }

  def apply[I: Integral](m: I): Modulus = cache(m)

  private lazy val mCache: Int => MersenneModulus = memoize(MersenneModulus(_))

  def mersenne(p: Int): MersenneModulus = mCache(p)
}