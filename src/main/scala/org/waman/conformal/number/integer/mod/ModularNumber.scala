package org.waman.conformal.number.integer.mod

import spire.math.Integral

trait ModularNumber{ lhs =>

  def modulus: Modulus
  def modulusAs[I: Integral]: I = modulus.valueAs[I]
  def valueAs[I: Integral]: I

//  /** Create ModuloNumber object with the row value */
//  protected def create(value: I): ModuloNumber
//  /** Create ModuloNumber object with the value modified */
//  protected def build(value: I): ModuloNumber

  def isZero: Boolean
  def isOne : Boolean

  protected def matchModules(rhs: ModularNumber): Unit =
    require(lhs.modulus == rhs.modulus,
      s"Modulus of two numbers must be the same value: ${lhs.modulus} and ${rhs.modulus}")

  def unary_- : ModularNumber =
    if(isZero) this
    else       calculateNegate

  protected def calculateNegate: ModularNumber

  def +(rhs: ModularNumber): ModularNumber = {
    matchModules(rhs)
    if (lhs.isZero) rhs
    else if (rhs.isZero) lhs
    else calculateSum(rhs)
  }

  protected def calculateSum(rhs: ModularNumber): ModularNumber

  def -(rhs: ModularNumber): ModularNumber = {
    matchModules(rhs)
    if (lhs.isZero) -rhs
    else if (rhs.isZero) lhs
    else calculateDifference(rhs)
  }

  protected def calculateDifference(rhs: ModularNumber): ModularNumber

  def *(rhs: ModularNumber): ModularNumber = {
    matchModules(rhs)
    if (lhs.isZero | rhs.isOne) lhs
    else if (lhs.isOne | rhs.isZero) rhs
    else calculateProduct(rhs)
  }

  protected def calculateProduct(rhs: ModularNumber): ModularNumber

  //  def toInt: Int = value.toInt
  //  def toLong: Long = lhs.toLong
  //  def toBigInt: BigInt = lhs.toBigInt

  override def equals(other: Any): Boolean =
    other match {
      case that: ModularNumber =>
        that.canEqual(this) &&
          this.modulus == that.modulus &&
            hasTheSameValueAs(that)
      case _ => false
    }

  protected def hasTheSameValueAs(that: ModularNumber): Boolean

  def canEqual(other: Any): Boolean = other.isInstanceOf[ModularNumber]

  override def hashCode: Int

  override def toString: String = s"$valueAsString (mod $modulus)"

  protected def valueAsString: String
}