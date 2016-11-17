package org.waman.conformal.number.integer.mod

import spire.math.Integral
import spire.implicits._

trait Modulus{

  def valueAs[I: Integral]: I

  /** Create a ModularNumber object with modifying the argument
    * The argument is always positive */
  def apply[I: Integral](n: I): ModularNumber =
    if(n < 0) -apply(-n)
    else      create(n % valueAs[I])

  /** Create a ModularNumber object with the argument as row value */
  def create[I: Integral](n: I): ModularNumber

  def apply(module: String): ModularNumber = apply(BigInt(module))

  override def equals(other: Any): Boolean =
    other match {
      case that: Modulus =>
        that.canEqual(this) &&
          hasTheSameValueAs(that)
      case _ => false
    }

  protected def hasTheSameValueAs(that: Modulus): Boolean

  def canEqual(other: Any): Boolean = other.isInstanceOf[Modulus]
}

object Modulus{

  //********** class implementations **********
  //***** Modulo *****
  private[Modulus] class IntModulus(val value: Int) extends Modulus{

    override def valueAs[I: Integral] = value

    override def create[I: Integral](n: I) = new ModularInt(this, n.toInt)

    override protected def hasTheSameValueAs(other: Modulus) = other match {
      case that: IntModulus => value == that.value
      case _ => valueAs[BigInt] == other.valueAs[BigInt]
    }

    override def hashCode: Int = (getClass, value).##
  }

  private[Modulus] class LongModulus(val value: Long) extends Modulus{

    override def valueAs[I: Integral] = implicitly[Integral[I]].fromLong(value)

    override def create[I: Integral](n: I) = new ModularLong(this, n.toLong)

    override protected def hasTheSameValueAs(other: Modulus) = other match {
      case that: LongModulus => value == that.value
      case _ => valueAs[BigInt] == other.valueAs[BigInt]
    }

    override def hashCode: Int = (getClass, value).##
  }

  private[Modulus] class BigIntModulus(val value: BigInt) extends Modulus{

    override def valueAs[I: Integral] = implicitly[Integral[I]].fromBigInt(value)

    override def create[I: Integral](n: I) = new ModularBigInt(this, n.toBigInt)

    override protected def hasTheSameValueAs(other: Modulus) = other match {
      case that: BigIntModulus => value == that.value
      case _ => valueAs[BigInt] == other.valueAs[BigInt]
    }

    override def hashCode: Int = (getClass, value).##
  }

  //***** ModuloNumbers *****
  private[Modulus] class ModularInt(val modulus: IntModulus, val value: Int)
      extends ModularNumber{

    override def valueAs[I: Integral]: I = implicitly[Integral[I]].fromInt(value)

    override def isZero = value == 0
    override def isOne = value == 0

    override protected def calculateNegate =
      modulus.create(modulus.value - value)

    override protected def calculateSum(rhs: ModularNumber) =
      modulus(value.toLong + rhs.valueAs[Long])

    override protected def calculateDifference(rhs: ModularNumber) =
      modulus(value - rhs.valueAs[Int])

    override protected def calculateProduct(rhs: ModularNumber) =
      modulus(value.toLong * rhs.valueAs[Long])

    override protected def hasTheSameValueAs(that: ModularNumber) =
      value == that.valueAs[Int]

    override def hashCode = (modulus, value).##
    override protected def valueAsString = value.toString
  }

  private[Modulus] class ModularLong(val modulus: LongModulus, val value: Long)
      extends ModularNumber{

    override def valueAs[I: Integral]: I = implicitly[Integral[I]].fromLong(value)

    override def isZero = value == 0L
    override def isOne = value == 0L

    override protected def calculateNegate = modulus.create(modulus.value - value)

    override protected def calculateSum(rhs: ModularNumber) =
      modulus(value.toBigInt + rhs.valueAs[BigInt])

    override protected def calculateDifference(rhs: ModularNumber) =
      modulus(value - rhs.valueAs[Long])

    override protected def calculateProduct(rhs: ModularNumber) =
      modulus(value.toBigInt * rhs.valueAs[BigInt])

    override protected def hasTheSameValueAs(that: ModularNumber) =
      value == that.valueAs[Long]

    override def hashCode = (modulus, value).##
    override protected def valueAsString = value.toString
  }

  private[Modulus] class ModularBigInt(val modulus: BigIntModulus, val value: BigInt)
      extends ModularNumber{

    override def valueAs[I: Integral]: I = implicitly[Integral[I]].fromBigInt(value)

    override def isZero = value.isZero
    override def isOne = value.isOne

    override protected def calculateNegate = modulus.create(modulus.value - value)

    override protected def calculateSum(rhs: ModularNumber) =
      modulus(value.toLong + rhs.valueAs[Long])

    override protected def calculateDifference(rhs: ModularNumber) =
      modulus(value - rhs.valueAs[BigInt])

    override protected def calculateProduct(rhs: ModularNumber) =
      modulus(value.toLong * rhs.valueAs[Long])

    override protected def hasTheSameValueAs(that: ModularNumber) =
      value == that.valueAs[Int]

    override def hashCode = (modulus, value).##
    override protected def valueAsString = value.toString
  }

  //********** Apply factory methods **********
  def apply(module: Int): Modulus = {
    require(module > 0, s"Module must be positive: $module")
    new IntModulus(module)
  }

  def apply(module: Long): Modulus = {
    require(module > 0, s"Module must be positive: $module")

    if(module <= Int.MaxValue)
      new IntModulus(module.toInt)
    else
      new LongModulus(module)
  }

  def apply(module: BigInt): Modulus = {
    require(module > 0, s"Module must be positive: $module")

    if(module <= Int.MaxValue)
      new IntModulus(module.toInt)
    else if(module <= Long.MaxValue)
      new LongModulus(module.toLong)
    else
      new BigIntModulus(module)
  }
}