package org.waman.conformal.number.integer.mod

import org.waman.conformal.number.integer.{BigIntScalaIntegralNumber, IntScalaIntegralNumber, LongScalaIntegralNumber, ScalaIntegralNumber}
import spire.implicits._
import spire.math.{Integral, UInt, ULong}

@SerialVersionUID(0L)
abstract class Modulus extends ScalaIntegralNumber with Serializable{

  def valueAs[I: Integral]: I

  /** Create a ModularNumber object with modifying the argument */
  def apply[I: Integral](n: I): ModularNumber
  def apply(module: String): ModularNumber = apply(BigInt(module))

  //***** Methods fo Any *****
  override def equals(other: Any): Boolean =
    other match {
      case that: Modulus =>
        that.canEqual(this) &&
          hasTheSameValueAs(that)
      case _ => false
    }

  protected def hasTheSameValueAs(that: Modulus): Boolean
  def canEqual(other: Any): Boolean = other.isInstanceOf[Modulus]
  def valueAsString: String
}

object Modulus{

  //********** apply() factory methods **********
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

  //********** class implementations **********
  //***** Modulo *****
  private[mod] abstract class ModulusAdapter extends Modulus{

    /** Create a ModularNumber object with modifying the argument */
    override def apply[I: Integral](n: I): ModularNumber = {
      val algebra = implicitly[Integral[I]]
      def maxInt = algebra.fromInt(Int.MaxValue)
      def maxLong = algebra.fromLong(Long.MaxValue)

      n match {
        case _ if n < 0 =>
          n match {
            case _ if n == Int.MinValue  => -fromLong(Int.MaxValue + 1L)
            case _ if n == Long.MinValue => -fromBigInt(BigInt(Long.MaxValue) + 1)
            case _ => -apply(-n)
          }

        case _ if n <= maxInt  => fromInt(n.toInt)
        case _ if n <= maxLong => fromLong(n.toLong)
        case _                 => fromBigInt(n.toBigInt)
      }
    }

    /** The argument integer is positive. */
    protected def fromInt(n: Int): ModularNumber
    protected def fromLong(n: Long): ModularNumber
    protected def fromBigInt(n: BigInt): ModularNumber
  }

  private[mod] class IntModulus(val value: Int)
      extends ModulusAdapter with IntScalaIntegralNumber{

    override protected def fromInt(n: Int) = create(n % value)
    override protected def fromLong(n: Long) = create((n % value).toInt)
    override protected def fromBigInt(n: BigInt) = create((n % value).toInt)

    private def create(n: Int) = new ModularInt(this, n)

    override protected def hasTheSameValueAs(that: Modulus) =
      toInt == that.toInt

    override def hashCode = (getClass, value).##
    override def valueAsString = value.toString
  }

  private[mod] class LongModulus(val value: Long)
      extends ModulusAdapter with LongScalaIntegralNumber{
    
    require(!value.isValidInt)

    override protected def fromInt(n: Int) = create(n)
    override protected def fromLong(n: Long) = create(n % value)
    override protected def fromBigInt(n: BigInt) = create((n % value).toLong)

    private def create(n: Long) = new ModularLong(this, n)

    override protected def hasTheSameValueAs(that: Modulus) =
      toLong == that.toLong

    override def hashCode = (getClass, value).##
    override def valueAsString = value.toString
  }

  private[mod] class BigIntModulus(val value: BigInt)
      extends ModulusAdapter with BigIntScalaIntegralNumber{
    
    require(!value.isValidLong)

    override protected def fromInt(n: Int) = create(n)
    override protected def fromLong(n: Long) = create(n)
    override protected def fromBigInt(n: BigInt) = create(n % value)

    private def create(n: BigInt) = new ModularBigInt(this, n)

    override protected def hasTheSameValueAs(that: Modulus) =
      toBigInt == that.toBigInt

    override def hashCode = (getClass, value).##
    override def valueAsString = value.toString
  }

  //***** ModuloNumbers *****
  private[mod] class ModularInt(val modulus: IntModulus, val value: Int)
      extends ModularNumber with IntScalaIntegralNumber{ lhs =>

    private def create(n: Int) = new ModularInt(modulus, n)

    override protected def calculateNegate = create(modulus.value - value)

    override protected def calculateSum(rhs: ModularNumber) = {
      val sum = (UInt(lhs.toInt) + UInt(rhs.toInt)) % UInt(modulus.toInt)
      create(sum.toInt)
    }

    override protected def calculateDifference(rhs: ModularNumber) =
      lhs.toInt - rhs.toInt match {
        case x if x >= 0 => create(x)
        case x           => create(x + value)
      }

    override protected def calculateProduct(rhs: ModularNumber) = {
      val prod = (lhs.toLong * rhs.toLong) % value.toLong
      create(prod.toInt)
    }

    override protected def hasTheSameValueAs(rhs: ModularNumber) =
      lhs.toInt == rhs.toInt

    override def hashCode = (modulus, value).##
    override protected def valueAsString = value.toString
  }

  private[mod] class ModularLong(val modulus: LongModulus, val value: Long)
      extends ModularNumber with LongScalaIntegralNumber{ lhs =>

    private def create(n: Long) = new ModularLong(modulus, n)

    override protected def calculateNegate =
      create(modulus.value - value)

    override protected def calculateSum(rhs: ModularNumber) = {
      val sum = (ULong(lhs.toLong) + ULong(rhs.toLong)) % ULong(modulus.toLong)
      create(sum.toLong)
    }

    override protected def calculateDifference(rhs: ModularNumber) = {
      lhs.toLong - rhs.toLong match {
        case x if x >= 0 => create(x)
        case x           => create(x + value)
      }
    }

    override protected def calculateProduct(rhs: ModularNumber) = {
      val prod = (lhs.toBigInt * rhs.toBigInt) % value.toBigInt
      create(prod.toLong)
    }

    override protected def hasTheSameValueAs(rhs: ModularNumber) =
      lhs.toLong == rhs.toLong

    override def hashCode = (modulus, value).##
    override protected def valueAsString = value.toString
  }

  private[mod] class ModularBigInt(val modulus: BigIntModulus, val value: BigInt)
      extends ModularNumber with BigIntScalaIntegralNumber{ lhs =>

    private def create(n: BigInt) = new ModularBigInt(modulus, n)

    override protected def calculateNegate = create(modulus.value - value)

    override protected def calculateSum(rhs: ModularNumber) =
      create((lhs.toBigInt + rhs.toBigInt) % value)

    override protected def calculateDifference(rhs: ModularNumber) =
      lhs.toBigInt - rhs.toBigInt match {
        case x if x >= 0 => create(x)
        case x           => create(x + value)
      }

    override protected def calculateProduct(rhs: ModularNumber) =
      create((lhs.toBigInt * rhs.toBigInt) % value)

    override protected def hasTheSameValueAs(rhs: ModularNumber) =
      lhs.toBigInt == rhs.toBigInt

    override def hashCode = (modulus, value).##
    override protected def valueAsString = value.toString
  }
}