package org.waman.conformal.computerscience

import scala.{specialized => spec}

trait Checksum[@spec(Byte, Int) I]{

  def calculateChecksum(bytes: Seq[I]): I

  def check(bytes: Seq[I]): Boolean
}

object SimpleChecksum extends Checksum[Byte]{

  override def calculateChecksum(bytes: Seq[Byte]): Byte = sumBytes(bytes)

  override def check(bytes: Seq[Byte]): Boolean = sumBytes(bytes) == 0

  def sumBytes(bytes: Seq[Byte]): Byte =
    bytes.foldLeft(0)((sum, b) => sum ^ b).toByte
}