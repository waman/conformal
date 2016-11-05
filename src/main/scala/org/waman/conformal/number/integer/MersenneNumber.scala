package org.waman.conformal.number.integer

import org.waman.conformal.ForImplementationInterest
import spire.math.Integral
import spire.implicits._

import scala.annotation.tailrec

/**
  * This class can be instantiated by p of a composite number.
  * Note that "MersenneNumber(2) == 3" return false (the == method of MersenneNumber is not overridden) .
  */
case class MersenneNumber(p: Int){

  def toVal[I: Integral]: I = implicitly[Integral[I]].fromInt(2)**p - 1
  lazy val toBigInt: BigInt = toVal[BigInt]

  /** Lucas-Lehmer test */
  def isPrime: Boolean = p match {
    case 2 => true
    case _ =>
      val m = toBigInt
      val seq = Stream.iterate[BigInt](4)(x => (x ** 2 - 2) % m)
      seq(p - 2) == 0
  }

  @ForImplementationInterest
  private[integer]
  def lucasSequence: Seq[Seq[Int]] = {

    def modAt[E](v: Seq[E], n: Int): Seq[E] = {
      val (first, second) = v.splitAt(n)
      second ++: first
    }

    def addWithSlided(x: Seq[Int], y: Seq[Int], n: Int): Seq[Int] =
      (0 until p).map(i => x(i) + y((i+n)%p))

    def f(xs: Seq[Int]): Seq[Int] = {
      @tailrec
      def f(as: Seq[Int], xs: Seq[Int], i: Int): Seq[Int] = i match {
        case _ if i == p => as
        case _ =>
          xs(i) match {
            case x if x <= 0 => f(as, xs, i+1)
            case x =>
              val (result, s) = addWithSlided(xs, as, i)
                .foldLeft((Vector[Int](), 0)) { case ((bs, t), c) =>
                  val newT = t / 2 + c
                  (bs :+ (newT % 2), newT)
                }
              val newA = if(s > 1) Vector.fill(p)(0).updated(i, 1)
              else      result
              f(newA, xs, i+1)
          }
      }

      val as0 = Vector.fill(p)(1).updated(1, 0)  // as0= -2 mod Mp
      f(as0, xs, 0)
    }

    val xs0: Seq[Int] = Vector.fill(p)(0).updated(2, 1)  // xs0 = 4
    Stream.iterate(xs0)(f)
  }

  @ForImplementationInterest
  private[integer]
  def isPrime2: Boolean = p match {
    case 2 => true
    case _ =>
      val as = lucasSequence.apply(p-2)
      as.indexOf(as.head, 1) match {
        case -1 => false // TODO
        case i  => i == p
      }
  }
}