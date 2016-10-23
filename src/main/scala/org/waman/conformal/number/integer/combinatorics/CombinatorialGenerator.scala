package org.waman.conformal.number.integer.combinatorics

import spire.math.Integral

trait CombinatorialGenerator{

  //***** Permutation *****
  def allPermutations[E](seq: Seq[E]): Seq[Seq[E]] = allPermutations(seq, seq.length)

  def allPermutations(degree: Int): Seq[Seq[Int]] = allPermutations(degree, degree)

  def allPermutations(s: String): Seq[String] = allPermutations(s: Seq[Char]).map(_.mkString)

  //***** Partial Permutation *****
  // Subtypes implementing this trait must override
  // either allPermutations(Seq[E], Int) or allPermutations(Int, Int)
  def allPermutations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] =
    allPermutations(seq.length, rank).map(s => s.map(seq))

  def allPermutations(degree: Int, rank: Int): Seq[Seq[Int]] =
    allPermutations(0 until degree, rank)

  def allPermutations(s: String, length: Int): Seq[String] =
    allPermutations(s: Seq[Char], length).map(_.mkString)

  //***** Combination *****
  // Subtypes implementing this trait must override
  // either allCombinations(Seq[E], Int) or allCombinations(Int, Int)
  def allCombinations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] =
  allCombinations(seq.length, rank).map(s => s.map(seq))

  def allCombinations(degree: Int, rank: Int): Seq[Seq[Int]] =
    allCombinations(0 until degree, rank)

  def allCombinations(s: String, length: Int): Seq[String] =
    allCombinations(s: Seq[Char], length).map(_.mkString)
}

object CombinatorialGenerator extends CombinatorialGenerator{

  def permutationCount[I: Integral](degree: I): I =
    Permutation.permutationCount(degree)

  def permutationCount[I: Integral](degree: I, rank: I): I =
    PartialPermutation.permutationCount(degree, rank)

  override def allPermutations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] =
    PartialPermutation.allPermutations(seq, rank)

  def combinationCount[I: Integral](degree: I, rank: I): I =
    Combination.combinationCount(degree, rank)

  override def allCombinations(degree: Int, rank: Int): Seq[Seq[Int]] =
    Combination.generateCombinations(degree, rank)
}