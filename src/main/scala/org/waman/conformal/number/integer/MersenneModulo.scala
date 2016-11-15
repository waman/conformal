package org.waman.conformal.number.integer

import spire.implicits._
import spire.math.Integral

case class MersenneModulo(p: Int){

  require(p >= 0, s"p must be zero or positive: $p")

  def modulus: MersenneNumber = MersenneNumber(p)
  def modulusIn[I: Integral]: I = modulus.toVal[I]

  private[integer] val mask = BigInt(2)**p - 1
  // For p = 3 :
  //   mask = 00...0111
  //  ~mask = 11...1000

  def apply(n: Int): MersenneModuloNumber = apply(n.toBigInt)
  def apply(n: Long): MersenneModuloNumber = apply(n.toBigInt)

  def apply(n: BigInt): MersenneModuloNumber =
    if(n < 0) -apply(-n)
    else {
      def separateByPBits(n: BigInt): Stream[BigInt] = n & ~mask match {
        case x if x == 0 =>
          if(n == 0 | n == mask)
            Stream(0)
          else
            Stream(n)
        case x =>
          (n & mask) #:: separateByPBits(n >> p)
      }

      separateByPBits(n).map(MersenneModuloNumber(_, this)).reduce(_ + _)
    }

  def apply(s: String): MersenneModuloNumber = apply(BigInt(s))
}

case class MersenneModuloNumber private[integer](value: BigInt, mersenneModulo: MersenneModulo){ lhs =>

  def p: Int = mersenneModulo.p
  private def mask = mersenneModulo.mask

  /** Create MersenneModuloNumber object with the row value */
  private def create(n: BigInt): MersenneModuloNumber = MersenneModuloNumber(n, mersenneModulo)
  /** Create MersenneModuloNumber object with the value modified
    * (available even when more p-bit BigInt value) */
  private def build(n: BigInt): MersenneModuloNumber = mersenneModulo(n)

  def isZero: Boolean = value == BigInt(0)
  def isOne : Boolean = value == BigInt(1)

  def unary_- : MersenneModuloNumber =
    if(isZero) this
    else       calculateNegate

  protected def calculateNegate: MersenneModuloNumber = create(value ^
    mask)

  def +(rhs: MersenneModuloNumber): MersenneModuloNumber = {
    require(lhs.p == rhs.p,
      s"Modulus of two numbers must be the same value: ${lhs.p} and ${rhs.p}")
    if (lhs.isZero) rhs
    else if (rhs.isZero) lhs
    else calculateSum(rhs)
  }

  protected def calculateSum(rhs: MersenneModuloNumber): MersenneModuloNumber
   = build(lhs.value + rhs.value)

  def -(rhs: MersenneModuloNumber): MersenneModuloNumber = {
    require(lhs.p == rhs.p,
      s"Modulus of two numbers must be the same value: ${lhs.p} and ${rhs.p}")
    if (lhs.isZero) -rhs
    else if (rhs.isZero) lhs
    else calculateSum(rhs.calculateNegate)
  }

  def *(rhs: MersenneModuloNumber): MersenneModuloNumber = {
    require(lhs.p == rhs.p,
      s"Modulus of two numbers must be the same value: ${lhs.p} and ${rhs.p}")
    if (lhs.isZero | rhs.isOne) lhs
    else if (lhs.isOne | rhs.isZero) rhs
    else calculateProduct(rhs)
  }

  protected def calculateProduct(rhs: MersenneModuloNumber): MersenneModuloNumber =
    build(lhs.value * rhs.value)

  def toVal[I: Integral]: I = implicitly[Integral[I]].fromBigInt(value)
  def toInt: Int = value.toInt
  def toLong: Long = value.toLong
  def toBigInt: BigInt = value

  override def toString: String = s"$value (mod 2^$p-1)"
}