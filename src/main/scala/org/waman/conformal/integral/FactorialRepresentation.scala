package org.waman.conformal.integral

import spire.math.Integral
import spire.implicits._
import scala.{specialized => spec}

class FactorialRepresentation private(val coefficients: Seq[Int]) {

  def order: Int = coefficients.length

  def toVal[@spec(Int, Long) I: Integral]: I = order match {
    case 0 => 0
    case _ =>
      (order to 1 by -1)
        .zip(coefficients)
        .map{ case (n, c) => factorial(n) * c}.sum
  }

  def toInt: Int = toVal[Int]
  def toLong: Long = toVal[Long]


}

object FactorialRepresentation{

  def apply(cs: Int*): FactorialRepresentation = {
    val n = cs.length
    (n to 1 by -1).foreach{ i =>
      require((0 to i).contains(cs(n-i)),
        s"The ${i}th coefficient (the ${n-i}th element of argument) must be in a range [0, $i] (inclusive)")
    }

    new FactorialRepresentation(cs)
  }
}
