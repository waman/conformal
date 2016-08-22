package org.waman.conformal

import org.waman.conformal.integral.combinatorial.Combination
import spire.math.Integral
import spire.implicits._

import scala.language.implicitConversions
import scala.annotation.tailrec

package object integral {

  implicit def convertIntToFactorialRepresentation(i: Int): FactorialRepresentation =
    FactorialRepresentation.fromInt(i)

  implicit def convertFactorialRepresentationToInt(fr: FactorialRepresentation): Int =
    fr.toInt

  def factorial[I: Integral](i: I): I = {
    require(i >= 0, s"The argument must be non-negative: $i")

    @tailrec
    def factorial(prod: I, n: I): I = n match {
      case 0 | 1 => prod
      case _ => factorial(prod * n, n-1)
    }

    factorial(1, i)
  }

  // For implementation interest
  private[integral]
  def factorial1(i: Long): Long = i match {
    case 0L | 1L => 1L
    case _ => (2L to i).product
  }

  def doubleFactorial[I: Integral](i: I): I = {
    require(i >= 0, s"The argument must be non-negative: $i")

    @tailrec
    def doubleFactorial(prod: I, n: I): I = n match {
      case 0 | 1 => prod
      case _ => doubleFactorial(prod * n, n-2)
    }

    doubleFactorial(1, i)
  }

  // For implementation interest
  private[integral]
  def doubleFactorial1(i: Long): Long = i match {
    case 0L | 1L => 1L
    case _ => (i to 2L by -2L).product
  }

  @tailrec
  def gcd[I: Integral](m: I, n: I): I = n match {
    case 0 => m
    case _ => gcd(n, m % n)
  }

  def nGCD[I: Integral](seq: Seq[I]): I = {
    require(seq.nonEmpty)
    nGCD(seq.head, seq.tail:_*)
  }

  def nGCD[I: Integral](m: I, ns: I*): I = m match {
    case 1 => m
    case _ =>
      ns match {
        case Nil => m
        case n +: rest => nGCD[I](gcd[I](m, n), rest:_*)
      }
  }

  // For implementation interest
  private[integral]
  def gcd1[I: Integral](m: I, n: I): I = {
    require(m > 0 && n > 0)
    if(m == 1 || n == 1) return 1

    @tailrec
    def gcd_(m: I, n: I, d: I): I = (m % 2, n % 2) match {
      case (0, 0) => gcd_(m /~ 2, n /~ 2, d * 2)
      case _ =>
        @tailrec
        def gcd1_(u: I, v: I, t: I): I = t match {
          case 0 => u * d
          case _ =>
            @tailrec
            def dropPrimeFactor2(t: I): I = t.abs % 2 match {
              case 0 => dropPrimeFactor2(t /~ 2)
              case _ => t
            }

            dropPrimeFactor2(t) match {
              case s if s > 0 => gcd1_(s,  v, s - v)
              case s          => gcd1_(u, -s, u + s)
            }
        }

        val t = if(m % 2 == 0) m else -n
        gcd1_(m, n, t)
    }

    gcd_(m, n, 1)
  }

  def lcm[I: Integral](m: I, n: I): I = (m /~ gcd(m, n)) * n

  def nLCM[I: Integral](seq: Seq[I]): I = {
    require(seq.nonEmpty)

    val gcdCombs = (1 to seq.length)
      .map(Combination.allCombinations(seq, _).map(nGCD(_)))

    def toFactorizedFraction(seq: Seq[Seq[I]]): (Seq[I], Seq[I]) = {
      val patched = if(seq.length % 2 == 0) seq else seq :+ Nil
      val flatten = patched.grouped(2).toSeq.transpose.map(_.flatten)
      (flatten.head, flatten(1))
    }

    val frac = toFactorizedFraction(gcdCombs)
    reduceIntegralFraction(frac._1, frac._2)
  }

  def nLCM[I: Integral](m: I, ns: I*): I = nLCM(m +: ns)

  def reduceIntegralFraction[I: Integral](nFactors: Seq[I], dFactors: Seq[I]): I = {

    @tailrec
    def reduceByFactors(nFactors: Seq[I], dFactors: Seq[I]): Seq[I] = dFactors match {
      case Nil => nFactors
      case dHead +: dTail =>
        @tailrec // reduce i-th factor of the numerator
        def reduceByFactor(nFactors: Seq[I], d: I, i: Int): Seq[I] = d match {
          case 1 => nFactors
          case _ =>
            nFactors(i) match {
              case 1 => reduceByFactor(nFactors, d, i+1)
              case nFactor =>
                gcd(nFactor, d) match {
                  case 1 => reduceByFactor(nFactors, d, i+1)
                  case g => reduceByFactor(nFactors.updated(i, nFactor/~g), d/~g, i+1)
                }
            }
        }

        reduceByFactors(reduceByFactor(nFactors, dHead, 0), dTail)
    }

    reduceByFactors(nFactors, dFactors)
      .filter(_ != 1)
      .reduceLeft( (b, i) => b * i )
  }
}
