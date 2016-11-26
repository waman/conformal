package org.waman.conformal.number.integer.mod

import org.waman.conformal.number.integer.ScalaIntegralNumber
import spire.math.Integral

import scala.math.{ScalaNumber, ScalaNumericConversions}

@SerialVersionUID(0L)
abstract class ModularNumber
    extends ScalaIntegralNumber with Serializable{ lhs =>

  def modulus: Modulus
  def modulusAs[I: Integral]: I = modulus.valueAs[I]

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

  //***** Methods of Any *****
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

  override def toString: String = s"$valueAsString (mod ${modulus.valueAsString})"
  protected def valueAsString: String
}