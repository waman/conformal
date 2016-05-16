package org.waman.conformal.integral

import spire.math.Integral
import spire.implicits._

import scala.annotation.tailrec
import scala.language.implicitConversions

trait ConformalIntegralOps{

  implicit def convertIntegralToIntegralOps[I: Integral](i: I): ConformalIntegral[I] =
    new ConformalIntegral(i)

  def factorial[I: Integral](i: I): I = factorial(1, i)

  @tailrec
  private def factorial[I: Integral](prod: I, n: I): I = n match {
    case 0 => prod
    case _ => factorial(prod * n, n-1)
  }
}

object ConformalIntegralOps extends ConformalIntegralOps

class ConformalIntegral[I: Integral](i: I){
  val ! : I = ConformalIntegralOps.factorial(i)
}
