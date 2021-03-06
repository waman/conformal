package org.waman.conformal.number.integral.mod

import org.waman.conformal.ForImplementationInterest
import spire.implicits._
import spire.math.{Integral, UInt}

import scala.annotation.tailrec

object UIntMersenneNumber{

  @ForImplementationInterest
  private[integral]
  def isPrime(p: Int): Boolean = p match {
    case 0 | 1 => false
    case 2 => true
    case _ =>
      val mmod = UIntMersenneModulus(p)
      val v2 = mmod(2)
      val xs: Seq[MersenneModularUInt] = Stream.iterate(mmod(4))(x => x*x - v2)
      xs(p-2).isZero
  }
}

case class UIntMersenneModulus(p: Int){

  require(0 <= p && p <= 32)

  def modulusIn[I: Integral] = implicitly[Integral[I]].fromInt(2)**p - 1
  def modulus: Long = modulusIn[Long]

  private[integral] val mask64 = 2L**p-1L
  private[integral] val mask32 = UInt(mask64.toInt)
    // For p = 3 :
    //  mask = 00...0111
    // ~mask = 11...1000

  def fromUInt(n: UInt): MersenneModularUInt = {
    @tailrec
    def mod(n: UInt): UInt = n & ~mask32 match {
      case x if x == UInt(0) =>
        if(n == mask32) UInt(0) else n
      case x => mod((n & mask32) + (n >>> p))
    }

    MersenneModularUInt(mod(n), this)
  }

  def apply(n: Int): MersenneModularUInt =
    if(n < 0) -fromUInt(UInt(-n))
    else       fromUInt(UInt(n))

  def apply(n: Long): MersenneModularUInt = n match {
    case _ if n < 0L => -apply(-n)
    case _ =>
      @tailrec
      def mod(n: Long): Long = n & ~mask64 match {
        case 0L =>
          if(n == mask64) 0L else n
        case x => mod((n & mask64) + (n >>> p))
      }

      MersenneModularUInt(UInt(mod(n)), this)
  }
}

/**
  * The bit property is in ascending order (head is 2**0 digit).
  * This constructor must be used with Seq[Boolean] whose length is p.
  */
case class MersenneModularUInt private[integral](digit: UInt, mersenneModulo: UIntMersenneModulus){

  def p: Int = mersenneModulo.p

  def isZero: Boolean = this.digit == UInt(0)
  def isOne : Boolean = this.digit == UInt(1)

  def unary_- : MersenneModularUInt =
    if(this.isZero) this
    else            MersenneModularUInt(mersenneModulo.mask32 & ~digit, mersenneModulo)

  def +(that: MersenneModularUInt): MersenneModularUInt =
    if(this.isZero) that
    else if(that.isZero) this
    else calculateSum(that)

  private def calculateSum(that: MersenneModularUInt): MersenneModularUInt =
    this.mersenneModulo(this.digit.toLong + that.digit.toLong)

  def -(that: MersenneModularUInt): MersenneModularUInt =
    if(this.isZero) -that
    else if(that.isZero) this
    else this + (-that)

  def *(that: MersenneModularUInt): MersenneModularUInt =
    if(this.isZero | that.isOne) this
    else if(this.isOne | that.isZero) that
    else calculateProduct(that)

  private def calculateProduct(that: MersenneModularUInt): MersenneModularUInt =
    this.mersenneModulo(this.digit.toLong * that.digit.toLong)

  def toInt: Int = this.digit.toInt
  def toLong: Long = this.digit.toLong
  def toBigInt: BigInt = this.digit.toBigInt
  override def toString: String = s"$digit (mod 2^$p-1)"
}