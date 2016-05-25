package org.waman.conformal.integral

import spire.math.Integral
import spire.implicits._

import scala.annotation.tailrec
import scala.language.implicitConversions

trait ConformalIntegralOps{

  implicit def convertIntegralToIntegralOps[I: Integral](i: I): ConformalIntegral[I] =
    new ConformalIntegral(i)
}

object ConformalIntegralOps extends ConformalIntegralOps

class ConformalIntegral[I: Integral](i: I){
  val ! : I = factorial(i)
  def P(r: I): I = permutationCount(i, r)
}