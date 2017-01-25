package org.waman.conformal.number.integral.mod

import org.waman.conformal.number.integral._
import spire.implicits._
import spire.math.{Integral, UInt, ULong}

@SerialVersionUID(0L)
abstract class ModularNumber
    extends SpireIntegralNumber with Serializable{ lhs =>

  def valueAs[I: Integral]: I
  def modulusAs[I: Integral]: I

  protected def matchModulus(rhs: ModularNumber): Unit =
    require(hasTheSameModulusAs(rhs),
      s"Modulus of two numbers must be the same value: " +
        s"${lhs.modulusAs[BigInt]} and ${rhs.modulusAs[BigInt]}")

  def unary_- : ModularNumber =
    if(isZero) this
    else       calculateNegate

  protected def calculateNegate: ModularNumber

  def +(rhs: ModularNumber): ModularNumber = {
    matchModulus(rhs)
    if (lhs.isZero) rhs
    else if (rhs.isZero) lhs
    else calculateSum(rhs)
  }

  protected def calculateSum(rhs: ModularNumber): ModularNumber

  def -(rhs: ModularNumber): ModularNumber = {
    matchModulus(rhs)
    if (lhs.isZero) -rhs
    else if (rhs.isZero) lhs
    else calculateDifference(rhs)
  }

  protected def calculateDifference(rhs: ModularNumber): ModularNumber

  def *(rhs: ModularNumber): ModularNumber = {
    matchModulus(rhs)
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
          hasTheSameModulusAs(that) &&
            hasTheSameValueAs(that)
      case _ => false
    }

  protected def hasTheSameModulusAs(that: ModularNumber): Boolean
  protected def hasTheSameValueAs(that: ModularNumber): Boolean

  def canEqual(other: Any): Boolean = other.isInstanceOf[ModularNumber]

  override def hashCode: Int
  override def toString = s"${valueAs[BigInt]} (mod ${modulusAs[BigInt]})"
}

object ModularNumber{

  //********** apply() factory methods **********
  def apply[I: Integral](n: I, m: Int): ModularNumber = {
    require(m > 1, s"Modulus must be greater than 1: $m")
    val value = n mod m
    new ModularInt(value.toInt, m)
  }

  def apply[I: Integral](n: I, m: Long): ModularNumber = {
    if(m <= Int.MaxValue){
      apply(n, m.toInt)
    }else{
      require(m > 1, s"Modulus must be greater than 1: $m")

      val value: Long = n match {
        case i: Int => i.toLong mod m
        case i: Long => i mod m
        case i: BigInt => (i mod BigInt(m)).toLong
        case _ => (n.toBigInt mod BigInt(m)).toLong
      }
      new ModularLong(value, m)
    }
  }

  def apply[I: Integral](n: I, m: BigInt): ModularNumber = {
    if(m <= Int.MaxValue) apply(n, m.toInt)
    else if(m <= Long.MaxValue) apply(n, m.toLong)
    else{
      require(m > 1, s"Modulus must be greater than 1: $m")
      val value = n match {  // because Integral.toBigInt does not work well
        case i: Int => BigInt(i)
        case i: Long => BigInt(i)
        case i: BigInt => i
        case _ => n.toBigInt
      }
      new ModularBigInt(value mod m, m)
    }
  }

  //***** ModuloNumbers *****
  private[mod] abstract class ModularNumberAdapter[I: Integral] extends ModularNumber{

    def modulus: I
    def value: I

    override def hashCode = (value, modulus).##
    override def toString = s"$value (mod $modulus)"
  }

  private[mod] class ModularInt(val intValue: Int, val modulus: Int)
      extends ModularNumberAdapter[Int] with IntSpireIntegralNumber{ lhs =>

    override def valueAs[I: Integral] = implicitly[Integral[I]].fromInt(intValue)
    override def modulusAs[I: Integral] = implicitly[Integral[I]].fromInt(modulus)

    private def create(n: Int) = new ModularInt(n, modulus)

    override protected def calculateNegate = create(modulus - intValue)

    override protected def calculateSum(rhs: ModularNumber) = {
      val sum = (UInt(lhs.toInt) + UInt(rhs.toInt)) % UInt(modulus)
      create(sum.toInt)
    }

    override protected def calculateDifference(rhs: ModularNumber) =
      lhs.toInt - rhs.toInt match {
        case x if x >= 0 => create(x)
        case x           => create(x + modulus)
      }

    override protected def calculateProduct(rhs: ModularNumber) = {
      val prod = (lhs.toLong * rhs.toLong) % modulus.toLong
      create(prod.toInt)
    }

    override protected def hasTheSameValueAs(rhs: ModularNumber) =
      lhs.toInt == rhs.toInt

    override protected def hasTheSameModulusAs(that: ModularNumber) = that match {
      case rhs: ModularInt => lhs.modulus == rhs.modulus
      case _ => modulus == that.modulusAs[Int]
    }
  }

  private[mod] class ModularLong(val longValue: Long, val modulus: Long)
    extends ModularNumberAdapter[Long] with LongSpireIntegralNumber{ lhs =>

    override def valueAs[I: Integral] = implicitly[Integral[I]].fromLong(longValue)
    override def modulusAs[I: Integral] = implicitly[Integral[I]].fromLong(modulus)

    private def create(n: Long) = new ModularLong(n, modulus)

    override protected def calculateNegate =
      create(modulus - longValue)

    override protected def calculateSum(rhs: ModularNumber) = {
      val sum = (ULong(lhs.toLong) + ULong(rhs.toLong)) % ULong(modulus.toLong)
      create(sum.toLong)
    }

    override protected def calculateDifference(rhs: ModularNumber) = {
      lhs.toLong - rhs.toLong match {
        case x if x >= 0 => create(x)
        case x           => create(x + modulus)
      }
    }

    override protected def calculateProduct(rhs: ModularNumber) = {
      val prod = (lhs.toBigInt * rhs.bigIntValue) % modulus.toBigInt
      create(prod.toLong)
    }

    override protected def hasTheSameValueAs(rhs: ModularNumber) =
      lhs.toLong == rhs.toLong

    override protected def hasTheSameModulusAs(that: ModularNumber) = that match {
      case rhs: ModularLong => lhs.modulus == rhs.modulus
      case _ => modulus == that.modulusAs[Long]
    }
  }

  private[mod] class ModularBigInt(val bigIntValue: BigInt, val modulus: BigInt)
    extends ModularNumberAdapter[BigInt] with BigIntSpireIntegralNumber{ lhs =>

    override def valueAs[I: Integral] = implicitly[Integral[I]].fromBigInt(bigIntValue)
    override def modulusAs[I: Integral] = implicitly[Integral[I]].fromBigInt(modulus)

    private def create(n: BigInt) = new ModularBigInt(n, modulus)

    override protected def calculateNegate = create(modulus - bigIntValue)

    override protected def calculateSum(rhs: ModularNumber) =
      create((lhs.bigIntValue + rhs.bigIntValue) mod modulus)

    override protected def calculateDifference(rhs: ModularNumber) =
      create((lhs.bigIntValue - rhs.bigIntValue) mod modulus)

    override protected def calculateProduct(rhs: ModularNumber) =
      create((lhs.bigIntValue * rhs.bigIntValue) mod modulus)

    override protected def hasTheSameValueAs(rhs: ModularNumber) =
      lhs.bigIntValue == rhs.bigIntValue

    override protected def hasTheSameModulusAs(that: ModularNumber) = that match {
      case rhs: ModularBigInt => lhs.modulus == rhs.modulus
      case _ => modulus == that.modulusAs[BigInt]
    }
  }
}