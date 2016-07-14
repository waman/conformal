package org.waman.conformal.integral.combinatorial

import scala.annotation.tailrec
import org.waman.conformal.integral.FactorialRepresentation
import org.waman.conformal._

/**
  * Permutation[E] trait represents (passive) permutations.
  *
  * Example
  *   degree: 4
  *   E (type parameter): Int
  *
  * / 0 1 2 3 \  <- indices (in this order)
  * \ 3 1 0 2 /  <- suffices (in this order)
  *
  *   apply(0) == 3, apply(2) == 0, apply(100) is not defined (maybe throws an exception)
  *   indexOf(3) == 0, indexOf(0) == 2, indexOf(100) is not defined (maybe throws an exception)
  *
  *   apply(Seq(a, b, c, d)) == Seq(d, b, a, c)
  *   apply(Seq(x0, x1, x2, x3)) == Seq(x3, x1, x0, x2)
  *
  *   val x: Seq[E] = ...
  *   apply(x) == Seq(x(3), x(1), x(0), x(2))
  */
trait PassivePermutation[E] extends Combinatorial[E, E]
    with PartialFunction[E, E]
    with Ordered[PassivePermutation[E]] {

  def degree: Int
  def rank: Int = degree

  def indices: Seq[E]
  def suffices: Seq[E] = indices.map(apply)
  def sufficesToIntSeq: Seq[Int] = suffices.map(indices.indexOf)

  override def isDefinedAt(e: E): Boolean = indices.contains(e)

  override def apply(e: E): E

  def apply(seq: Seq[E]): Seq[E] = {
    require(seq.length == degree,
      "The argument Seq must have the same length as the degree of this permutation.")

    seq.map(apply)
  }

  def applyOption(e: E): Option[E] = isDefinedAt(e) match {
    case true  => Some(apply(e))
    case false => None
  }

  def indexOf(e: E): E

  def *(that: PassivePermutation[E]): PassivePermutation[E]
  def andThen(that: PassivePermutation[E]): PassivePermutation[E] = that * this
  def inverse: PassivePermutation[E]

  /**
    * Note that the default implementation calculate sign every time,
    * so it is recommended to override this implementation in subclasses
    */
  def sign: Int = toPermutation.sign
  final def sgn = sign

  //***** Order related *****
  override def compare(that: PassivePermutation[E]): Int =
    toPermutation.compare(that.toPermutation)

  def next: Option[PassivePermutation[E]]

  //***** Methods of Order *****
  /**
    * Return the sequence number in the lexicographic order.
    *
    * Returned object is a FactorialRepresentation one,
    * so if you want it as Int value, call 'toInt' method of it
    * or import 'org.waman.conformal.integral._' to convert type implicitly.
    */
  def sequenceNumber: FactorialRepresentation =
    toPermutation.sequenceNumber

  //***** Type converters *****
  def toPermutation: Permutation = new Permutation.SeqPermutation(sufficesToIntSeq)
  def toMap: Map[E, E] = indices.map{i => (i, apply(i))}.toMap

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: PassivePermutation[E] =>
      that.canEqual(this) &&
        degree == that.degree &&
          indices.forall(i => apply(i) == that.apply(i))
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[PassivePermutation[_]]

  // a call of asInstanceOf method is for suppressing warning
  override def hashCode: Int = (degree +: suffices.asInstanceOf[Seq[Any]]).hashCode

  override def toString: String = suffices.mkString("[", " ", "]")
}

class PassivePermutationFactory[E](val indices: Seq[E]){

  def degree = indices.length

  //***** Implementations of Permutation trait *****
  trait PassivePermutationAdapter extends PassivePermutation[E] {

    override def degree = PassivePermutationFactory.this.degree
    override def indices = PassivePermutationFactory.this.indices

    override def *(that: PassivePermutation[E]) = {
      val thiz = this
      new PassivePermutationAdapter{
        override def degree = thiz.degree
        override def indices = thiz.indices
        override def apply(e: E) = thiz(that(e))
        override def indexOf(e: E) = that.indexOf(thiz.indexOf(e))
        override def sign = thiz.sign * that.sign
      }
    }

