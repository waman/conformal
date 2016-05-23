package org.waman.conformal.integral

import spire.math.Integral
import spire.implicits._

import scala.annotation.tailrec
import scala.{specialized => spec}

case class FactorialRepresentation(private val coefficients: Seq[Int]) {

  def this(cHead: Int, cTail: Int*) = this(cHead +: cTail)

  def order: Int = coefficients.length
  def coefficient(n: Int): Int =  coefficients(order - n)

  (order to 1 by -1).foreach { i =>
    val c_i = coefficient(i)
    require(0 <= c_i && c_i <= i,
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
}

object FactorialRepresentation{

  def fromInt(i: Int): FactorialRepresentation = {
    @tailrec
    def divide(cs: Seq[Int], dividend: Int, divisor: Int): Seq[Int] = dividend match {
      case 0 => cs
      case _ => divide((dividend % divisor) +: cs, dividend / divisor, divisor + 1)
    }

    val cs = divide(Nil, i, 2)
    new FactorialRepresentation(cs)
  }
}
