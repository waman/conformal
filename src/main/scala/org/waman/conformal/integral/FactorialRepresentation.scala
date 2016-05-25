package org.waman.conformal.integral

import spire.math.Integral
import spire.implicits._

import scala.annotation.tailrec
import scala.{specialized => spec}

class FactorialRepresentation private (private val coefficients: List[Int] /* descendant */) {

  def order: Int = coefficients.length
  def coefficient(n: Int): Int =  coefficients(order - n)

  def coefficientsInDescendant: List[Int] = coefficients
  def coefficientsInAscendant : List[Int] = coefficients.reverse

  def coefficientsInDescendantWithFixedLength(n: Int): List[Int] =
    List.fill(n - order)(0) ++: coefficientsInDescendant

  def coefficientsInAscendantWithFixedLength(n: Int): List[Int] =
    coefficientsInDescendantWithFixedLength(n).reverse

  coefficientsInAscendant.zipWithIndex.foreach{ case (c_i, i) => // the order of each term is i+1
    require(0 <= c_i && c_i <= i+1,
      s"The ${i}th coefficient (the ${order - i}th element of argument) must be in a range [0, $i] (inclusive)")
  }

  def toVal[@spec(Int, Long) I: Integral]: I = {
    @tailrec
    def toVal(accum: I, cs: Seq[Int], n: Int): I = n match {
      case 0 => accum
      case _ => toVal((accum + cs.head) * n, cs.tail, n-1)
    }

    toVal(0, coefficients, order)
  }

  def toInt: Int = toVal[Int]
  def toLong: Long = toVal[Long]

  //***** Methods of Any *****
  override def toString: String =
    coefficients
      .zipWithIndex
      .map{ case (c_i, n_i) => (c_i, order-n_i)}
      .map{ case (c_i, i) => s"$i!*$c_i" }
      .mkString(" + ")

  override def equals(other: scala.Any): Boolean = other match {
    case that: FactorialRepresentation =>
      that.canEqual(this) &&
        coefficients == that.coefficients
  }

  def canEqual(that: Any): Boolean = that.isInstanceOf[FactorialRepresentation]

  override def hashCode(): Int = coefficients.hashCode
}

object FactorialRepresentation{

  /** descendant */
  def apply(coefficients: List[Int]): FactorialRepresentation = {
    val canonical = coefficients.dropWhile(_ == 0)
    new FactorialRepresentation(canonical)
  }

  /** descendant */
  def apply(coefficient: Int*): FactorialRepresentation = apply(coefficient.toList)

  def fromInt(i: Int): FactorialRepresentation = {
    @tailrec
    def divide(cs: List[Int], dividend: Int, divisor: Int): List[Int] = dividend match {
      case 0 => cs
      case _ => divide((dividend % divisor) +: cs, dividend / divisor, divisor + 1)
    }

    val cs = divide(Nil, i, 2)
    apply(cs)
  }
}