    override def inverse = {
      val thiz = this
      new PassivePermutationAdapter{
        override def degree = thiz.degree
        override def indices = thiz.indices
        override def apply(e: E) = thiz.indexOf(e)
        override def indexOf(e: E) = thiz(e)
        override def sign = thiz.sign
      }
    }

    def next: Option[PassivePermutation[E]] = toPermutation.next match {
      case Some(p) => Some(fromPermutation(p))
      case _ => None
    }
  }

  lazy val identity: PassivePermutation[E] = new PassivePermutationAdapter {
    override def apply(e: E) = e
    override def apply(seq: Seq[E]) = seq
    override def indexOf(e: E) = e
    override def suffices: Seq[E] = indices
    override def sign = 1
  }

  abstract class AbstractMapPassivePermutation(map: Map[E, E]) extends PassivePermutationAdapter {
    override def apply(e: E) = map(e)
    override def indexOf(e: E) = map.find(_._2 == e).get._1
    override def toMap = map
  }

  private[integral]
  class MapPassivePermutation(map: Map[E, E]) extends AbstractMapPassivePermutation(map) {

    def apply(suffices: E*): PassivePermutation[E] = {
      val map = indices.zip(suffices).toMap
      new MapPassivePermutation(map)
    }
  }

  def apply(e: E*): PassivePermutation[E] = ???

  def fromPermutation(p: Permutation): PassivePermutation[E] = ???

  //***** Sequence number in lexicographic order *****
  def nthPermutation(n: Int, degree: Int): PassivePermutation[E] =
    nthPermutation(FactorialRepresentation.fromInt(n))

  def nthPermutation(n: FactorialRepresentation): PassivePermutation[E] =
    fromPermutation(Permutation.nthPermutation(n, degree))

  //***** Permutation Generators *****
  protected trait PermutationBuilder[B <: PermutationBuilder[B]]
    extends CombinatorialBuilder[E, B]{

    def suffices: Seq[E]
  }

  def allPermuted: Seq[Seq[E]] = Permutation.generatePermutations(indices.toVector)
  def allPartialPermuted(rank: Int): Seq[Seq[E]] =
    Permutation.generatePermutations(indices.toVector, rank)

  def allPermutations: Seq[PassivePermutation[E]] = allPermuted.map(apply)

  def allPermutedStrings: Seq[String] = allPermuted.map(_.mkString)
  def allPartialPermutedStrings(rank: Int): Seq[String] = allPartialPermuted(rank).map(_.mkString)

  //**** Other permutation generators *****
  private def parityPermutations(arg: Seq[E], parity: Int): Seq[Seq[E]] = {
    require(arg.length > 1)

    case class Builder(suffices: Vector[E], available: Vector[E], sign: Int)
      extends PermutationBuilder[Builder]{

      override def nextGeneration: Seq[Builder] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder], i: Int): Seq[Builder] =
          i match {
            case -1 => accum
            case _  =>
              val newSuffices = suffices :+ available(i)
              val newAvailable = removeAt(available, i)
              val newSign = if(i % 2 == 0) sign else -sign
              val newBuilder = Builder(newSuffices, newAvailable, newSign)
              nextGeneration(newBuilder +: accum, i-1)
          }

        nextGeneration(Nil, available.length-1)
      }
    }

    val init = Builder(Vector(), arg.toVector, 1)

    generateCombinatorial(init, arg.length-2).map{
      case b if b.sign == parity =>
        b.suffices ++: b.available
      case b =>
        b.suffices ++: b.available.reverse
    }
  }

  def evenPermuted: Seq[Seq[E]] = degree match {
    case 1 => Seq(indices)
    case _ => parityPermutations(indices, 1)
  }

  def evenPermutations: Seq[PassivePermutation[E]] = evenPermuted.map(apply)

  def oddPermuted: Seq[Seq[E]] = degree match {
    case 1 => Nil
    case _ => parityPermutations (indices, -1)
  }

  def oddPermutations: Seq[PassivePermutation[E]] = oddPermuted.map(apply)

  def derangements: Seq[Seq[E]] = ???
  def derangementPermutations: Seq[PassivePermutation[E]] = Permutation.derangements(degree).map(fromPermutation)
}