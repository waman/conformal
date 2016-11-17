package org.waman.conformal.number.integer.mod

import spire.math.Integral

class MersenneModularNumber private[integer](val modulus: MersenneModulus, val value: BigInt)
    extends ModularNumber{ lhs =>

  def p: Int = modulus.p
  private def mask = modulus.mask

  private def matchExponentsOfModules(rhs: MersenneModularNumber): Unit =
    require(lhs.p == rhs.p,
      s"Modulus of two numbers must be the same value: 2^${lhs.p}-1 and 2^${rhs.p}-1")

  override def isZero: Boolean = value == BigInt(0)
  override def isOne : Boolean = value == BigInt(1)

  override def unary_- : MersenneModularNumber =
    if(isZero) this
    else       calculateNegate

  override protected def calculateNegate: MersenneModularNumber =
    modulus.create(value ^ mask)

  def +(rhs: MersenneModularNumber): MersenneModularNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero) rhs
    else if (rhs.isZero) lhs
    else calculateSum(rhs.value)
  }

  private def calculateSum(rhs: BigInt): MersenneModularNumber
  = modulus(lhs.value + rhs)

  def -(rhs: MersenneModularNumber): MersenneModularNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero) -rhs
    else if (rhs.isZero) lhs
    else calculateDifference(rhs.value)
  }

  private def calculateDifference(rhs: BigInt): MersenneModularNumber =
    modulus(lhs.value - rhs)

  def *(rhs: MersenneModularNumber): MersenneModularNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero | rhs.isOne) lhs
    else if (lhs.isOne | rhs.isZero) rhs
    else calculateProduct(rhs.value)
  }

  private def calculateProduct(rhs: BigInt): MersenneModularNumber =
    modulus(lhs.value * rhs)

  //*********** Methods Inherited from ModuloNumber **********
  override protected def calculateSum(rhs: ModularNumber) =
    calculateSum(rhs.valueAs[BigInt])

  override protected def calculateDifference(rhs: ModularNumber) =
    calculateDifference(rhs.valueAs[BigInt])

  override protected def calculateProduct(rhs: ModularNumber) =
    calculateProduct(rhs.valueAs[BigInt])

  //********** Type Conversions **********
  def valueAs[I: Integral]: I = implicitly[Integral[I]].fromBigInt(value)
  def toInt: Int = value.toInt
  def toLong: Long = value.toLong
  def toBigInt: BigInt = value

  override protected def hasTheSameValueAs(that: ModularNumber) =
    this.value == that.valueAs[BigInt]

  override def hashCode = (modulus, value).##
  override protected def valueAsString = value.toString
  override def toString: String = s"$value (mod 2^$p-1)"
}