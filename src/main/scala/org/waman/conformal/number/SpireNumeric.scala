package org.waman.conformal.number

import spire.math.{Numeric, Rational, Real}

import scala.math.{ScalaNumber, ScalaNumericConversions}

trait SpireNumeric { self: ScalaNumber with ScalaNumericConversions =>

  def isValidLong: Boolean

  def bigIntValue: BigInt
  def bigDecimalValue: BigDecimal
  def rationalValue: Rational
  def realValue: Real

  def toBigInt: BigInt = bigIntValue
  def toBigDecimal: BigDecimal = bigDecimalValue
  def toRational: Rational = rationalValue
  def toReal: Real = realValue

  def valueAs[A](implicit A: Numeric[A]): A
}
