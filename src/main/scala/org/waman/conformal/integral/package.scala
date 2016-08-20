package org.waman.conformal

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

  def gcd[I: Integral](m: I, n: I): I = n match {
    case 0 => m
    case _ => gcd(n, m % n)
  }

  // For implementation interest
  private[integral]
  def gcd1[I: Integral](m: I, n: I): I = {
    require(m > 0 && n > 0)
    if(m == 1 || n == 1) return 1

    @tailrec
    def gcd1(m: I, n: I, d: I): I = (m % 2, n % 2) match {
      case (0, 0) => gcd1(m /~ 2, n /~ 2, d * 2)
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

    gcd1(m, n, 1)
  }

  def ngcd[I: Integral](m: I, ns: I*): I = ns match {
    case _ if m == 1 => m
    case Nil => m
    case n +: rest => ngcd[I](gcd[I](m, n), rest:_*)
  }

  def lcm[I: Integral](m: I, n: I): I = (m /~ gcd(m, n)) * n
}
