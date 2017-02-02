package org.waman.conformal.number.integral.mod

import org.waman.conformal.number.integral.{BigIntSpireIntegralNumber, MersenneNumber}
import spire.implicits._
import spire.math.{Integral, Numeric}

// TODO Rewrite arithemetic operations by bit operations
class MersenneModularNumber private(val value: BigInt, val p: Int, val modulus: BigInt)
    extends ModularNumber with BigIntSpireIntegralNumber{ lhs =>

  def this(value: BigInt, p: Int) = this(value, p, BigInt(2)**p - 1)

  override def modulusAs[A](implicit a: Numeric[A]) = a.fromBigInt(modulus)
  def modulusAsMersenneNumber: MersenneNumber = MersenneNumber(p)

  override def bigIntValue = value

  override def isZero = value.isZero
  override def isOne  = value.isOne

  private def create(n: BigInt) = new MersenneModularNumber(n, p, modulus)

  private def build(n: BigInt): MersenneModularNumber =
    n & ~modulus match {
      case x if x == 0 => create(n)
      case _ => build((n & modulus) + (n >> p))
    }

  private def matchExponentsOfModules(rhs: MersenneModularNumber): Unit =
    require(lhs.p == rhs.p,
      s"Modulus of two numbers must be the same value: 2^${lhs.p}-1 and 2^${rhs.p}-1")

  override def unary_- : MersenneModularNumber =
    if(isZero) this
    else       calculateNegate

  override protected def calculateNegate = create(value ^ modulus)

  def +(rhs: MersenneModularNumber): MersenneModularNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero) rhs
    else if (rhs.isZero) lhs
    else calculateSum(rhs)
  }

  override protected def calculateSum(rhs: ModularNumber): MersenneModularNumber =
    build(lhs.bigIntValue + rhs.bigIntValue)

  def -(rhs: MersenneModularNumber): MersenneModularNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero) -rhs
    else if (rhs.isZero) lhs
    else calculateDifference(rhs)
  }

  override protected def calculateDifference(rhs: ModularNumber): MersenneModularNumber =
    lhs.bigIntValue - rhs.bigIntValue match {
      case x if x >= 0 => create(x)
      case x           => create(x + modulus)
    }

    def *(rhs: MersenneModularNumber): MersenneModularNumber = {
    matchExponentsOfModules(rhs)
    if (lhs.isZero | rhs.isOne) lhs
    else if (lhs.isOne | rhs.isZero) rhs
    else calculateProduct(rhs)
  }

  override protected def calculateProduct(rhs: ModularNumber): MersenneModularNumber =
    build(lhs.bigIntValue * rhs.bigIntValue)

  override protected def hasTheSameModulusAs(rhs: ModularNumber) =
    rhs match {
      case that: MersenneModularNumber => p == that.p
      case _ => lhs.modulus == rhs.modulusAs[BigInt]
    }

  override protected def hasTheSameValueAs(that: ModularNumber) =
    value == that.bigIntValue

  override def hashCode = (value, p).##
  override def toString = s"$value (mod 2^$p-1)"
}

object MersenneModularNumber{

  //********** apply() factory methods **********
  def apply[I: Integral](n: I, p: Int): MersenneModularNumber = {
    require(p > 1, s"Modulus must be greater than 1: $p")

    val value: BigInt = n match {  // because Integral.toBigInt does not work well
      case i: Int => BigInt(i)
      case i: Long => BigInt(i)
      case i: BigInt => i
      case _ => n.toBigInt
    }

    new MersenneModularNumber(value mod (BigInt(2)**p-1), p)
  }
}