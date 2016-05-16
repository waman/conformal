package org.waman.conformal.computerscience

import java.io.BufferedInputStream
import java.nio.file.{Files, Path, Paths}

import spire.implicits._

import scala.annotation.tailrec
import scala.collection.LinearSeq

trait CRC extends Checksum[Byte, Int] {

  val bitCount: Int
  val isReversedBitOrder: Boolean

  lazy val mask: Int = CRC.getMask(bitCount)

  def initialValue: Int = mask
  val divisor: Int

  //***** calculate checksum *****
  override protected def sum(bytes: Seq[Byte]): Int = bytes match {
    case seq: LinearSeq[Byte] =>
      processBytes(initialValue, seq)
    case _ =>
      processBytes(initialValue, bytes, bytes.length)
  }

  @tailrec
  protected final def processBytes(r: Int, bytes: LinearSeq[Byte]): Int = bytes.isEmpty match {
    case true  => r
    case false => processBytes(processOneByte(r, bytes.head), bytes.tail)
  }

  protected final def processBytes(r: Int, bytes: Seq[Byte], n: Int): Int = {
    // bytes.length may not equal n
    @tailrec
    def processBytesWithIndex(r: Int, i: Int): Int = i < n match {
      case true  => processBytesWithIndex(processOneByte(r, bytes(i)), i+1)
      case false => r
    }

    processBytesWithIndex(r, 0)
  }

  protected def processOneByte(r: Int, b: Byte): Int
  protected def processBits(r: Int, n: Int): Int

  //***** check *****
  def check(input: Seq[Byte], checksum: Int): Boolean =
    testSum(sum(input) ^ checksum)

  override def check(input: Seq[Byte]): Boolean = {
    require(bitCount % CRC.BitsPerByte == 0,
      "The bit size of the checksum must be a multiple of 8")
    super.check(input)
  }

  //***** IO *****
  val bufferSize = 1024 * 32

  def calculateChecksum(path: String): Int = calculateChecksum(Paths.get(path))

  def calculateChecksum(path: Path): Int = {

    val in = new BufferedInputStream(Files.newInputStream(path))
    val buffer = new Array[Byte](bufferSize)

    @tailrec
    def processByteStream(r: Int): Int =
      in.read(buffer) match {
        case n if n >= 0 => processByteStream(processBytes(r, buffer, n))
        case -1 => r
      }

    val r = try processByteStream(initialValue)
            finally in.close()

    sumToChecksum(r)
  }
}

object CRC{

  val BitsPerByte: Int = 8
  val BitsPerInt: Int  = 32

  def getMask(bit: Int): Int = (2L**bit-1L).toInt // 0xFFFF for 16 bits, 0xFFFFFFFF for 32 bits

  def getHighestPlaceFilter(bit: Int): Int = 1 << (bit-1)  // 0x8000 for 16 bits, 0x80000000 for 32 bits

  def getFiniteFieldPolynomial(powers: Seq[Int]): Int = {
    val s = (powers.max to 0 by -1).map{
      case d if powers.contains(d) => 1
      case _ => 0
    }.mkString("")

    Integer.valueOf(s, 2)
  }

  def getDivisor(powers: Seq[Int]): Int =
    getFiniteFieldPolynomial(powers.sortWith((x, y) => x > y).tail)  // descendant, remove the highest

  def reverseBits(i: Int, bit: Int): Int = Integer.reverse(i) >>> (32 - bit)

  def toInt(bytes: Seq[Byte]): Int = {
    val n = bytes.length

    @tailrec
    def constructInt(r: Int, bytes: Seq[Byte]): Int = bytes.length match {
      case 0 => r
      case i =>
        val s = r ^ ((bytes.head & 0xFF) << (CRC.BitsPerByte * (i-1)))
        constructInt(s, bytes.tail)
    }

    constructInt(0, bytes)
  }

  object Polynomial{

    lazy val CRC_16_CCITT: Int =
      CRC.getDivisor(Seq(16, 12, 5, 0))

    lazy val CRC_32: Int =
      CRC.getDivisor(Seq(32, 26, 23, 22, 16, 12, 11, 10, 8, 7, 5, 4, 2, 1, 0))
  }
}

trait NormalBitOrder{ self: CRC =>

  override val isReversedBitOrder = false
  override protected def sumToChecksum(r: Int): Int = ~r & mask
  override protected def testSum(r: Int): Boolean = (~r & mask) == 0

  protected lazy val placeFilter: Int = CRC.getHighestPlaceFilter(bitCount)

  override protected def processOneByte(r: Int, b: Byte): Int = {
    val s = r ^ ((b & 0xFF) << (bitCount - CRC.BitsPerByte))
    processBits(s, CRC.BitsPerByte)
  }

