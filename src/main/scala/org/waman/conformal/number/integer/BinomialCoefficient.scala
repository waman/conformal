package org.waman.conformal.number.integer

import spire.math.{Integral, Rational}
import spire.implicits._

import scala.annotation.tailrec

object BinomialCoefficient{

  def apply[I: Integral](n: I, r: I): I = {
    require(n >= 0, s"n must be non-negative: $n")
    require(0 <= r && r <= n, s"r must be in 0 <= r <= $n")

    val s = if(n-r < r) n-r else r
    if(s == 0) return 1
    if(s == 1) return n

    // The following 2 lines can be written by iterate() methods of Vector/Seq class,
    // but toInt method of I object must be called.
    val nums = Stream.iterate[I](n-r+1)(_+1).takeWhile(_ <= n).toVector
    val denos = Stream.iterate[I](2)(_+1).takeWhile(_ <= r)

    reduceIntegralFraction(nums, denos)
  }

  // For implementation interest
  private[integer]
  def binomialCoefficient1(n: Int, r: Int): Int =
    if(r == 0 || r == n) 1
    else binomialCoefficient1(n-1, r-1) + binomialCoefficient1(n-1, r)

  // For implementation interest
  private[integer]
  def binomialCoefficient2(n: Int, r: Int): Int = {
    val s = if(n-r < r) n-r else r

    def add(v0: Vector[Int], v1: Vector[Int]): Vector[Int] =
      v0.zip(v1).map(p => p._1 + p._2)

    @tailrec
    def binomialCoefficient(accum: Vector[Int], i: Int): Vector[Int] = i match {
      case 0 => accum
      case _ => binomialCoefficient(add(0 +: accum, accum :+ 0), i-1)
    }

    binomialCoefficient(Vector(1), n)(s)
  }

  // For implementation interest
  private[integer]
  def binomialCoefficient3(n: Int, r: Int): Int = {
    val s = if(n-r < r) n-r else r

    def add(v0: Vector[Int], v1: Vector[Int]): Vector[Int] =
      v0.zip(v1).map(p => p._1 + p._2)

    @tailrec
    def binomialCoefficient(accum: Vector[Int], i: Int): Vector[Int] = i match {
      case 0 =>
        accum
      case _ if i <= s =>
        binomialCoefficient(add(accum.init, accum.tail), i-1)
      case _ if i < n-s =>
        binomialCoefficient(add(0 +: accum.init, accum), i-1)
      case _ =>
        binomialCoefficient(add(0 +: accum, accum :+ 0), i-1)
    }

    binomialCoefficient(Vector(1), n).head
  }

  // For implementation interest
  private[integer]
  def binomialCoefficient4[I: Integral](n: I, r: I): I = {
    require(n >= 0, s"n must be non-negative: $n")
    require(0 <= r && r <= n, s"r must be in 0 <= r <= $n")

    val s = if(n-r < r) n-r else r

    // 10C3 = (10/3) * (9/2) * (8/1)
    @tailrec
    def binomialCoefficient(accum: Rational, n: I, r: I): Rational =
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

        binomialCoefficient(accum * f, n-1, r-1)
    }

    s match {
      case 0 => 1
      case 1 => n
      case _ =>
        val result = binomialCoefficient(1, n, s)
        implicitly[Integral[I]].fromRational(result)
    }
  }
}