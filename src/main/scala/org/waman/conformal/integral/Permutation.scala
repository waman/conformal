package org.waman.conformal.integral

import org.waman.conformal._

import scala.annotation.tailrec

trait Permutation extends PartialFunction[Int, Int] with Ordered[Permutation]{

  def degree: Int
  protected def indices: Range = 0 until degree
  protected def towards: Seq[Int] = indices.map(apply(_))

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
      override def degree: Int = th.degree
      override def apply(i: Int): Int = th(p(i))
      override def sgn: Int = th.sgn * p.sgn
    }
  }

  def andThen(p: Permutation): Permutation = p * this

  def inverse: Permutation = {
    val th = this
    new Permutation {
      override def degree: Int = th.degree
      override def apply(i: Int): Int = th.indexOf(i)
      override def indexOf(i: Int): Int = th.apply(i)
      override def inverse: Permutation = th
      override def sgn: Int = th.sgn
    }
  }

  def sgn: Int

  //***** Order related *****
  override def compare(that: Permutation): Int =
    indices.map(i => (apply(i), that(i))).find(p => p._1 != p._2) match {
      case None => 0
      case Some((x, y)) =>
        if(x < y) -1
        else       1
    }

  def next: Option[Permutation] = degree match {
    case 1 => None
    case _ =>
      val to = towards
      to.sliding(2).toSeq.lastIndexWhere(p => p.head < p(1)) match {
        case -1 => None
        case i =>  //  case for args: to = Seq(0, 2, 4, 1, 5, 3), i = 3 (1 < 5)
          val a = apply(i)  // a = 1
          val j = to.lastIndexWhere(p => a < p)  // j = 5 (1 < 3)
          val swapped = swap(to, i, j)  // swapped = Seq(0, 2, 4, 3, 5, 1)

          val (first, second) = swapped.splitAt(i+1)  // first = Seq(0, 2, 4, 3), second = Seq(5, 1)
          val newTo = first ++ second.reverse  // newTo = Seq(0, 2, 4, 3, 1, 5)
          Some(new ListPermutation(newTo.toList))
      }
  }

  //***** Methods of Order *****
  /** Return the sequence number in the lexicographic order */
  def sequenceNumber: Int = sequenceNumberInFactorialRepresentation.toInt

  def sequenceNumberInFactorialRepresentation: FactorialRepresentation = {
    @tailrec
    def sequenceNumber(fact: Vector[Int], towards: Seq[Int]): Seq[Int] =
      towards.length match {
        case 1 => fact :+ towards.head
        case _ =>
          val a = towards.head
          val seq = towards.map(i => if(i > a) i-1 else i)
          sequenceNumber(fact :+ seq.head, seq.tail)
      }

    val fact = sequenceNumber(Vector(), towards)
    FactorialRepresentation(fact.init.toList)
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

  override def toString: String = towards.mkString("[", " ", "]")
}

object Permutation{

  def identity(deg: Int): Permutation = new Permutation {
    override def degree: Int = deg
    override protected def towards: Seq[Int] = indices
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

  private[Permutation] class SignedPermutation(towards: List[Int], val sgn: Int)
    extends AbstractListPermutation(towards){

    def generateHigherPermutations: Seq[SignedPermutation] =
      (degree to 0 by -1).map{ i =>
        val (first, second) = towards.splitAt(i)
        val newTo = first ::: degree :: second
        val newSign = if((degree-i) % 2 == 0) sgn else -sgn
        new SignedPermutation(newTo, newSign)
      }
  }

  private[Permutation] def signed(to: List[Int], sign: Int): Permutation
    = new SignedPermutation(to, sign)

  def nthPermutation(n: Int, degree: Int): Permutation =
    nthPermutation(FactorialRepresentation.fromInt(n), degree)

  def nthPermutation(n: FactorialRepresentation, degree: Int): Permutation = {
    @tailrec
    def nthPermutation(towards: List[Int], fact: Vector[Int]): List[Int] = fact.isEmpty match {
      case true  => towards
      case false =>
        val a = fact.last
        val newTowards = a +: towards.map(i => if(i >= a) i+1 else i)
        nthPermutation(newTowards, fact.init)
    }

    val towards = nthPermutation(Nil, n.coefficientsInDescendantWithFixedLength(degree-1).toVector :+ 0)
    new ListPermutation(towards)
  }
}

abstract class AbstractListPermutation(override protected val towards: List[Int]) extends Permutation{

  override protected def indices: Range = towards.indices
  override def degree: Int = towards.length

  override def apply(i: Int): Int = towards(i)
  override def indexOf(i: Int): Int = towards.indexOf(i)

  override def toString: String = towards.mkString("[", " ", "]")
}

class ListPermutation(towards: List[Int]) extends AbstractListPermutation(towards){

  require(degree > 0, "The degree of permutation must be positive: " + degree)
  require(indices.forall(towards contains _),
    "The constructor arguments of ListPermutation must contain all integers from 0 until "+ degree)

  override lazy val sgn: Int = {  // (024153)
    @tailrec
    def calculateSign(sign: Int, list: List[Int], n: Int): Int = list match {
      case _ :: Nil => sign
      case head :: _ if head == n => calculateSign(sign, list.tail, n+1)
      case _ =>  // case for args: list = List(2, 4, 1, 5, 3), n = 1
        val i = list.indexOf(n)  // i = 2
        val swapped = swap(list, 0, i) // swapped = List(1, 4, 2, 5, 3)
        calculateSign(-sign, swapped.tail, n+1)
    }

    calculateSign(1, towards, 0)
  }
}