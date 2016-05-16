package org.waman.conformal.algebra

case class PolynomialOnGF2(bit: Int){

  def this(s: String) = this(Integer.valueOf(s, 2))
}

class FiniteField(val order: Int){

}

object FiniteField{

  def apply(order: Int): FiniteField = new FiniteField(order)
}