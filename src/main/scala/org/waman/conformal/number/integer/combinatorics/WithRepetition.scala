package org.waman.conformal.number.integer.combinatorics

import org.waman.conformal.number.integer.GeneralizedBinomialCoefficient
import spire.implicits._
import spire.math.Integral

object WithRepetition extends CombinatorialGenerator{

  //***** Permutation *****
  def permutationCount[I: Integral](degree: I): I = permutationCount(degree, degree)

  //***** Partial Permutation *****
  def permutationCount[I: Integral](degree: I, rank: I): I = degree ** rank.toInt

  override def allPermutations[E](arg: Seq[E], rank: Int): Seq[Seq[E]] = {

    case class Builder(seq: Vector[E])
      extends CombinatorialBuilder[E, Builder]{

      override def nextGeneration: Seq[Builder] =
        arg.map(e => Builder(seq :+ e))
    }

    generateCombinatorials(Builder(Vector()), rank).map(_.seq)
  }

  //***** Combination *****
  def combinationCount[I: Integral](degree: I, rank: I): I =
    GeneralizedBinomialCoefficient(degree+rank-1, rank)

  override def allCombinations(degree: Int, rank: Int): Seq[Seq[Int]] = {
    val entries = 0 until degree

    case class Builder(elements: Vector[Int])
      extends CombinatorialBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] =
        entries.filter(elements.isEmpty || elements.last <= _).map(e =>
          Builder(elements :+ e)
        )
    }

    generateCombinatorials(Builder(Vector()), rank).map(_.elements)
  }
}
