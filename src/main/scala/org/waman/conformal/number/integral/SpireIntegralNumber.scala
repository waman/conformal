package org.waman.conformal.number.integral

import org.waman.conformal.number.SpireNumeric

import scala.math.{ScalaNumber, ScalaNumericConversions}
import spire.math.{Numeric, Real}
import spire.implicits._

abstract class SpireIntegralNumber
    extends ScalaNumber with ScalaNumericConversions with SpireNumeric{

  override def underlying = this
  override def isWhole = true

  override def isValidByte = toByte.toInt == toInt
  override def isValidShort = toShort.toInt == toInt
  override def isValidChar = toChar.toInt == toInt
  override def isValidInt = toInt.toLong == toLong
  override def isValidLong = BigInt(toLong) == bigIntValue

  def isZero: Boolean
  def isOne : Boolean
}

trait IntSpireIntegralNumber{ self: SpireIntegralNumber =>

  override def longValue = intValue.toLong
  override def bigIntValue = intValue.bigIntValue

  override def floatValue = intValue.toFloat
  override def doubleValue = intValue.toDouble
  override def bigDecimalValue = intValue.toBigDecimal
  override def rationalValue = intValue.toRational
  override def realValue = intValue.toReal

  override def valueAs[A](implicit a: Numeric[A]) = a.fromInt(intValue)

  override def isValidInt = true
  override def isValidLong = true

  override def isZero = intValue == 0
  override def isOne  = intValue == 1
}

trait LongSpireIntegralNumber{ self: SpireIntegralNumber =>

  override def intValue = longValue.toInt
  override def bigIntValue = longValue.toBigInt

  override def floatValue = longValue.toFloat
  override def doubleValue = longValue.toDouble
  override def bigDecimalValue = longValue.toBigDecimal
  override def rationalValue = longValue.toRational
  override def realValue = Real(longValue)

  override def valueAs[A](implicit a: Numeric[A]) = a.fromLong(longValue)

  override def isValidLong = true

  override def isZero = longValue == 0L
  override def isOne  = longValue == 1L
}

trait BigIntSpireIntegralNumber{ self: SpireIntegralNumber =>

  override def intValue = bigIntValue.toInt
  override def longValue = bigIntValue.toLong

  override def floatValue = bigIntValue.toFloat
  override def doubleValue = bigIntValue.toDouble
  override def bigDecimalValue = bigIntValue.toBigDecimal
  override def rationalValue = bigIntValue.toRational
  override def realValue = Real(bigIntValue)

  override def valueAs[A](implicit a: Numeric[A]) = a.fromBigInt(toBigInt)

  override def isZero = toBigInt.isZero
  override def isOne  = toBigInt.isOne
}