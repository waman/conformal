package org.waman.conformal.number.integer

import org.waman.conformal.ForImplementationInterest
import spire.implicits._
import spire.math.Integral

/**
  * This class can be instantiated by p of a composite number.
  * Note that "MersenneNumber(2) == 3" return false (the == method of MersenneNumber is not overridden) .
  */
case class MersenneNumber(p: Int){

  def toVal[I: Integral]: I = implicitly[Integral[I]].fromInt(2)**p - 1
  lazy val toBigInt: BigInt = toVal[BigInt]

  /** Lucas-Lehmer test */
  def isPrime: Boolean = p match {
    case 0 | 1 => false
    case 2 => true
    case _ =>
      val m = toBigInt
      val seq = Stream.iterate[BigInt](4)(x => (x * x - 2) % m)
      seq(p - 2) == 0
  }

  @ForImplementationInterest
  private[integer]
  def isPrime2: Boolean = p match {
    case 0 | 1 => false
    case 2 => true
    case _ =>
      val mmod = MersenneModulo(p)
      val v2 = mmod(2)
      val xs: Seq[MersenneModuloNumber] = Stream.iterate(mmod(4))(x => x*x - v2)
      xs(p-2).isZero
  }
}