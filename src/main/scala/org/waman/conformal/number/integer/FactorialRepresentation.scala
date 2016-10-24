package org.waman.conformal.number.integer

import spire.implicits._
import spire.math.Integral

import scala.annotation.tailrec
import scala.collection.LinearSeq
import scala.{specialized => spec}

class FactorialRepresentation private (val coefficientsInDescendant: Seq[Int]) {

  def order: Int = coefficientsInDescendant.length
  def coefficient(n: Int): Int =  coefficientsInDescendant(order - n)

  def coefficientsInAscendant : Seq[Int] = coefficientsInDescendant.reverse

  def coefficientsAsNthOrderInDescendant(n: Int): Seq[Int] =
    Seq.fill(n - order)(0) ++: coefficientsInDescendant

  def coefficientsAsNthOrderInAscendant(n: Int): Seq[Int] =
    coefficientsAsNthOrderInDescendant(n).reverse

  coefficientsInAscendant.zipWithIndex.foreach{ case (c_i, i) => // the order of each term is i+1
    require(0 <= c_i && c_i <= i+1,
      s"The ${i}th coefficient (the ${order - i}th element of argument) must be in a range [0, $i] (inclusive)")
  }

  //***** Algebraic Operation *****
//  def +(other: FactorialRepresentation): FactorialRepresentation = ???
//  def -(other: FactorialRepresentation): FactorialRepresentation = ???
//
//  def next: FactorialRepresentation = this + FactorialRepresentation.One
//  def previous: FactorialRepresentation = this - FactorialRepresentation.One

  //***** Conversions to other types *****
  def toVal[@spec(Int, Long) I: Integral]: I = {
    @tailrec
    def toVal(accum: I, cs: Seq[Int], n: Int): I = n match {
      case 0 => accum
      case _ => toVal((accum + cs.head) * n, cs.tail, n-1)
    }

    toVal(0, coefficientsInDescendant, order)
  }

  def toInt: Int = toVal[Int]
  def toLong: Long = toVal[Long]

  //***** Methods of Any *****
  override def toString: String = coefficientsInDescendant match {
    case Nil => "0!*0"
    case _ =>
      coefficientsInDescendant
        .zipWithIndex
        .map{ case (c_i, n_i) => (c_i, order-n_i)}
        .map{ case (c_i, i) => s"$i!*$c_i" }
        .mkString(" + ")
  }

  override def equals(other: scala.Any): Boolean = other match {
    case that: FactorialRepresentation =>
      that.canEqual(this) &&
        coefficientsInDescendant == that.coefficientsInDescendant
  }

  // TODO: equality to integral values
  def canEqual(that: Any): Boolean = that.isInstanceOf[FactorialRepresentation]

  override def hashCode(): Int = coefficientsInDescendant.##
}

object FactorialRepresentation{

  //***** Constants *****
  val Zero = fromInt(0)
  val One = fromInt(1)

  /**
    * descendant
    * arg type is List (not Seq) to distinguish apply(Int*)
    */
  def apply(coefficients: LinearSeq[Int]): FactorialRepresentation = {
    val canonical = coefficients.dropWhile(_ == 0)
    new FactorialRepresentation(canonical)
  }

  def apply(coefficients: IndexedSeq[Int]): FactorialRepresentation = {
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
