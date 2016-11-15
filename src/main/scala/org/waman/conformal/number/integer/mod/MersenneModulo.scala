package org.waman.conformal.number.integer.mod

import org.waman.conformal.number.integer.MersenneNumber
import spire.implicits._
import spire.math.Integral

class MersenneModulo(val p: Int) extends Modulo[BigInt]{

  require(p >= 0, s"p must be zero or positive: $p")

  def moduleInMersenneNumber: MersenneNumber = MersenneNumber(p)
  override def module = mask

  override protected def algebra = Integral[BigInt]

  private[integer] val mask = BigInt(2)**p - 1
  // For p = 3 :
  //   mask = 00...0111
  //  ~mask = 11...1000

  def apply(n: Int): MersenneModuloNumber = apply(n.toBigInt)
  def apply(n: Long): MersenneModuloNumber = apply(n.toBigInt)

  override def apply(n: BigInt): MersenneModuloNumber =
    if(n < 0) -apply(-n)
    else {
      def separateByPBits(n: BigInt): Stream[BigInt] = n & ~mask match {
        case x if x == 0 =>
          if(n == 0 | n == mask)
            Stream(0)
          else
            Stream(n)
        case x =>
          (n & mask) #:: separateByPBits(n >> p)
      }

      separateByPBits(n).map(new MersenneModuloNumber(_, this)).reduce(_ + _)
    }

  override def equals(other: Any): Boolean =
    other match {
      case that: MersenneModulo =>
        that.canEqual(this) &&
          this.p == that.p
      case that: Modulo[_] =>
        that.canEqual(this) &&
          super.equals(that)
      case _ => false
    }

  override def hashCode: Int = p.##
}

object MersenneModulo{

  def apply(p: Int): MersenneModulo = new MersenneModulo(p)
}