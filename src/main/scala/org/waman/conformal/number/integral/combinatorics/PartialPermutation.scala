package org.waman.conformal.number.integral.combinatorics

import org.waman.conformal.ForImplementationInterest

import scala.annotation.tailrec
import spire.math.Integral
import spire.implicits._

/**
  * The apply method is place-base permutation
  *
  * Example
  *   degree: 4
  *   rank: 3
  *
  * / 0 1 (2) 3 \  <- indices (containing 2)
  * \ 2 1 (-) 0 /  <- suffices
  *
  * = / 3 1 0 (2) \  <- proper indices in this order (NOT containing 2)
  *   \ 0 1 2 (-) /
  *
  * In the above example, an object at place 3 is moved to place 0
  */
trait PartialPermutation
    extends Combinatorial[Option[Int]]
    with Ordered[PartialPermutation]{

  def properIndices: Seq[Int]

  override def apply[E](seq: Seq[E]): Seq[E] = properIndices.map(seq)

  //***** Order related *****
  override def compare(that: PartialPermutation): Int = {
    Combinatorial.validateComparablity(this, that)

    properIndices.zip(that.properIndices).find(p => p._1 != p._2) match {
      case None => 0
      case Some((x, y)) => if (x < y) -1 else 1
    }
  }

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: PartialPermutation =>
      that.canEqual(this) &&
        degree == that.degree &&
        properIndices == that.properIndices
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[PartialPermutation]

  // a call of asInstanceOf method is for suppressing warning
  override def hashCode: Int = (degree, properIndices).##

  override def toString: String =
    (properIndices.map(_.toString) ++: Seq.fill(degree-rank)("-")).mkString("[[", " ", "]]")
}

object PartialPermutation{

  //***** Partial permutation count *****
  def permutationCount[I: Integral](degree: I, rank: I): I = {
    require(degree >= 0, "The degree must be non-negative")
    require(0 <= rank && rank <= degree, s"The rank must be in [0, $degree]")

    @tailrec
    def permutationCount(prod: I, n: I, r: I): I = r match {
      case 0 => prod
      case _ => permutationCount(prod * n, n-1, r-1)
    }

    permutationCount(1, degree, rank)
  }

  @ForImplementationInterest
  private[integral]
  def permutationCount1(n: Long, r: Long): Long =
    (n until (n - r) by -1).product

  //***** apply() factory method *****
  def apply(degree: Int, properIndices: Seq[Int]): PartialPermutation = {
    Combinatorial.validateArgument(degree, properIndices, "proper indices")
    new SeqPartialPermutation(degree, properIndices.toVector)
  }

  class PartialPermutationFactory(val degree: Int){

    def apply(properIndices: Int*): PartialPermutation =
      PartialPermutation.this.apply(degree, properIndices.toVector)
  }

  def apply(degree: Int): PartialPermutationFactory =
    new PartialPermutationFactory(degree)

  //***** Permutation generation *****
  private[combinatorics]
  class SeqPartialPermutation(val degree: Int, val properIndices: Seq[Int])
      extends PartialPermutation{

    override def rank = properIndices.length
    override def apply(i: Int) = properIndices.indexOf(i) match {
      case -1 =>
        if(indices.contains(i)) None
        else throw new IllegalArgumentException(s"The argument Int must be in [0, $degree): $i")
      case n  => Some(n)
    }
  }

  def allPermutations[E](arg: Seq[E], rank: Int): Seq[Seq[E]] ={

    case class Builder(seq: Vector[E], available: Seq[E])
      extends CombinatorialBuilder[E, Builder]{

      override def nextGeneration: Seq[Builder] =
        available.map(e => Builder(seq :+ e, available.filter(_ != e)))
    }

    val start = Builder(Vector(), arg)
    generateCombinatorials(start, rank).map(_.seq)
  }

  def allPermutations(degree: Int, rank: Int): Seq[PartialPermutation] =
    allPermutations(0 until degree, rank).map(new SeqPartialPermutation(degree, _))

  def allPermutations(s: String, rank: Int): Seq[String] =
    allPermutations(s: Seq[Char], rank).map(_.mkString)
}