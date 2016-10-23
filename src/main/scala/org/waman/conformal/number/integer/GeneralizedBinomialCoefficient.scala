package org.waman.conformal.number.integer

import spire.math._
import spire.implicits._

object GeneralizedBinomialCoefficient {

  def apply[I: Integral](n: I, r: I): I =
    if(r < 0)0
    else{
      if(n >= 0){
        if(r > n) 0
        else BinomialCoefficient(n, r)
      }else{
        val sign = if(r % 2 == 0) 1 else -1
        sign * BinomialCoefficient(-n+r-1, r)
      }
    }
}
