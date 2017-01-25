package org.waman.conformal.number.integral.mod

import spire.math.Integral
import spire.implicits._

class ResultOfCongruence[I: Integral](n0: I, n1: I) {

  def mod(m: I): Boolean = (n0 - n1) % m == 0
}
