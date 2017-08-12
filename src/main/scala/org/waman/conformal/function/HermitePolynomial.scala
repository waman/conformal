package org.waman.conformal.function

import spire.ClassTag
import spire.math._
import spire.algebra._
import spire.implicits._

import scala.annotation.tailrec

object HermitePolynomial {

  def apply[C: Eq: ClassTag](n: Int)(implicit f: Field[C]): Polynomial[C] = {
    require(n >= 0)
    n match {
      case 0 => Polynomial.one[C]
      case 1 => Polynomial.twox[C]
      case _ =>
        @tailrec
        def apply(data: Map[Int, C], c: C, m: Int): Polynomial[C] =
          if(m < 0) {
            Polynomial(data)
          }else {
            val c_m: C = -c * (m+2) * (m+1) / (2 * (n - m))
            apply(data + (m -> c_m), c_m, m - 2)
          }

        val c = f.fromInt(2)**n
        apply(Map(n -> c), c, n-2)
    }
  }
}
