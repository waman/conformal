//package org.waman.conformal.number.integer.mod
//
//import org.waman.conformal.number.integer.{BigIntScalaIntegralNumber, IntScalaIntegralNumber, LongScalaIntegralNumber, MersenneNumber}
//import spire.implicits._
//import spire.math.{Integral, UInt}
//
//trait MersenneModulus extends Modulus{
//
//  def p: Int
//
////  def mask[I: Integral]: I = implicitly[Integral[I]].fromInt(2)**p - 1
//  // For p = 3 :
//  //   mask = 00...0111
//  //  ~mask = 11...1000
//  lazy val mask: BigInt = BigInt(2)**p - 1
//
//  override def toInt: Int = 2**p - 1
//  override def toLong: Long = 2L**p - 1L
//  override def toBigInt: BigInt = mask
//  override def valueAs[I: Integral]: I = implicitly[Integral[I]].fromBigInt(2)**p - 1
//  def toMersenneNumber: MersenneNumber = MersenneNumber(p)
//
//  override def apply[I: Integral](n: I): MersenneModularNumber
//
//  override def equals(other: Any): Boolean =
//    other match {
//      case that: MersenneModulus =>
//        that.canEqual(this) &&
//          this.p == that.p
//      case that: Modulus =>
//        that.canEqual(this) &&
//          super.equals(that)
//      case _ => false
//    }
//
//  override def hashCode: Int = (getClass, p).##
//  override def valueAsString: String = s"2^$p-1"
//}
//
//object MersenneModulus{
//
//  //********** apply() factory method **********
//  def apply(p: Int): MersenneModulus = {
//    require(p > 0, s"p must be positive: $p")
//    if(0 <= p && p <= 32)
//      new IntMersenneModulus(p)
//    else if(p <= 64)
//      new LongMersenneModulus(p)
//    else
//      new BigIntMersenneModulus(p)
//  }
//
//  //********** Class Implementation **********
//  //***** Modulus *****
//  private[mod] abstract class MersenneModulusAdapter(val p: Int) extends MersenneModulus{
//
//    override def apply[I: Integral](n: I): MersenneModularNumber = {
//      val algebra = implicitly[Integral[I]]
//      def maxInt = algebra.fromInt(Int.MaxValue)
//      def maxLong = algebra.fromLong(Long.MaxValue)
//
//      n match {
//        case _ if n < 0        =>
//          n match {
//            case _ if n == Int.MinValue  => -fromLong(Int.MaxValue + 1L)
//            case _ if n == Long.MinValue => -fromBigInt(BigInt(Long.MaxValue) + 1)
//            case _ => -apply(-n)
//          }
//
//        case _ if n <= maxInt  => fromInt(n.toInt)
//        case _ if n <= maxLong => fromLong(n.toLong)
//        case _                 => fromBigInt(n.toBigInt)
//      }
//    }
//
//    protected def fromInt(n: Int): MersenneModularNumber
//    protected def fromLong(n: Long): MersenneModularNumber
//    protected def fromBigInt(n: BigInt): MersenneModularNumber
//  }
//
//  private[mod] class IntMersenneModulus(p: Int)
//      extends MersenneModulusAdapter(p) with IntScalaIntegralNumber{
//
//    override def value: Int = 2**p-1
//
//    override protected def fromInt(n: Int) = create(n % value)
//    override protected def fromLong(n: Long) = create((n % value).toInt)
//    override protected def fromBigInt(n: BigInt) = create((n % value).toInt)
//
//    private def create(n: Int) = new ModularInt(this, n)
//
//    override protected def hasTheSameValueAs(that: Modulus): Boolean =
//      toInt == that.toInt
//  }
//
//  private[mod] class LongMersenneModulus(p: Int)
//      extends MersenneModulusAdapter(p) with LongScalaIntegralNumber{
//
//    override def value: Long = 2L**p-1L
//
//    override protected def fromInt(n: Int) = create(n)
//    override protected def fromLong(n: Long) = create(n % value)
//    override protected def fromBigInt(n: BigInt) = create((n % value).toLong)
//
//    private def create(n: Long) = new ModularLong(this, n)
//
//    override protected def hasTheSameValueAs(that: Modulus): Boolean =
//      toLong == that.toLong
//  }
//
//  private[mod] class BigIntMersenneModulus(p: Int)
//      extends MersenneModulusAdapter(p) with BigIntScalaIntegralNumber{
//
//    override def value: BigInt = BigInt(2)**p-1
////    else {
////      val mask = toBigInt
////
////      def separateByPBits(i: BigInt): Stream[BigInt] = i & ~mask match {
////        case x if x == 0 =>
////          if(i == 0 | i == mask)
////            Stream(0)
////          else
////            Stream(i)
////        case x =>
////          (i & mask) #:: separateByPBits(i >> p)
////      }
////
////      separateByPBits(n.toBigInt).map(create(_)).reduce(_ + _)
////    }
//
//    override protected def fromInt(n: Int) = create(n)
//    override protected def fromLong(n: Long) = create(n)
//    override protected def fromBigInt(n: BigInt) = create(n % value)
//
//    private def create(n: BigInt) = new ModularBigInt(this, n)
//
//    override protected def hasTheSameValueAs(that: Modulus): Boolean =
//      toBigInt == that.toBigInt
//  }
//
//  //***** ModularNumber *****
//}