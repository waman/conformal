package org.waman.conformal.number.integer.mod

import org.waman.conformal.number.integer.MersenneNumber
import spire.implicits._
import spire.math.Integral

class MersenneModulus(val p: Int) extends Modulus{

  require(p >= 0, s"p must be non-negative: $p")

  val mask = BigInt(2)**p - 1
  // For p = 3 :
  //   mask = 00...0111
  //  ~mask = 11...1000

  def valueAsMersenneNumber: MersenneNumber = MersenneNumber(p)
  override def valueAs[I: Integral] = implicitly[Integral[I]].fromBigInt(mask)

  override def apply[I: Integral](n: I): MersenneModularNumber =
    if(n < 0) -apply(-n)
    else      fromBigInt(n.toBigInt)

  private def fromBigInt(n: BigInt): MersenneModularNumber = {

    def separateByPBits(n: BigInt): Stream[BigInt] = n & ~mask match {
      case x if x == 0 =>
        if(n == 0 | n == mask)
          Stream(0)
        else
          Stream(n)
      case x =>
        (n & mask) #:: separateByPBits(n >> p)
    }

    separateByPBits(n).map(new MersenneModularNumber(this, _)).reduce(_ + _)
  }

  override def create[I: Integral](n: I) =
    new MersenneModularNumber(this, n.toBigInt)

  override def equals(other: Any): Boolean =
    other match {
      case that: MersenneModulus =>
        that.canEqual(this) &&
          this.p == that.p
      case that: Modulus =>
        that.canEqual(this) &&
          super.equals(that)
      case _ => false
    }

  override protected def hasTheSameValueAs(other: Modulus) = other match {
    case that: MersenneModulus => this.p == that.p
    case _ => valueAs[BigInt] == other.valueAs[BigInt]
  }

  override def hashCode = (getClass, p).##
}

object MersenneModulus{

  def apply(p: Int): MersenneModulus = new MersenneModulus(p)
}