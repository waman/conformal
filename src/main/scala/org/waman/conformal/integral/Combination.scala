package org.waman.conformal.integral

import org.waman.conformal._
import spire.implicits._
import spire.math.Integral

import scala.annotation.tailrec

/**
  * Example
  *   degree = 4
  *   rank = 3
  *
  * / 0 1 (2) 3 \  <- indices (including 2 in this case)
  * \ 1 2 (-) 0 /
  *
  *  = / 3 0 1 (2) \  <- elements (not containing 2 in this case)
  *    \ 0 1 2 (-) /
  *
  * apply(0) == 1, apply(1) == 2,
  * apply(2) not defined (maybe return -1 or throws an exception)
  * apply(100) not defined (maybe throws an exception)
  *
  * applyOption(0) == Some(1), applyOption(2) == None
  *
  * indexOf(1) == 0, indexOf(2) == 1, indexOf(100) not defined (maybe throws an exception)
  *
  * apply(Seq(a, b, c, d)) -> Seq(c, a, b)
  */
trait Combination extends Combinatorial[Boolean]{

  def elements: Set[Int]

  def contains(i: Int): Boolean = elements.contains(i)
  def apply(i: Int): Boolean = contains(i)

  def apply[E](set: Set[E]): Set[E] = apply(set.toSeq).toSet

  override def apply[E](seq: Seq[E]): Seq[E] = elements.map(seq(_)).toSeq

  //***** Type converters *****
  def toMap: Map[Int, Boolean] = indices.map{i => (i, apply(i))}.toMap

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: Combination =>
      that.canEqual(this) &&
        degree == that.degree &&
        elements == that.elements  // rank must equal
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Combination]

  override def hashCode: Int = (elements + degree + rank).hashCode

  override def toString: String = indices.map{
    case i: Int if contains(i) => i.toString
    case i => s"[$i]"
  }.mkString("(", ", ", ")")
}

object Combination{

  def combinationCount[I: Integral](n: I, r: I): I = {
    @tailrec
    def combinationCount(prod: I, n: I, r: I): I = r match {
      case 0 => prod
      case _ => combinationCount(prod * n, n-1, r-1)
    }

    combinationCount(1, n, r)
  }

  private class SetCombination(val degree: Int, override val elements: Set[Int])
      extends Combination{

    override def rank: Int = elements.size
  }

  private class SeqCombination(val degree: Int, es: Seq[Int])
      extends Combination{

    override def rank: Int = es.length
    override def contains(i: Int): Boolean = es.contains(i)
    override def elements: Set[Int] = es.toSet
  }

  //***** apply() factory methods *****
  private def validateElements(degree: Int, elements: Set[Int]): Unit = {
    require(degree > 0, "The degree of combination must be positive: " + degree)

    val range = 0 until degree

    elements.foreach{ i =>
      require(range.contains(i),
        "The elements of combination must not contain any integer out of range 0 until " + degree)
    }
  }

  def apply(degree: Int, elements: Set[Int]): Combination = {
    validateElements(degree, elements)
    new SetCombination(degree, elements)
  }

  def apply(degree: Int)(elements: Int*): Combination = apply(degree, elements.toSet)

  //***** all combinations generators *****
  def allCombinations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] =
    allCombinations(seq.length, rank).map(_(seq))

  def allCombinations(s: String, rank: Int): Seq[String] =
    allCombinations(s.length, rank).map(_(s))

  def allCombinations[E](arg: Set[E], rank: Int): Seq[Set[E]] =
    allCombinations(arg.toSeq, rank).map(_.toSet)

  def allCombinations(degree: Int, rank: Int): Seq[Combination] = {

    case class Builder(elements: Vector[Int], available: Vector[Int])
        extends CombinatorialBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder], i: Int): Seq[Builder] =
          i match {
            case -1 => accum
            case _  =>
              val entry = available(i)
              if(elements.isEmpty || elements.last < entry){
                val newElements = elements :+ entry
                val newAvailable = removeAt(available, i)
                val newBuilder = Builder(newElements, newAvailable)
                nextGeneration(newBuilder +: accum, i-1)
              }else{
                nextGeneration(accum, i-1)
              }
          }

        nextGeneration(Nil, available.length-1)
      }

      def toCombination = new SeqCombination(degree, elements)
    }

    val init = Builder(Vector(), (0 until degree).toVector)
    generateCombinatorial(init, rank).map(_.toCombination)
  }
}