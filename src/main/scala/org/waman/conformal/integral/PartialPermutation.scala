package org.waman.conformal.integral

import org.waman.conformal._
import org.waman.conformal.integral.Permutation.PermutationBuilder
import spire.math.Integral

import scala.annotation.tailrec
import spire.implicits._

import scala.collection.LinearSeq
import scala.collection.IndexedSeq

/**
  * / 3 0 1 (2) \ = / 0 1 (2) 3 \    (a, b, c, d) -> (c, a, b)
  * \ 0 1 2 (-) /   \ 1 2 (-) 0 /
  */
trait PartialPermutation extends PartialFunction[Int, Int]
    with Ordered[PartialPermutation]{

  def degree: Int
  def rank:Int

  def indices: Seq[Int] = 0 until degree

  /**
    * Return Seq(3, 0, 1) for the following partial permutation:
    *   / 3 0 1 (2) \
    *   \ 0 1 2 (-) /
    *
    */
  def properIndices: Seq[Int]

  override def isDefinedAt(i: Int): Boolean
  override def apply(i: Int): Int

  def applyOption(i: Int): Option[Int] =
    if(isDefinedAt(i)) Some(apply(i))
    else               None

  def apply[E](seq: Seq[E]): Seq[E] = properIndices.map(seq(_))
  def apply(s: String): String = apply(s: Seq[Char]).mkString("")

  def indexOf(i: Int): Int

  //***** Order related *****
  override def compare(that: PartialPermutation): Int = {
    require(degree == that.degree,
      s"Two PartialPermutations can be compared when these degrees are the same values: $degree <=> ${that.degree}")
    require(rank == that.rank,
      s"Two PartialPermutations can be compared when these ranks are the same values: $rank <=> ${that.rank}")

    indices.map(i => (apply(i), that(i))).find(p => p._1 != p._2) match {
      case None => 0
      case Some((x, y)) =>
        if (x < y) -1
        else 1
    }
  }

  //***** Type converters *****
  def toMap: Map[Int, Option[Int]] = indices.map{i => (i, applyOption(i))}.toMap

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: PartialPermutation =>
      that.canEqual(this) &&
        degree == that.degree &&
        properIndices == that.properIndices
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[PartialPermutation]

  override def hashCode: Int = (degree +: rank +: properIndices).hashCode

  override def toString: String = indices.map(i => (i, applyOption(i))).map{
    case (i, Some(x)) => i + " -> " + x
    case (i, None)    => i + " -> None"
  }.mkString("[", ", ", "]")
}

object PartialPermutation{

  def permutationCount[I: Integral](n: I, r: I): I = {
    @tailrec
    def permutationCount(prod: I, n: I, r: I): I = r match {
      case 0 => prod
      case _ => permutationCount(prod * n, n-1, r-1)
    }

    permutationCount(1, n, r)
  }

  /**
    * Note that this class has indices (not permuted indices) as a field.
    *
    * Example
    *   degree = 4
    *   indices = Seq(3, 0, 1)
    *
    * / 3 0 1 (2) \ = / 0 1 (2) 3 \    (a, b, c, d) -> (c, a, b)
    * \ 0 1 2 (-) /   \ 1 2 (-) 0 /
    */
  private[PartialPermutation]
  class SeqPartialPermutation(val degree: Int, val properIndices: Seq[Int]) extends PartialPermutation{

    override def rank: Int = properIndices.length

    override def isDefinedAt(i: Int): Boolean = properIndices.contains(i)

    override def apply(i: Int): Int = properIndices.indexOf(i) match {
      case -1 => throw new IllegalArgumentException()
      case n => n
    }

    override def indexOf(i: Int): Int = properIndices(i)
  }

  //***** apply() factory methods *****
  private[PartialPermutation]
  def validatePartialPermutationArguments(degree: Int, properIndices: Seq[Int]): Unit = {
    require(degree > 0, "The degree of partial permutation must be positive: " + degree)

    val range = 0 until degree

    range.foreach { i =>
      val c = properIndices.count(_ == i)
      require(c == 0 || c == 1,
        "The constructor arguments of partial permutation must contain all integers at most once from 0 until " + degree)
    }

    properIndices.foreach{ i =>
      require(range contains i,
        "The constructor arguments of partial permutation must not contain any integer out of range 0 until " + degree)
    }
  }

  def apply(degree: Int, properIndices: Seq[Int]): PartialPermutation = {
    validatePartialPermutationArguments(degree, properIndices)
    new SeqPartialPermutation(degree, properIndices)
  }

  trait ConstructorVarargManager[E]{
    def apply(args: Seq[E]): Seq[Int]
  }

  def apply(degree: Int): PartialPermutationFactory = new PartialPermutationFactory(degree)

  class PartialPermutationFactory(val degree: Int){

    def apply[E: ConstructorVarargManager](properIndices: E*): PartialPermutation = {
      val manager = implicitly[ConstructorVarargManager[E]]
      PartialPermutation(degree, manager(properIndices))
    }

    def apply(properIndices: LinearSeq[Int]): PartialPermutation = PartialPermutation(degree, properIndices)
    def apply(properIndices: IndexedSeq[Int]): PartialPermutation = PartialPermutation(degree, properIndices)
  }

  //***** all (partial) permutations generators *****
  private[PartialPermutation]
  trait PermutationBuilder[E, B <: PermutationBuilder[E, B]]
      extends CombinatorialBuilder[E, B]{

    def properIndices: Seq[E]
  }

  def allPermutations[E](arg: Seq[E], rank: Int): Seq[Seq[E]] = {
    require(arg.nonEmpty)

    case class Builder[F](properIndices: Vector[F], available: Vector[F])
      extends PermutationBuilder[F, Builder[F]]{

      override def nextGeneration: Seq[Builder[F]] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder[F]], i: Int): Seq[Builder[F]] =
          i match {
            case -1 => accum
            case _  =>
              val newProperIndices = properIndices :+ available(i)
              val newAvailable = removeAt(available, i)
              val newBuilder = Builder(newProperIndices, newAvailable)
              nextGeneration(newBuilder +: accum, i-1)
          }

        nextGeneration(Nil, available.length-1)
      }
    }

    val init = Builder(Vector(), arg.toVector)
    generateCombinatorial(init, rank).map(_.properIndices)
  }

  def allPermutations(s: String, rank: Int): Seq[String] =
    allPermutations(s: Seq[Char], rank).map(_.mkString)

  def allPermutations(degree: Int, rank: Int): Seq[PartialPermutation] =
    allPermutations(0 until degree, rank).map(new SeqPartialPermutation(degree, _))
//  {
//    require(degree > 0)
//
//    case class Builder(properIndices: Vector[Int], available: Vector[Int])
//      extends PermutationBuilder[Int, Builder]{
//
//      override def nextGeneration: Seq[Builder] = {
//        @tailrec
//        def nextGeneration(accum: Seq[Builder], i: Int): Seq[Builder] =
//          i match {
//            case -1 => accum
//            case _  =>
//              val newProperIndices = properIndices :+ available(i)
//              val newAvailable = removeAt(available, i)
//              val newBuilder = Builder(newProperIndices, newAvailable)
//              nextGeneration(newBuilder +: accum, i-1)
//          }
//
//        nextGeneration(Nil, available.length-1)
//      }
//
//      def toPermutation = new SeqPartialPermutation(degree, properIndices)
//    }
//
//    val init = Builder(Vector(), (0 until degree).toVector)
//    generateCombinatorial(init, rank).map(_.toPermutation)
//  }
}