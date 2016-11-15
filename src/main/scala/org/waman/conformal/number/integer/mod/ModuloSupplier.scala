package org.waman.conformal.number.integer.mod

import spire.math.Integral
import org.waman.conformal.memoize

abstract class ModuloSupplier[I: Integral]{

  private lazy val cache: I => Modulo[I] = memoize(Modulo(_))

  def apply(m: I): Modulo[I] = cache(m)
}