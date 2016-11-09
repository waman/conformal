package org.waman.conformal.number.integer

import org.waman.conformal.ForImplementationInterest
import spire.implicits._
import spire.math.{Integral, UInt}

import scala.annotation.tailrec

object UIntMersenneNumber{

  @ForImplementationInterest
  private[integer]
  def isPrime(p: Int): Boolean = p match {
    case 2 => true
    case _ =>
      val mmod = UIntMersenneModulo(p)
      val v2 = mmod(2)
      val xs: Seq[MersenneModuloUInt] = Stream.iterate(mmod(4))(x => x*x - v2)
      xs(p-2).isZero
  }
}

case class UIntMersenneModulo(p: Int){

  require(p <= 32)

  def modulusOf[I: Integral] = implicitly[Integral[I]].fromInt(2)**p - 1
  def modulus: Long = modulusOf[Long]

  private[integer] val mask64 = 2L**p-1L
  private[integer] val mask32 = UInt(mask64.toInt)
    // For p = 3 :
    //  mask = 00...0111
    // ~mask = 11...1000

  def fromUInt(n: UInt): MersenneModuloUInt = {
    @tailrec
    def mod(n: UInt): UInt = n & ~mask32 match {
      case x if x == UInt(0) =>
        if(n == mask32) UInt(0) else n
      case x => mod((n & mask32) + (n >>> p))
    }

    MersenneModuloUInt(mod(n), this)
  }

  def apply(n: Int): MersenneModuloUInt =
    if(n < 0) -fromUInt(UInt(-n))
    else       fromUInt(UInt(n))

  def apply(n: Long): MersenneModuloUInt = n match {
    case _ if n < 0L => -apply(-n)
    case _ =>
      @tailrec
      def mod(n: Long): Long = n & ~mask64 match {
        case 0L =>
          if(n == mask64) 0L else n
        case x => mod((n & mask64) + (n >>> p))
      }

      MersenneModuloUInt(UInt(mod(n)), this)
  }
}

/**
  * The bit property is in ascending order (head is 2**0 digit).
  * This constructor must be used with Seq[Boolean] whose length is p.
  */
case class MersenneModuloUInt private[integer](digit: UInt, mersenneModulo: UIntMersenneModulo){

  def p: Int = mersenneModulo.p

  def isZero: Boolean = this.digit == UInt(0)
  def isOne : Boolean = this.digit == UInt(1)

  def unary_- : MersenneModuloUInt =
    if(this.isZero) this
    else            MersenneModuloUInt(mersenneModulo.mask32 & ~digit, mersenneModulo)

  def +(that: MersenneModuloUInt): MersenneModuloUInt =
    if(this.isZero) that
    else if(that.isZero) this
    else calculateSum(that)

  private def calculateSum(that: MersenneModuloUInt): MersenneModuloUInt =
    this.mersenneModulo(this.digit.toLong + that.digit.toLong)

  def -(that: MersenneModuloUInt): MersenneModuloUInt =
    if(this.isZero) -that
    else if(that.isZero) this
    else this + (-that)

  def *(that: MersenneModuloUInt): MersenneModuloUInt =
    if(this.isZero | that.isOne) this
    else if(this.isOne | that.isZero) that
    else calculateProduct(that)

  private def calculateProduct(that: MersenneModuloUInt): MersenneModuloUInt =
    this.mersenneModulo(this.digit.toLong * that.digit.toLong)

  def toInt: Int = this.digit.toInt
  def toLong: Long = this.digit.toLong
  def toBigInt: BigInt = this.digit.toBigInt
  override def toString: String = s"$digit (mod 2^$p-1)"
}