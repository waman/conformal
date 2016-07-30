package org.waman.conformal.integral.combinatorial

import spire.implicits._
import spire.math.Rational

import scala.annotation.tailrec

object Multiset extends CombinatorialGenerator{

  private[combinatorial]
  def calculateMultiplicity[E](seq: Seq[E]): Map[E, Int] =
    seq.groupBy(e => e).mapValues(_.length)

  def permutationCount(seq: Seq[Any]): Int = {
    val numFactor = 1 to seq.length
    val denFactor = calculateMultiplicity(seq).values.flatMap(1 to _)

    numFactor.zip(denFactor)
      .map{ case (n: Int, d: Int) => Rational(n, d) }
      .reduce{(x, y) => x * y}
      .toInt
  }

  override def allPermutations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] =
    allPartialPermutations(calculateMultiplicity(seq), rank)

  def allPermutations[E](map: Map[E, Int]): Seq[Seq[E]] =
    allPartialPermutations(map, map.values.sum)

  def allPartialPermutations[E](map: Map[E, Int], rank: Int): Seq[Seq[E]] = {

    case class Builder(suffices: Vector[E], available: Map[E, Int])
      extends CombinatorialBuilder[E, Builder]{

      override def nextGeneration: Seq[Builder] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder], ite: Iterable[E]): Seq[Builder] =
          ite.isEmpty match {
            case true  => accum
            case false =>
              val key = ite.head
              val newSuffices = suffices :+ key
              val newAvailable = available(key) match {
                case 1 => available - key
                case i => available.updated(key, i-1)
              }
              val newBuilder = Builder(newSuffices, newAvailable)
              nextGeneration(newBuilder +: accum, ite.tail)
          }

        nextGeneration(Nil, available.keys)
      }
    }

    val init = Builder(Vector(), map)
    generateCombinatorial(init, rank).map(_.suffices)
  }
}
