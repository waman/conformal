package org.waman.conformal.number.decimal

import org.waman.conformal.number.SpireNumeric

import scala.math.{ScalaNumber, ScalaNumericConversions}
import spire.math._
import spire.algebra._
import spire.implicits._

abstract class SpireDecimalNumber
    extends ScalaNumber with ScalaNumericConversions with SpireNumeric{

  override def underlying = this

  override def isValidByte = isWhole && toByte.toInt == toInt
  override def isValidShort = isWhole && toShort.toInt == toInt
  override def isValidChar = isWhole && toChar.toInt == toInt
  override def isValidInt = isWhole && toInt.toLong == toLong
  override def isValidLong = isWhole && toLong.toBigInt == bigIntValue
}

abstract class GenericSpireDecimalNumber[T]
    extends SpireDecimalNumber{

  override def intValue = longValue.toInt
  override def longValue = doubleValue.toLong
  override def bigIntValue = rationalValue.toBigInt

  override def floatValue = doubleValue.toFloat

  override def valueAs[A](implicit A: Numeric[A]) = A.fromReal(realValue)

  def isZero(implicit o: IsReal[T]): Boolean
  def isOne (implicit o: IsReal[T]): Boolean
}

trait FloatSpireDecimalNumber{ self: SpireDecimalNumber =>

  override def isWhole = floatValue.isWhole

  override def intValue  = floatValue.toInt
  override def longValue = floatValue.toLong
  override def bigIntValue = floatValue.toBigInt

  override def bigDecimalValue = floatValue.toBigDecimal
  override def doubleValue = floatValue
  override def rationalValue  = floatValue.toRational
  override def realValue = Real(floatValue)

  override def valueAs[A](implicit A: Numeric[A]) = A.fromFloat(floatValue)

  def isZero: Boolean = floatValue.isZero
  def isOne : Boolean = floatValue.isOne
}

trait DoubleSpireDecimalNumber{ self: SpireDecimalNumber =>

  override def isWhole = doubleValue.isWhole

  override def intValue  = doubleValue.toInt
  override def longValue = doubleValue.toLong
  override def bigIntValue = doubleValue.toBigInt

  override def floatValue = doubleValue
  override def bigDecimalValue = doubleValue.toBigDecimal
  override def rationalValue = doubleValue.toRational
  override def realValue = Real(doubleValue)
  override def valueAs[A](implicit A: Numeric[A]) = A.fromDouble(doubleValue)

  def isZero: Boolean = doubleValue.isZero
  def isOne : Boolean = doubleValue.isOne
}

trait BigDecimalSpireDecimalNumber{ self: SpireDecimalNumber =>

  override def isWhole = bigDecimalValue.isWhole

  override def intValue  = bigDecimalValue.toInt
  override def longValue = bigDecimalValue.toLong
  override def bigIntValue = bigDecimalValue.toBigInt

  override def floatValue  = bigDecimalValue.toFloat
  override def doubleValue = bigDecimalValue.toDouble
  override def rationalValue = bigDecimalValue.toBigDecimal
  override def realValue = Real(bigDecimalValue)

  override def valueAs[A](implicit A: Numeric[A]) = A.fromBigDecimal(bigDecimalValue)

  def isZero: Boolean = bigDecimalValue.isZero
  def isOne : Boolean = bigDecimalValue.isOne
}

trait RationalSpireDecimalNumber{ self: SpireDecimalNumber =>

  override def isWhole = rationalValue.isWhole

  override def intValue  = rationalValue.toInt
  override def longValue = rationalValue.toLong
  override def bigIntValue = rationalValue.toBigInt

  override def floatValue  = rationalValue.toFloat
  override def doubleValue = rationalValue.toDouble
  override def bigDecimalValue = doubleValue.toBigDecimal
  override def realValue = Real(rationalValue)

  override def valueAs[A](implicit A: Numeric[A]) = A.fromRational(rationalValue)

  def isZero: Boolean = rationalValue.isZero
  def isOne : Boolean = rationalValue.isOne
}

trait RealSpireDecimalNumber{ self: SpireDecimalNumber =>

  def toReal: Real

  override def isWhole = toReal.isWhole

  override def intValue  = toReal.toInt
  override def longValue = toReal.toLong
  override def bigIntValue = toReal.toBigInt

  override def floatValue  = toReal.toFloat
  override def doubleValue = toReal.toDouble
  override def bigDecimalValue = doubleValue.toBigDecimal
  override def rationalValue = toReal.toRational
  
  override def valueAs[A](implicit A: Numeric[A]) = A.fromReal(toReal)

  def isZero: Boolean = toReal.isZero
  def isOne : Boolean = toReal.isOne
}