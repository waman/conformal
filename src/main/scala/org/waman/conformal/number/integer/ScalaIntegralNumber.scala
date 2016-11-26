package org.waman.conformal.number.integer

import scala.math.{ScalaNumber, ScalaNumericConversions}
import spire.math.Integral
import spire.implicits._

abstract class ScalaIntegralNumber extends ScalaNumber with ScalaNumericConversions{

  override def underlying: Object = this
  override def isWhole = true

  override def intValue: Int = toInt
  override def longValue: Long = toLong
  override def floatValue: Float = toFloat
  override def doubleValue: Double = toDouble

  override def toInt: Int = throw new UnsupportedOperationException(getClass+".toInt method")
  override def toLong: Long = throw new UnsupportedOperationException(getClass+".toLong method")
  def toBigInt: BigInt
  def valueAs[I: Integral]: I

  override def toFloat: Float = toBigInt.toFloat
  override def toDouble: Double = toBigInt.toDouble

  override def isValidByte: Boolean = toByte.toInt == toInt
  override def isValidShort: Boolean = toShort.toInt == toInt
  override def isValidChar: Boolean = toChar.toInt == toInt
  override def isValidInt: Boolean = toInt.toLong == toLong
  def isValidLong: Boolean = BigInt(toLong) == toBigInt

  def isZero: Boolean
  def isOne : Boolean
}

trait IntScalaIntegralNumber{ self: ScalaIntegralNumber =>

  def value: Int

  override def toInt: Int = value
  override def toLong: Long = value.toLong
  override def toBigInt: BigInt = value.toBigInt
  override def valueAs[I: Integral]: I = implicitly[Integral[I]].fromInt(value)

  override def isValidInt = true
  override def isValidLong = true

  override def isZero: Boolean = value == 0
  override def isOne : Boolean = value == 1
}

trait LongScalaIntegralNumber{ self: ScalaIntegralNumber =>

  def value: Long

  override def toInt: Int = value.toInt
  override def toLong: Long = value
  override def toBigInt: BigInt = value.toBigInt
  override def valueAs[I: Integral]: I = implicitly[Integral[I]].fromLong(value)

  override def isValidLong = true

  override def isZero: Boolean = value == 0L
  override def isOne : Boolean  = value == 1L
}

trait BigIntScalaIntegralNumber{ self: ScalaIntegralNumber =>

  def value: BigInt

  override def toInt: Int = value.toInt
  override def toLong: Long = value.toLong
  override def toBigInt: BigInt = value
  override def valueAs[I: Integral]: I = implicitly[Integral[I]].fromBigInt(value)

  override def isZero: Boolean = value.isZero
  override def isOne : Boolean  = value.isOne
}