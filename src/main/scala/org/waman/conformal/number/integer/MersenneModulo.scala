package org.waman.conformal.number.integer

import spire.implicits._
import spire.math.Integral

case class MersenneModulo(p: Int){

  def modulusOf[I: Integral] = implicitly[Integral[I]].fromInt(2)**p - 1
  def modulus: Long = modulusOf[Long]

  val Zero: MersenneModuloNumber = new MersenneModuloNumber{

    override def mersenneModulo: MersenneModulo = MersenneModulo.this

    override def bits = Stream.fill(p)(false)
    override def isZero: Boolean = true

    override def unary_- = this
    override def +(that: MersenneModuloNumber) = that
    override def -(that: MersenneModuloNumber) = -that
    override def *(that: MersenneModuloNumber) = this

    override def toVal[I: Integral]: I = 0
  }

  def apply[I: Integral](n: I): MersenneModuloNumber = n match {
    case _ if n < 0 => -apply(-n)
    case _ =>
      val bits = toBinaryInAscendingOrder(n).map(_ == 1)
      MersenneModuloNumber(bits, this)
  }
}

/**
  * The bit property is in ascending order (head is 2**0 digit).
  * This constructor must be used with Seq[Boolean] whose length is p.
  */
trait MersenneModuloNumber{

  def mersenneModulo: MersenneModulo
  def p: Int = mersenneModulo.p
  def bits: Seq[Boolean]
  def bitsInInt: Seq[Int] = bits.map(if(_) 1 else 0)
  def isZero: Boolean

  def unary_- : MersenneModuloNumber

  def +(that: MersenneModuloNumber): MersenneModuloNumber
  def -(that: MersenneModuloNumber): MersenneModuloNumber
  def *(that: MersenneModuloNumber): MersenneModuloNumber

  def toVal[I: Integral]: I
  def toInt: Int = toVal[Int]
  def toLong: Long = toVal[Long]
  def toBigInt: BigInt = toVal[BigInt]

  override def equals(other: Any): Boolean = other match {
    case that: MersenneModuloNumber =>
      (that canEqual this) &&
      this.p == that.p &&
      this.bits == that.bits
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[MersenneModuloNumber]

  override def hashCode: Int = (p, bits).##
  /** return a String representation of this bits with descending order of digit */
  override def toString: String = bitsInInt.reverse.mkString("(", "", ")_2")
}

object MersenneModuloNumber{

  //***** Impl of MersenneModuloNumber trait *****
  class BooleanBits(val bits: Seq[Boolean], val mersenneModulo: MersenneModulo)
      extends MersenneModuloNumber{

    override lazy val p = mersenneModulo.p

    override def isZero = bits.forall(_ == false)
    override def unary_- = MersenneModuloNumber(bits.map(!_), this.mersenneModulo)

    override def +(that: MersenneModuloNumber): MersenneModuloNumber = {
      // add two bit seqs
      def addBitByBit(as: Seq[Boolean], bs: Seq[Boolean], s: Boolean): Stream[Boolean] =
        as.isEmpty match {
          case true  =>
            require(bs.isEmpty,
              s"Two MersenneModuloNumber must have the same p when added: $p, ${that.p}")
            if(s) Stream(true) else Stream()

          case false => Seq(as.head, bs.head, s).count(_ == true) match {
            case 0 => false #:: addBitByBit(as.tail, bs.tail, s = false)
            case 1 => true  #:: addBitByBit(as.tail, bs.tail, s = false)
            case 2 => false #:: addBitByBit(as.tail, bs.tail, s = true)
            case 3 => true  #:: addBitByBit(as.tail, bs.tail, s = true)
          }
        }

      val summed = addBitByBit(this.bits, that.bits, s = false)
      MersenneModuloNumber(summed, this.mersenneModulo)
    }

    override def -(that: MersenneModuloNumber) =
      if(that.isZero) this
      else            this + (-that)

    override def *(that: MersenneModuloNumber): MersenneModuloNumber = {
      def mod(i: Int) = if(i >= 0) i else i + p

      val thisBits: Vector[Int] = this.bitsInInt.toVector
      val summedInts = that.bits.zipWithIndex
                         .collect { case (true, n) => n}  // ignore digits of 0-value
                         .foldRight[Seq[Int]](Vector.fill(p)(0)){ (n, s) =>
                           (0 until p).map(i => s(i) + thisBits(mod(i-n)))
                         }
      MersenneModuloNumber(intsToBits(summedInts), this.mersenneModulo)
    }

    override def toVal[I: Integral]: I =
      bits.foldRight[I](0){ (b, sum) =>
        if(b) sum * 2 + 1
        else  sum * 2
      }
  }
  
  //***** Utility methods *****

  /** This apply() factory method can be used with Seq[Boolean] whose length is not p. */
  def apply(bits: Seq[Boolean], mmod: MersenneModulo): MersenneModuloNumber =
    bits match {
      case Nil => mmod.Zero
      case _ =>
        val p = mmod.p
        // Example: p=3 (mod 7), seq = (10110011)  (1 = T, 0 = F)
        bits.grouped(p).map { // ((101)(100)(11))
          case s if s.length == p =>
            if (s.distinct.size == 1)  // all are true or all are false
              mmod.Zero
            else
              new BooleanBits(s, mmod)
          case s if s.length < p =>
            if(s.forall(_ == false))
              mmod.Zero
            else
              new BooleanBits(s.padTo(p, false), mmod) // ((101)(100)(110))
        }.reduce(_ + _)
    }

//  def sum(seq: Seq[MersenneModuloNumber]): MersenneModuloNumber =
//    MersenneModuloNumber(
//      sumBitByBit(seq.map(_.bits).transpose),
//      seq.head.mersenneModulo)
//
//  private def sumBitByBit(bits: Seq[Seq[Boolean]]): Stream[Boolean] =
//    intsToBits(bits.map(_.count(_ == true)))

  private def intsToBits(seq: Seq[Int]): Stream[Boolean] = {
    def convert(seq: Seq[Int], s: Int): Stream[Boolean] =
      seq match {
        case Nil =>
          if(s == 0) Stream()
          else       toBinaryInAscendingOrder(s).map(_ == 1)
        case x +: xs =>
          val t = x + s
          (t % 2 == 1) #:: convert(xs, t / 2)
      }

    convert(seq, 0)
  }
}