  @tailrec
  protected final def processBits(r: Int, n: Int): Int = n match {
    case 0 => r
    case _ =>
      r & placeFilter match {
        case 0 => processBits(r << 1, n-1)
        case _ => processBits((r << 1) ^ divisor, n-1)
      }
  }

  override def check(input: Seq[Byte]): Boolean = {
    require(bitCount % CRC.BitsPerByte == 0,
      "The bit size of the checksum must be a multiple of 8")

    val nBits = bitCount / CRC.BitsPerByte
    val (nInit, nLast) = input.splitAt(input.length-nBits)
    val r = sum(nInit)
    val s = r ^ CRC.toInt(nLast)
    testSum(s)
  }
}

trait NormalBitOrderWithTable extends NormalBitOrder{ self: CRC =>

  def tableLength = 1 << CRC.BitsPerByte

  lazy val table: List[Int] = (0 until tableLength).map{ i =>
    val r = i << (bitCount - CRC.BitsPerByte)
    processBits(r , CRC.BitsPerByte) & mask
  }.toList

  override protected def processOneByte(r: Int, b: Byte): Int =
    (r << CRC.BitsPerByte) ^ table((r >> (bitCount - CRC.BitsPerByte)^ b) & 0xFF)
}

trait ReversedBitOrder extends CRC{ self: CRC =>

  override val isReversedBitOrder = true
  override protected def sumToChecksum(sum: Int): Int = sum ^ mask
  override protected def testSum(sum: Int): Boolean = (sum ^ mask) == 0

  override lazy val divisor = CRC.reverseBits(divisorInNormalOrder, bitCount)
  def divisorInNormalOrder: Int

  override protected def processOneByte(r: Int, b: Byte): Int =
    processBits(r ^ (b & 0xFF), CRC.BitsPerByte)

  @tailrec
  protected final def processBits(r: Int, n: Int): Int = n match {
    case 0 => r
    case _ =>
      r & 1 match {
        case 0 => processBits(r >>> 1, n-1)
        case _ => processBits((r >>> 1) ^ divisor, n-1)
      }
  }

  override def check(input: Seq[Byte]): Boolean = {
    val nBits = bitCount / CRC.BitsPerByte
    val (nInit, nLast) = input.splitAt(input.length-nBits)
    val r = sum(nInit)
    val s = r ^ CRC.toInt(nLast.reverse)
    testSum(s)
  }
}

trait ReversedBitOrderWithTable extends ReversedBitOrder{ self: CRC =>

  def tableLength = 1 << CRC.BitsPerByte

  lazy val table: List[Int] = (0 until tableLength).map{ r =>
    processBits(r , CRC.BitsPerByte) & mask
  }.toList

  override protected def processOneByte(r: Int, b: Byte): Int =
    (r >>> CRC.BitsPerByte) ^ table((r ^ b) & 0xFF)
}

trait Bit16{ self: CRC =>

  override val bitCount = 16
}

trait Bit32{ self: CRC =>

  override val bitCount = 32

//  def check(input: Seq[Byte], checksum: Int): Boolean = {
//    val s = calculateChecksum(input)
//    val s1 = processOneByte(s , (checksum >>> (CRC.BitsPerByte*3)).toByte)
//    val s2 = processOneByte(s1, (checksum >>> (CRC.BitsPerByte*2)).toByte)
//    val s3 = processOneByte(s2, (checksum >>>  CRC.BitsPerByte).toByte)
//    val s4 = processOneByte(s3, checksum.toByte)
//    testSum(s4)
//  }
}

//***** CRC Objects *****
object CRC16 extends CRC with NormalBitOrder with Bit16{
  override lazy val divisor: Int = CRC.Polynomial.CRC_16_CCITT
}

object CRC16T extends CRC with NormalBitOrderWithTable with Bit16{
  override lazy val divisor: Int = CRC.Polynomial.CRC_16_CCITT
}

object CRC16R extends CRC with ReversedBitOrder with Bit16{
  override def divisorInNormalOrder: Int = CRC.Polynomial.CRC_16_CCITT
}

object CRC16RT extends CRC with ReversedBitOrderWithTable with Bit16{
  override def divisorInNormalOrder: Int = CRC.Polynomial.CRC_16_CCITT
}

object CRC32 extends CRC with NormalBitOrder with Bit32{
  override lazy val divisor: Int = CRC.Polynomial.CRC_32
}

object CRC32T extends CRC with NormalBitOrderWithTable with Bit32{
  override lazy val divisor: Int = CRC.Polynomial.CRC_32
}

object CRC32R extends CRC with ReversedBitOrder with Bit32{
  override def divisorInNormalOrder: Int = CRC.Polynomial.CRC_32
}

object CRC32RT extends CRC with ReversedBitOrderWithTable with Bit32{
  override def divisorInNormalOrder: Int = CRC.Polynomial.CRC_32
}