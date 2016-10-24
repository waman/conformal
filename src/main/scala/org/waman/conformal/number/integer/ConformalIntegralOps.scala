package org.waman.conformal.number.integer

import org.waman.conformal.number.integer.combinatorics.{Combination, Permutation, WithRepetition}
import org.waman.conformal.number.{integer => intpack}
import spire.math.Integral

import scala.language.implicitConversions

trait ConformalIntegralOps{

  implicit def convertIntegralToIntegralOps[I: Integral](i: I): ConformalIntegral[I] =
    new ConformalIntegral(i)
}

object ConformalIntegralOps extends ConformalIntegralOps

class ConformalIntegral[I: Integral](n: I){

  def |/|(divisor: I) : I = intpack.euclideanDivide(n, divisor)
  def |%|(divisor: I) : I = intpack.euclideanRemainder(n, divisor)

  def ! : I = factorial(n)
  def !! : I = doubleFactorial(n)
  def P(r: I): I = Permutation.permutationCount(n, r)
  def C(r: I): I = Combination.combinationCount(n, r)
  def H(r: I): I = WithRepetition.combinationCount(n, r)

  def gcd(m: I): I = intpack.gcd(n, m)
  def lcm(m: I): I = intpack.lcm(n, m)
}
