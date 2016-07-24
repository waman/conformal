package org.waman.conformal.integral.combinatorial

import spire.math.Integral
import spire.implicits._
import scala.annotation.tailrec

object WithRepetition {

  //***** Permutation *****
  def permutationCount[I: Integral](degree: I, rank: Int): I = degree ** rank

  def allPermutations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] = {
    val entries = seq.toVector

    case class Builder(suffices: Vector[E])
      extends CombinatorialBuilder[E, Builder]{

      override def nextGeneration: Seq[Builder] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder], i: Int): Seq[Builder] =
          i match {
            case -1 => accum
            case _  =>
              val newSuffices = suffices :+ entries(i)
              val newBuilder = Builder(newSuffices)
              nextGeneration(newBuilder +: accum, i-1)
          }

        nextGeneration(Nil, entries.length-1)
      }
    }

    generateCombinatorial(Builder(Vector()), rank).map(_.suffices)
  }

  def allPermutations(degree: Int, rank: Int): Seq[Seq[Int]] =
    allPermutations(0 until degree, rank)

  //***** Combination *****
  def allCombinations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] =
    allCombinations(seq.length, rank).map(s => s.map(seq))

  def allCombinations(degree: Int, rank: Int): Seq[Seq[Int]] = {
    val entries = (0 until degree).toVector

    case class Builder(elements: Vector[Int])
      extends CombinatorialBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder], i: Int): Seq[Builder] =
          i match {
            case -1 => accum
            case _  =>
              val entry = entries(i)
              if(elements.isEmpty || elements.last <= entry){
                val newElements = elements :+ entry
                val newBuilder = Builder(newElements)
                nextGeneration(newBuilder +: accum, i-1)
              }else{
                nextGeneration(accum, i-1)
              }
          }

        nextGeneration(Nil, degree-1)
      }
    }

    generateCombinatorial(Builder(Vector()), rank).map(_.elements)
  }
}
