package org.waman.conformal.computerscience

import scala.{specialized => spec}

trait Checksum[I, S]{

  protected def sum(input: Seq[I]): S

  def calculateChecksum(input: Seq[I]): S = sumToChecksum(sum(input))
  protected def sumToChecksum(sum: S): S

  def check(input: Seq[I]): Boolean = testSum(sum(input))
  protected def testSum(sum: S): Boolean
}

object SimpleChecksum extends Checksum[Byte, Byte]{

  override protected def sum(input: Seq[Byte]): Byte =
    input.reduce((x, y) => (x ^ y).toByte)

  override protected def sumToChecksum(sum: Byte): Byte = sum

  override protected def testSum(sum: Byte): Boolean = sum == 0

  def appendChecksum(input: Seq[Byte]): Seq[Byte] =
    Vector() ++ input :+ calculateChecksum(input)
}