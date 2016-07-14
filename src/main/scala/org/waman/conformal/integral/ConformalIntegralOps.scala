package org.waman.conformal.integral

import org.waman.conformal.integral.combinatorial.{Combination, Permutation}
import spire.math.Integral

import scala.language.implicitConversions

trait ConformalIntegralOps{

  implicit def convertIntegralToIntegralOps[I: Integral](i: I): ConformalIntegral[I] =
    new ConformalIntegral(i)
}

object ConformalIntegralOps extends ConformalIntegralOps

class ConformalIntegral[I: Integral](n: I){
  val ! : I = factorial(n)
  def P(r: I): I = Permutation.permutationCount(n, r)
  def C(r: I): I = Combination.combinationCount(n, r)
}
