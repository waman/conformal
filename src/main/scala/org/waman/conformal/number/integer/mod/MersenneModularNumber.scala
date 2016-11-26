package org.waman.conformal.number.integer.mod

import spire.math.Integral

abstract class MersenneModularNumber extends ModularNumber{ lhs =>

  def modulus: MersenneModulus
  def p: Int = modulus.p
  private def mask = modulus.mask

  private def matchExponentsOfModules(rhs: MersenneModularNumber): Unit =
    require(lhs.p == rhs.p,
      s"Modulus of two numbers must be the same value: 2^${lhs.p}-1 and 2^${rhs.p}-1")

  override def unary_- : MersenneModularNumber =
    if(isZero) this
    else       calculateNegate

  override protected def calculateNegate: MersenneModularNumber

  def +(rhs: MersenneModularNumber): MersenneModularNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero) rhs
    else if (rhs.isZero) lhs
    else calculateSum(rhs)
  }

  protected def calculateSum(rhs: ModularNumber): MersenneModularNumber

  def -(rhs: MersenneModularNumber): MersenneModularNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero) -rhs
    else if (rhs.isZero) lhs
    else calculateDifference(rhs)
  }

  protected def calculateDifference(rhs: ModularNumber): MersenneModularNumber

  def *(rhs: MersenneModularNumber): MersenneModularNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero | rhs.isOne) lhs
    else if (lhs.isOne | rhs.isZero) rhs
    else calculateProduct(rhs)
  }

  protected def calculateProduct(rhs: ModularNumber): MersenneModularNumber

  //********** Methods of Any **********
  override def toString: String = s"$valueAsString (mod 2^$p-1)"
}