package org.waman.conformal.number.integer.combinatorics

import org.waman.conformal.ForImplementationInterest
import org.waman.conformal.number.integer.BinomialCoefficient
import spire.implicits._
import spire.math.Integral

import scala.collection.SortedSet

trait Combination
    extends Combinatorial[Boolean]
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
    Combinatorial.validateComparablity(this, that)

    sortedElements.zip(that.sortedElements).find(p => p._1 != p._2) match {
      case None => 0
      case Some((x, y)) => if (x < y) -1 else 1
    }
  }

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: Combination =>
      that.canEqual(this) &&
        degree == that.degree &&
        equalElements(that)  // rank must equal
    case _ => false
  }

  protected def equalElements(that: Combination): Boolean

  def canEqual(other: Any): Boolean = other.isInstanceOf[Combination]

  override def hashCode: Int = (elements + degree + rank).##

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
  def apply(degree: Int, elements: Seq[Int]): Combination = {
    Combinatorial.validateArgument(degree, elements, "element")
    new SeqCombination(degree, elements.sorted)
  }

  def apply(degree: Int, elements: Set[Int]): Combination = {
    Combinatorial.validateArgument(degree, elements, "element")
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

  private[combinatorics]
  def generateCombinations(degree: Int, rank: Int): Seq[Seq[Int]] = {

    case class Builder(elements: Vector[Int], available: Seq[Int])
        extends CombinatorialBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] =
        available.filter(elements.isEmpty || elements.last < _).map(e =>
          Builder(
            elements :+ e,
            available.filter(_ != e)
          )
        )
    }

    val start = Builder(Vector(), 0 until degree)
    generateCombinatorials(start, rank).map(_.elements)
  }

  @ForImplementationInterest
  private[combinatorics]
  def allCombinations1[E](seq: Seq[E], rank: Int): Seq[Seq[E]] = {
    val indices = seq.indices

    generateCombinations1(seq.length, rank).map{ bits: Long =>
      indices.filter(i => (2L**i & bits) != 0).map(seq)
    }
  }

  @ForImplementationInterest
  private[combinatorics]
  def generateCombinations1(degree: Int, rank: Int): Seq[Long] = {
    def lowBit(n: Long): Long = (1L << n) - 1L
    def lowestBit(x: Long): Long = x & -x

    val start = lowBit(rank)          // start = 00001111
    val terminator = ~lowBit(degree)  // terminator = 11...100000000

    def generate(x: Long): Stream[Long] = x & terminator match {
      case 0L => // for x = 10011100
        val smallest = lowestBit(x)     // smallest = 00000100
        val ripple = x + smallest       // ripple = 10100000
        val newSmallest = lowestBit(ripple)  // newSmallest = 00100000
        val ones = ((newSmallest / smallest) >> 1) - 1L
          // newSmallest / smallest = 00001000, ones = 00000011
        val next = ripple | ones        // ripple | ones = 10100011
        x #:: generate(next)
      case _ => Stream.empty
    }
    generate(start)
  }
}