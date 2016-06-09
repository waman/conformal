package org.waman.conformal.integral

import org.waman.conformal._

import scala.annotation.tailrec
import scala.collection.immutable.LinearSeq

trait Permutation extends PartialFunction[Int, Int] with Ordered[Permutation]{

  def degree: Int
  protected def indices: Range = 0 until degree
  protected def towards: Seq[Int] = indices.map(apply(_))

  override def isDefinedAt(i: Int): Boolean = indices.contains(i)

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
          Some(new Permutation.SeqPermutation(newTo))
      }
  }

  //***** Methods of Order *****
  /** Return the sequence number in the lexicographic order */
  def sequenceNumber: Int = sequenceNumberInFactorialRepresentation.toInt

  def sequenceNumberInFactorialRepresentation: FactorialRepresentation = {
    @tailrec
    def sequenceNumber(fact: List[Int], towards: Vector[Int]): List[Int] =
      towards.isEmpty match {
        case true  => fact
        case false =>
          val (init, last) = (towards.init, towards.last)
          val n = last - init.count(_ < last)
          sequenceNumber(n::fact, init)
      }

    val fact = sequenceNumber(Nil, towards.toVector.init)  // The last place (place of 0!) is removed

//    // another implementation
//    @tailrec
//    def sequenceNumber(fact: Vector[Int], towards: List[Int]): List[Int] =
//      towards.length match {
//        case 1 => fact  // The last place (place of 0!) is ignored
//        case _ =>
//          val a = towards.head
//          val list = towards.map(i => if(i > a) i-1 else i)
//          sequenceNumber(fact :: list.head, list.tail)
//      }
//
//    val fact = sequenceNumber(Vector(), towards).toList

    FactorialRepresentation(fact)
  }


  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: Permutation =>
      that.canEqual(this) &&
        degree == that.degree &&
          towards == that.towards  // indices.forall(i => apply(i) == that.apply(i))
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Permutation]

  override def hashCode: Int = (degree +: apply(indices)).hashCode

  override def toString: String = towards.mkString("[", " ", "]")
}

object Permutation{

  private[Permutation]
  abstract class AbstractSeqPermutation(override protected val towards: Seq[Int]) extends Permutation{

    override protected def indices: Range = towards.indices
    override def degree: Int = towards.length

    override def apply(i: Int): Int = towards(i)
    override def indexOf(i: Int): Int = towards.indexOf(i)

    override def toString: String = towards.mkString("[", " ", "]")
  }

  private[Permutation]
  class SeqPermutation(towards: Seq[Int]) extends AbstractSeqPermutation(towards){

    override lazy val sgn: Int = {  // (024153)
    @tailrec
    def calculateSign(sign: Int, towards: Seq[Int], n: Int): Int = towards match {
        case _ +: Nil => sign
        case head +: _ if head == n => calculateSign(sign, towards.tail, n+1)
        case _ =>  // case for args: list = Seq(2, 4, 1, 5, 3), n = 1
          val i = towards.indexOf(n)  // i = 2

//          val swapped = swap(towards, 0, i) // swapped = Seq(1, 4, 2, 5, 3)
//          val newTowards = swapped.tail
          val newTowards = towards.updated(i, towards.head).tail  // newTowards = Seq(4, 2, 5, 3)

          calculateSign(-sign, newTowards, n+1)
      }

      calculateSign(1, towards.toVector, 0)
    }
  }

  private[Permutation]
  class SignedPermutation(towards: Vector[Int], val sgn: Int)
      extends AbstractSeqPermutation(towards){

    def generateHigherPermutations: Seq[SignedPermutation] =
      (degree to 0 by -1).map{ i =>
        val newTo = insertAt(towards, i, degree)
        val newSign = if((degree-i) % 2 == 0) sgn else -sgn
        new SignedPermutation(newTo, newSign)
      }
  }

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

  //***** apply constructors *****
  // apply constructors validate the arguments as well as instantiation
  def apply(towards: Int*): Permutation = apply(towards.toList)

  private def validatePermutationArguments(towards: Seq[Int]): Unit = {
    val degree = towards.length

    require(degree > 0, "The degree of permutation must be positive: " + degree)
    require(towards.indices.forall(i => towards.count(_ == i) == 1),
      "The constructor arguments of SeqPermutation must contain all integers once from 0 until "+ degree)
  }

  def apply(towards: LinearSeq[Int]): Permutation = {
    validatePermutationArguments(towards)
    new SeqPermutation(towards)
  }

