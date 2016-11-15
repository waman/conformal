package org.waman.conformal.number.integer.mod

import spire.math.Integral

class MersenneModuloNumber private[integer](val value: BigInt, val modulo: MersenneModulo)
  extends ModuloNumber[BigInt]{ lhs =>

  def p: Int = modulo.p
  private def mask = modulo.mask

  /** Create MersenneModuloNumber object with row value */
  override protected def create(n: BigInt): MersenneModuloNumber = new MersenneModuloNumber(n, modulo)
  /** Create MersenneModuloNumber object with the value modified
    * (available even when more p-bit BigInt value) */
  override protected def build(n: BigInt): MersenneModuloNumber = modulo(n)

  private def matchExponentsOfModules(rhs: MersenneModuloNumber): Unit =
    require(lhs.p == rhs.p,
      s"Modulus of two numbers must be the same value: 2^${lhs.p}-1 and 2^${rhs.p}-1")

  override def isZero: Boolean = value == BigInt(0)
  override def isOne : Boolean = value == BigInt(1)

  override def unary_- : MersenneModuloNumber =
    if(isZero) this
    else       calculateNegate

  override protected def calculateNegate: MersenneModuloNumber =
    create(value ^ mask)

  def +(rhs: MersenneModuloNumber): MersenneModuloNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero) rhs
    else if (rhs.isZero) lhs
    else calculateSum(rhs.value)
  }

  private def calculateSum(rhs: BigInt): MersenneModuloNumber
  = build(lhs.value + rhs)

  def -(rhs: MersenneModuloNumber): MersenneModuloNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero) -rhs
    else if (rhs.isZero) lhs
    else calculateSum(rhs.calculateNegate.value)
  }

  def *(rhs: MersenneModuloNumber): MersenneModuloNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero | rhs.isOne) lhs
    else if (lhs.isOne | rhs.isZero) rhs
    else calculateProduct(rhs.value)
  }

  private def calculateProduct(rhs: BigInt): MersenneModuloNumber =
    build(lhs.value * rhs)

  //*********** Methods Inherited from ModuloNumber **********
  override protected def calculateSum(rhs: ModuloNumber[BigInt]) =
  calculateSum(rhs.value)

  override protected def calculateProduct(rhs: ModuloNumber[BigInt]) =
    calculateProduct(rhs.value)

  //********** Type Conversions **********
  def toVal[I: Integral]: I = implicitly[Integral[I]].fromBigInt(value)
  def toInt: Int = value.toInt
  def toLong: Long = value.toLong
  def toBigInt: BigInt = value

  override def toString: String = s"$value (mod 2^$p-1)"
}