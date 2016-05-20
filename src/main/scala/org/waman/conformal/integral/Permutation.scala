package org.waman.conformal.integral

import scala.annotation.tailrec
import org.waman.conformal._

trait Permutation extends PartialFunction[Int, Int]{

  val degree: Int
  protected def to: Seq[Int]
  protected def indices: Range = 0 until degree

  override def isDefinedAt(i: Int): Boolean = indices contains i

  override def apply(i: Int): Int

  /** This method should be overridden by subclasses.  */
  def indexOf(i: Int): Int = indices.find(apply(_) == i).get

  def apply[E](seq: Seq[E]): Seq[E] = {
    require(seq.length == degree,
      "The argument Seq must have the same length as the degree of this permutation.")
    indices.map(indexOf).map(seq(_))
  }

  def apply[E](list: List[E]): List[E] = apply(list: Seq[E]).toList

  def *(p: Permutation): Permutation = {
    require(degree == p.degree)
    val th = this
    new Permutation {
      override val degree: Int = th.degree
      override protected def to: Seq[Int] = th.indices.map(p(_))
      override def apply(i: Int): Int = th(p(i))
      override def sgn: Int = th.sgn * p.sgn
    }
  }

  def andThen(p: Permutation): Permutation = p * this

  def inverse: Permutation = {
    val th = this
    new Permutation {
      override val degree: Int = th.degree
      override protected def to: Seq[Int] = th.indices.map(th.indexOf)
      override def apply(i: Int): Int = th.indexOf(i)
      override def indexOf(i: Int): Int = th.apply(i)
      override def inverse: Permutation = th
      override def sgn: Int = th.sgn
    }
  }

  def sgn: Int

  //***** Order related *****
  def next: Option[Permutation] = degree match {
    case 1 => None
    case _ =>
      to.sliding(2).toSeq.lastIndexWhere(p => p.head < p(1)) match {
        case -1 => None
        case i =>  //  case for args: list = List(0, 2, 4, 1, 5, 3), i = 3 (1 < 5)
          val a = to(i)  // a = 1
          val j = to.lastIndexWhere(p => a < p)  // j = 5 (1 < 3)
          val swapped = swap(to, i, j)  // swapped = List(0, 2, 4, 3, 5, 1)

          @tailrec
          def swapTail(to: Seq[Int], i: Int, j: Int): Seq[Int] =
            if(i < j) swapTail(swap(to, i, j), i+1, j-1)
            else      to

//          val newTo = swapTail(swapped, i+1, degree-1)  // newTo = List(0, 2, 4, 3, 1, 5)
          val (first, second) = swapped.splitAt(i+1)  // first = List(0, 2, 4, 3), second = List(5, 1)
          val newTo = first ++ second.reverse  // newTo = List(0, 2, 4, 3, 1, 5)
          Some(new ListPermutation(newTo.toList))
      }
  }

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: Permutation =>
      that.canEqual(this) &&
      degree == that.degree &&
      indices.forall(i => apply(i) == that.apply(i))
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Permutation]

  override def hashCode: Int = (degree +: apply(indices)).hashCode

  override def toString: String = indices.map(apply(_)).mkString("[", " ", "]")
}

object Permutation{

  def identity(deg: Int): Permutation = new Permutation {
    override val degree: Int = deg
    override protected def to: Seq[Int] = indices
    override def apply(i: Int): Int = i
    override def indexOf(i: Int): Int = i
    override def *(p: Permutation): Permutation = p
    override def inverse: Permutation = this
    override def sgn: Int = 1
    override def toString: String = indices.mkString("[", " ", "]")
  }

  def apply(to: Int*): Permutation = new ListPermutation(to.toList)

  def allPermutations(degree: Int): Seq[Permutation] = degree match {
    case 1 => Seq(identity(degree))
    case 2 => Seq(identity(degree), signed(List(1, 0), -1))
    case 3 => Seq(identity(degree), signed(List(0, 2, 1), -1),
      signed(List(1, 0, 2), -1), signed(List(1, 2, 0),  1),
      signed(List(2, 0, 1),  1), signed(List(2, 1, 0), -1))
    case _ =>
      @tailrec
      def generateSignedPermutations(seq: Seq[SignedPermutation], n: Int): Seq[SignedPermutation] =
        n match {
          case 1 => seq
          case _ =>
            val newSeq = seq.flatMap(_.generateHigherPermutations)
            generateSignedPermutations(newSeq, n-1)
        }

      val p1 = new SignedPermutation(List(0), 1)
      generateSignedPermutations(Stream(p1), degree)
  }

  private[Permutation] class SignedPermutation(protected val to: List[Int], val sgn: Int)
    extends AbstractListPermutation{

    def generateHigherPermutations: Seq[SignedPermutation] =
      (degree to 0 by -1).map{ i =>
        val (first, second) = to.splitAt(i)
        val newTo = first ::: degree :: second
        val newSign = if((degree-i) % 2 == 0) sgn else -sgn
        new SignedPermutation(newTo, newSign)
      }
  }

  private[Permutation] def signed(to: List[Int], sign: Int): Permutation
  = new SignedPermutation(to, sign)
}

abstract class AbstractListPermutation extends Permutation{

  protected val to: List[Int]

  override val degree: Int = to.length
  override protected def indices: Range = to.indices

  override def apply(i: Int): Int = to(i)
  override def indexOf(i: Int): Int = to.indexOf(i)

  override def toString: String = to.mkString("[", " ", "]")
}

class ListPermutation(protected val to: List[Int]) extends AbstractListPermutation{

  require(degree > 0, "The degree of permutation must be positive: " + degree)
  require(indices.forall(to contains _),
    "The constructor arguments of ListPermutation must contain all integers from 0 until "+ degree)

  override lazy val sgn: Int = {  // (024153)
    @tailrec
    def calculateSign(sign: Int, list: List[Int], n: Int): Int = list match {
      case _ :: Nil => sign
      case head :: _ if head == n => calculateSign(sign, list.tail, n+1)
      case _ =>  // case for args: list = List(2, 4, 1, 5, 3), n = 1
        val i = list.indexOf(n)  // i = 2
        val (first, second) = list.splitAt(i)  // first = List(2, 4), second = List(1, 5, 3)
        val newList = first.tail ::: first.head :: second.tail
        // newList = List(4, 2, 5, 3) ( = List(4) ::: 2 :: List(5, 3))
        calculateSign(-sign, newList, n+1)
    }

    calculateSign(1, to, 0)
  }
}