package org.waman.conformal.integral.combinatorial

import org.waman.conformal._
import org.waman.conformal.integral.BinomialCoefficient
import spire.implicits._
import spire.math.{Integral, Rational}

import scala.annotation.tailrec
import scala.collection.SortedSet

trait Combination
    extends PartialIntCombinatorial[Boolean]
    with Ordered[Combination]{

  def elements: Set[Int]
  def sortedElements: Seq[Int]

  def contains(i: Int): Boolean = elements.contains(i)
  override def apply(i: Int): Boolean = contains(i)

  def apply[E](seq: Seq[E]): Seq[E] = sortedElements.map(seq)
  def apply[E](set: SortedSet[E]): Set[E] = apply(set.toSeq).toSet

  //***** Type converters *****
  def toMap: Map[Int, Boolean] = indices.map{i => (i, apply(i))}.toMap

  //***** Order related *****
  override def compare(that: Combination): Int = {
    PartialIntCombinatorial.validateComparablity(this, that)

    sortedElements.zip(that.sortedElements).find(p => p._1 != p._2) match {
      case None => 0
      case Some((x, y)) =>
        if (x < y) -1
        else 1
    }
  }

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: Combination =>
      that.canEqual(this) &&
        degree == that.degree &&
        equalElements(that)  // rank must equal
  }

  protected def equalElements(that: Combination): Boolean

  def canEqual(other: Any): Boolean = other.isInstanceOf[Combination]

  override def hashCode: Int = (elements + degree + rank).hashCode

  override def toString: String = indices.map{
    case i if contains(i) => i.toString
    case i => s"[$i]"
  }.mkString("(", ", ", ")")
}

object Combination{

  def combinationCount[I: Integral](n: I, r: I): I = BinomialCoefficient(n, r)

  private class SetCombination(val degree: Int, override val elements: Set[Int])
      extends Combination{

    override def rank: Int = elements.size

    override def sortedElements: Seq[Int] = elements.toSeq.sorted

    override protected def equalElements(that: Combination): Boolean =
      elements == that.elements
  }

  private class SeqCombination(val degree: Int, val sortedElements: Seq[Int])
      extends Combination{

    override def rank: Int = sortedElements.length
    override def contains(i: Int): Boolean = sortedElements.contains(i)
    override def elements: Set[Int] = sortedElements.toSet

    override protected def equalElements(that: Combination): Boolean =
      sortedElements == that.sortedElements
  }

  //***** apply() factory methods *****
  private def validateElements(degree: Int, elements: Traversable[Int]): Unit =
    PartialIntCombinatorial.validateArgument(degree, elements, "elements")

  def apply(degree: Int, elements: Seq[Int]): Combination = {
    validateElements(degree, elements)
    new SeqCombination(degree, elements.sorted)
  }

  def apply(degree: Int, elements: Set[Int]): Combination = {
    validateElements(degree, elements)
    new SetCombination(degree, elements)
  }

  class CombinationFactory(degree: Int){
    def apply(elements: Int*): Combination = Combination.this.apply(degree, elements.toVector)
  }

  def apply(degree: Int): CombinationFactory = new CombinationFactory(degree)

  //***** all combinations generators *****
  def allCombinations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] =
    generateCombinations(seq.length, rank).map(_.map(seq))

  def allCombinations(s: String, rank: Int): Seq[String] =
    allCombinations(s: Seq[Char], rank).map(_.mkString)

  def allCombinations[E](arg: Set[E], rank: Int): Seq[Set[E]] =
    allCombinations(arg.toSeq, rank).map(_.toSet)

  def allCombinations(degree: Int, rank: Int): Seq[Combination] =
    generateCombinations(degree, rank).map(new SeqCombination(degree, _))

  private[combinatorial]
  def generateCombinations(degree: Int, rank: Int): Seq[Seq[Int]] = {

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
    }

    val init = Builder(Vector(), (0 until degree).toVector)
    generateCombinatorial(init, rank).map(_.elements)
  }
}