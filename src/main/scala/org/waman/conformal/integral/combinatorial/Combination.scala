package org.waman.conformal.integral.combinatorial

import org.waman.conformal._
import spire.implicits._
import spire.math.{Integral, Rational}

import scala.annotation.tailrec

trait Combination extends PartialIntCombinatorial[Boolean]{

  def elements: Set[Int]
  def sortedElements: Seq[Int]

  def contains(i: Int): Boolean = elements.contains(i)
  def apply(i: Int): Boolean = contains(i)

  def apply[E](seq: Seq[E]): Seq[E] = sortedElements.map(seq)
  def apply[E](set: Set[E]): Set[E] = apply(set.toSeq).toSet

  //***** Type converters *****
  def toMap: Map[Int, Boolean] = indices.map{i => (i, apply(i))}.toMap

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

  def combinationCount[I: Integral](n: I, r: I): I = {
    require(n >= 0, s"n must be non-negative: $n")
    require(0 <= r && r <= n, s"r must be in 0 <= r <= $n")

    val s = if(2*r > n) n-r else r

    @tailrec
    def combinationCount[J: Integral](accum: Rational, n: J, r: J): Rational =
      r match {
        case 0 => accum
        case _ =>
          // This matching is due to bugs of spire 0.10.1, 0.11.0
          val f = r match {
            case i: Int    => Rational(n.toInt, i)
            case i: Long   => Rational(n.toLong, i)
            case i: BigInt => Rational(n.toBigInt, i)
            case _         => n.toRational / r.toRational
          }

          combinationCount(accum * f, n-1, r-1)
      }

    s match {
      case 0 => 1
      case 1 => n
      case _ =>
        val result = combinationCount(1, n, s)
        implicitly[Integral[I]].fromRational(result)
    }
  }

  // For implementation interest
  private[combinatorial]
  def combinationCount1(n: Int, r: Int): Int =
    if(r == 0 || r == n) 1
    else combinationCount1(n-1, r-1) + combinationCount1(n-1, r)

  // For implementation interest
  private[combinatorial]
  def combinationCount2(n: Int, r: Int): Int = {
    val s = if(2*r > n) n-r else r

    def add(v0: Vector[Int], v1: Vector[Int]): Vector[Int] =
      v0.zip(v1).map(p => p._1 + p._2)

    @tailrec
    def combinationCount(accum: Vector[Int], i: Int): Vector[Int] = i match {
      case 0 =>
        accum
      case _ if i <= s =>
        combinationCount(add(accum.init, accum.tail), i-1)
      case _ if i < n-s =>
        combinationCount(add(0 +: accum.init, accum), i-1)
      case _ =>
        combinationCount(add(0 +: accum, accum :+ 0), i-1)
    }

    combinationCount(Vector(1), n).head
  }

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
    allCombinations(s.length, rank).map(_(s)).map(_.toString)

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