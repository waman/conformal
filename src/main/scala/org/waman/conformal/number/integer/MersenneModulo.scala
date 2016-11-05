package org.waman.conformal.number.integer

import spire.implicits._
import spire.math.Integral

import scala.annotation.tailrec

case class MersenneModulo(p: Int){

  def apply[I: Integral](n: I): MersenneModuloNumber = {
    val bits = toBinary(n).map(_ == 1)
    MersenneModuloNumber(bits, p)
  }
}

/**
  * The bit property is in ascending order (head is 2^0 digit).
  * This constructor must be used with Seq[Boolean] whose length is p.
  */
case class MersenneModuloNumber private(bits: Seq[Boolean]){

  def p: Int = bits.length

  def +(that: MersenneModuloNumber) : MersenneModuloNumber = {
    require(that.p == this.p)

    def addBitByBit(bs: Seq[Seq[Boolean]], s: Boolean): Stream[Boolean] =
      bs.isEmpty match {
        case true  =>
          if(s) Stream(true) else Stream()
        case false => (s +: bs.head).count(_ == true) match {
          case 0 => false #:: addBitByBit(bs.tail, s = false)
          case 1 => true  #:: addBitByBit(bs.tail, s = false)
          case 2 => false #:: addBitByBit(bs.tail, s = true)
          case 3 => true  #:: addBitByBit(bs.tail, s = true)
        }
      }

    val summed = addBitByBit(Seq(this.bits, that.bits).transpose, s = false)
    MersenneModuloNumber(summed, p)
  }

  def toVal[I: Integral]: I =
    bits.foldRight[I](0){ (b, sum) =>
      if(b) sum * 2 + 1
      else  sum * 2
    }

  def toInt: Int = toVal[Int]
  def toLong: Long = toVal[Long]
  def toBigInt: BigInt = toVal[BigInt]
}

object MersenneModuloNumber{

  /** This apply() factory method can be used with Seq[Boolean] whose length is not p. */
  def apply(bits: Seq[Boolean], p: Int): MersenneModuloNumber =
    bits.length match {
      case len if len == p =>
        if (bits.forall(_ == true))
          new MersenneModuloNumber(Seq.fill(p)(false)) // 2^p - 1 => 0
        else
          new MersenneModuloNumber(bits)

      case len if len < p =>
        new MersenneModuloNumber(bits.padTo(p, false))

      case _ => // Example: p=3 (mod 7), seq = (10110011)  (1 = T, 0 = F)
        bits.grouped(p).map {
          // ((101)(100)(11))
          case s if s.length != p => s.padTo(p, false) // ((101)(100)(110))
          case s => s
        }.map(new MersenneModuloNumber(_)).reduce(_ + _)
    }
}