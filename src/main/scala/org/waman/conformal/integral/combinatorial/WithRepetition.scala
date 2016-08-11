package org.waman.conformal.integral.combinatorial

import org.waman.conformal.integral.GeneralizedBinomialCoefficient
import spire.implicits._
import spire.math.Integral

object WithRepetition extends CombinatorialGenerator{

  //***** Permutation *****
  def permutationCount[I: Integral](degree: I): I = permutationCount(degree, degree)

  //***** Partial Permutation *****
  def permutationCount[I: Integral](degree: I, rank: I): I = degree ** rank.toInt

  override def allPermutations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] = {
    val entries = seq.toVector

    case class Builder(suffices: Vector[E])
      extends CombinatorialBuilder[E, Builder]{

      override def nextGeneration: Seq[Builder] =
        seq.map( e => Builder(suffices :+ e))
    }

    generateCombinatorial(Builder(Vector()), rank).map(_.suffices)
  }

  //***** Combination *****
  def combinationCount[I: Integral](degree: I, rank: I): I =
    GeneralizedBinomialCoefficient(degree+rank-1, rank)

  override def allCombinations(degree: Int, rank: Int): Seq[Seq[Int]] = {
    val entries = 0 until degree

    case class Builder(elements: Vector[Int])
      extends CombinatorialBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] =
        entries.collect{ case e if elements.isEmpty || elements.last <= e =>
          Builder(elements :+ e)
        }
    }

    generateCombinatorial(Builder(Vector()), rank).map(_.elements)
  }
}
