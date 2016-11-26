package org.waman.conformal.computerscience

import spire.implicits._

import scala.annotation.tailrec

object XorDivision{
  def binaryLength(i: Int): Int = Integer.toBinaryString(i).length
  def createPlaceFilter(length: Int): Int = 1 << (length-1)

  def remainder(dividend: Int, divisor: Int): Int = {
    val divisorLength = binaryLength(divisor)
    val placeFilter = createPlaceFilter(divisorLength)
    remainder(dividend, binaryLength(dividend), divisor, divisorLength, placeFilter)
  }

  private[XorDivision]
  def remainder(dividend: Int, dividendLength: Int, divisor: Int, divisorLength: Int, placeFilter: Int): Int = {
    val lengthDif = dividendLength - divisorLength

    val divisorShifted     = divisor     << lengthDif
    val placeFilterShifted = placeFilter << lengthDif

    @tailrec
    def divideOnePlace(dividend: Int, divisor: Int, placeFilter: Int, n: Int): Int = n match {
      case 0 => dividend
      case _ =>
        val newDividend = dividend & placeFilter match {
          case 0 => dividend
          case _ => dividend ^ divisor
        }

        divideOnePlace(newDividend, divisor >> 1, placeFilter >> 1, n-1)
    }

    divideOnePlace(dividend, divisorShifted, placeFilterShifted, lengthDif+1)
  }

  private[XorDivision]
  def remainder(dividend: BigInt, dividendLength: Int,
                      divisor: BigInt, divisorLength: Int, placeFilter: BigInt): BigInt = {
    val lengthDif: Int = dividendLength - divisorLength

    val divisorShifted     = divisor     << lengthDif
    val placeFilterShifted = placeFilter << lengthDif

    @tailrec
    def divideOnePlace(dividend: BigInt, divisor: BigInt, placeFilter: BigInt, n: Int): BigInt = n match {
      case 0 => dividend
      case _ =>
        val newDividend = dividend & placeFilter match {
          case bi if bi.isZero => dividend
          case _ => dividend ^ divisor
        }

        divideOnePlace(newDividend, divisor >> 1, placeFilter >> 1, n-1)
    }

    divideOnePlace(dividend, divisorShifted, placeFilterShifted, lengthDif+1)
  }

  def constantDividend(dividend: Int) = new ConstantDividend(dividend)

  class ConstantDividend(val dividend: Int){

    val dividendLength: Int = binaryLength(dividend)

    def remainder(divisor: Int): Int = {
      val divisorLength = binaryLength(divisor)
      val placeFilter = createPlaceFilter(divisorLength)
      XorDivision.remainder(dividend, dividendLength, divisor, divisorLength, placeFilter)
    }
  }

  def constantDivisor(divisor: Int) = new ConstantDivisor(divisor)

  class ConstantDivisor(val divisor: Int){

    val divisorLength: Int = binaryLength(divisor)
    val placeFilter  : Int = 1 << (divisorLength-1)

    def remainder(dividend: Int): Int =
      XorDivision.remainder(dividend, binaryLength(dividend), divisor, divisorLength, placeFilter)
  }
}