  def apply(towards: IndexedSeq[Int]): Permutation = {
    validatePermutationArguments(towards)
    new SeqPermutation(towards)
  }

  private lazy val allPermutationsOfOrder1 = Seq(identity(1))
  private lazy val allPermutationsOfOrder2 = Seq(identity(2), signed(Vector(1, 0), -1))
  private lazy val allPermutationsOfOrder3 = Seq(
    identity(3),
    signed(Vector(0, 2, 1), -1),
    signed(Vector(1, 0, 2), -1),
    signed(Vector(1, 2, 0),  1),
    signed(Vector(2, 0, 1),  1),
    signed(Vector(2, 1, 0), -1))

  def allPermutations(degree: Int,
                      beOrdered: Boolean = true,
                      calculateSign: Boolean = false): Seq[Permutation] =
    degree match {
      case 1 => allPermutationsOfOrder1
      case 2 => allPermutationsOfOrder2
      case 3 => allPermutationsOfOrder3
      case _ =>
        if(beOrdered){
          allPermutationsInLexicographicOrder(degree)
        }else{
          if(calculateSign)
            allPermutationsWithSignCalculated(degree)
          else
            Nil  // TODO
        }
    }

  private[integral] def allPermutationsInLexicographicOrder(degree: Int): Seq[Permutation] = {

    case class PermutationInfo(towards: Vector[Int], available: Vector[Int])

    @tailrec
    def generatePermutations(accum: Stream[PermutationInfo], n: Int): Stream[PermutationInfo] =
      n match {
        case 0 => accum
        case _ =>
          val newAccum = accum.flatMap{ info =>
            @tailrec
            def nextPermutationInfo(seq: Seq[PermutationInfo], i: Int): Seq[PermutationInfo] =
              i match {
                case -1 => seq
                case _  =>
                  val newTowards = info.towards :+ info.available(i)
                  val newAvailable = removeAt(info.available, i)
                  nextPermutationInfo(PermutationInfo(newTowards, newAvailable) +: seq, i-1)
              }

            nextPermutationInfo(Nil, n-1)
          }
          generatePermutations(newAccum, n-1)
      }

    val info = PermutationInfo(Vector(), (0 until degree).toVector)
    generatePermutations(Stream(info), degree).map(info => new SeqPermutation(info.towards))
  }

  private[integral] def allPermutationsWithSignCalculated(degree: Int): Seq[Permutation] = {
    @tailrec
    def generatePermutations(stream: Stream[SignedPermutation], n: Int): Stream[SignedPermutation] =
      n match {
        case 1 => stream
        case _ =>
          val newStream = stream.flatMap(_.generateHigherPermutations)
          generatePermutations(newStream, n-1)
      }

    val p1 = signed(Vector(0), 1)
    generatePermutations(Stream(p1), degree)
  }

  private[Permutation] def signed(to: Vector[Int], sign: Int): SignedPermutation =
    new SignedPermutation(to, sign)

  //***** Sequence number in lexicographic order *****
  def nthPermutation(n: Int, degree: Int): Permutation =
    nthPermutation(FactorialRepresentation.fromInt(n), degree)

  def nthPermutation(n: FactorialRepresentation, degree: Int): Permutation = {
    @tailrec
    def nthPermutation(towards: Vector[Int], rest: Seq[Int], fact: Seq[Int]): Vector[Int] = fact match {
      case Nil => towards :+ rest.head
      case 0 +: tail =>
        nthPermutation(towards :+ rest.head, rest.tail, tail)
      case head +: tail =>
        // case for towards = Vector(), rest = Seq(0, 1, 2, 3, 4, 5), fact = Seq(3, 1, 0, 2, 1)
        nthPermutation(towards :+ rest(head), removeAt(rest, head), tail)
          // next call arg: towards = Vector(3), rest = Seq(0, 1, 2, 4, 5), fact = Seq(1, 0, 2, 1)
    }

    val cs = n.coefficientsAsNthOrderInDescendant(degree-1)
    val towards = nthPermutation(Vector(), 0 until degree, cs).toList

//    @tailrec
//    def nthPermutation(towards: List[Int], fact: Vector[Int]): List[Int] = fact.isEmpty match {
//      case true  => towards
//      case false =>
//        val a = fact.last
//        val newTowards = a :: towards.map(i => if(i >= a) i+1 else i)
//        nthPermutation(newTowards, fact.init)
//    }
//
//    val towards = nthPermutation(Nil, n.coefficientsAsNthOrderInDescendant(degree-1).toVector :+ 0)

    new SeqPermutation(towards)
  }
}