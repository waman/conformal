package org.waman.conformal.integral

import org.waman.conformal.integral.combinatorial.{Combination, Permutation}
import spire.math.Integral

import scala.language.implicitConversions

import org.waman.conformal.{integral => intpack}

trait ConformalIntegralOps{

  implicit def convertIntegralToIntegralOps[I: Integral](i: I): ConformalIntegral[I] =
    new ConformalIntegral(i)
}

object ConformalIntegralOps extends ConformalIntegralOps

class ConformalIntegral[I: Integral](n: I){
  val ! : I = factorial(n)
  def P(r: I): I = Permutation.permutationCount(n, r)
  def C(r: I): I = Combination.combinationCount(n, r)

  def gcd(m: I): I = intpack.gcd(n, m)
  def lcm(m: I): I = intpack.lcm(n, m)
}
