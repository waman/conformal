package org.waman.conformal.integral

import spire.math.{Integral, Rational}
import spire.implicits._

import scala.annotation.tailrec

object BinomialCoefficient{

  def apply[I: Integral](n: I, r: I): I = {
    require(n >= 0, s"n must be non-negative: $n")
    require(0 <= r && r <= n, s"r must be in 0 <= r <= $n")

    val s = if(2*r > n) n-r else r

    // 10C3 = (10/3) * (9/2) * (8/1)
    @tailrec
    def apply[J: Integral](accum: Rational, n: J, r: J): Rational =
      r match {
        case 0 => accum
        case _ =>
          // This matching is due to bugs of spire 0.10.1, 0.11.0
          val f = r match {
            case i: Int    => Rational(n.toInt, i)
            case i: Long   => Rational(n.toLong, i)
            case i: BigInt => Rational(n.toBigInt, i)
            case _         => n.toRational / r.toRational
          }

          apply(accum * f, n-1, r-1)
      }

    s match {
      case 0 => 1
      case 1 => n
      case _ =>
        val result = apply(1, n, s)
        implicitly[Integral[I]].fromRational(result)
    }
  }

  // For implementation interest
  private[integral]
  def binomialCoefficient1(n: Int, r: Int): Int =
    if(r == 0 || r == n) 1
    else binomialCoefficient1(n-1, r-1) + binomialCoefficient1(n-1, r)

  // For implementation interest
  private[integral]
  def binomialCoefficient2(n: Int, r: Int): Int = {
    val s = if(2*r > n) n-r else r

    def add(v0: Vector[Int], v1: Vector[Int]): Vector[Int] =
      v0.zip(v1).map(p => p._1 + p._2)

    @tailrec
    def combinationCount(accum: Vector[Int], i: Int): Vector[Int] = i match {
      case 0 =>
        accum
      case _ if i <= s =>
        combinationCount(add(accum.init, accum.tail), i-1)
      case _ if i < n-s =>
        combinationCount(add(0 +: accum.init, accum), i-1)
      case _ =>
        combinationCount(add(0 +: accum, accum :+ 0), i-1)
    }

    combinationCount(Vector(1), n).head
  }
}
