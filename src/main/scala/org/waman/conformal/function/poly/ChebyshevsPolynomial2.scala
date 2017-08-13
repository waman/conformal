package org.waman.conformal.function.poly

import spire.ClassTag
import spire.algebra._
import spire.implicits._
import spire.math._

import scala.annotation.tailrec

object ChebyshevsPolynomial2 extends SpecialPolynomial{

  override def degree(n: Int): Int = n

  // For efficiency
  override def apply[C: Eq : ClassTag](n: Int)(implicit f: Field[C]): Polynomial[C] =
    n match{
      case 0 => Polynomial.one
      case 1 => Polynomial.twox
      case _ => super.apply(n)
    }

  override def data[C: Eq: ClassTag](n: Int)(implicit f: Field[C]): Map[Int, C] = {
    @tailrec
    def data(cMap: Map[Int, C], c: C, m: Int): Map[Int, C] =
      if(m < 0) {
        cMap
      }else{
        val c_m: C = -c * (m+2) * (m+1) / ((n-m) * (n+m+2))
        data(cMap + (m -> c_m), c_m, m-2)
      }

    val c: C = f.fromInt(2)**n
    data(Map(n -> c), c, n-2)
  }
}
