package org.waman.conformal.function.poly

import spire.ClassTag
import spire.algebra.{Eq, Field}
import spire.math.Polynomial

trait SpecialPolynomial{

  def degree(n: Int): Int

  def coeffsArray[C: Eq: ClassTag](n: Int)(implicit f: Field[C]): Array[C] = {
    val d = data(n).withDefaultValue(f.zero)
    (0 to degree(n)).map(d).toArray
  }

  def data[C: Eq: ClassTag](n: Int)(implicit f: Field[C]): Map[Int, C]

  def apply[C: Eq: ClassTag](n: Int)(implicit f: Field[C]): Polynomial[C] = {
    require(n >= 0)
    Polynomial(data(n))
  }
}