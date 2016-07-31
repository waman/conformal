package org.waman.conformal.integral.combinatorial

import spire.implicits._
import spire.math.Rational

import scala.annotation.tailrec
import scala.collection.immutable.SortedMap

object Multiset extends CombinatorialGenerator{

  private[combinatorial]
  def calculateMultiplicity[E](seq: Seq[E]): SortedMap[E, Int] = {
    val mul = seq.groupBy(e => e).mapValues(_.length)
    SortedMap(mul.toSeq:_*)(new Ordering[E]{
      override def compare(x: E, y: E) = (seq.indexOf(x), seq.indexOf(y)) match {
        case (s, t) if s < t => -1
        case (s, t) if s > t =>  1
        case _               =>  0
      }
    })
  }

  //***** Permutation *****
  def permutationCount(seq: Seq[Any]): Int = {
    val numFactor = 1 to seq.length
    val denFactor = calculateMultiplicity(seq).values.flatMap(1 to _)

    numFactor.zip(denFactor)
      .map{ case (n: Int, d: Int) => Rational(n, d) }
      .reduce{(x, y) => x * y}
      .toInt
  }

  def permutationCount(s: String): Int = permutationCount(s: Seq[Char])

  def allPermutations[E](map: SortedMap[E, Int]): Seq[Seq[E]] =
    generatePermutations(map, map.values.sum)

  //***** Partial Permutation *****
  def permutationCount(seq: Seq[Any], rank: Int): Int =
    allPermutations(seq, rank).length

  def permutationCount(s: String, rank: Int): Int = permutationCount(s: Seq[Char], rank)

  override def allPermutations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] =
    generatePermutations(calculateMultiplicity(seq), rank)

  def generatePermutations[E](map: Map[E, Int], rank: Int): Seq[Seq[E]] = {

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
              nextGeneration(accum :+ newBuilder, ite.tail)
          }

        nextGeneration(Vector(), available.keys)
      }
    }

    val init = Builder(Vector(), map)
    generateCombinatorial(init, rank).map(_.suffices)
  }

  //***** Combination *****
  def combinationCount(seq: Seq[Any], rank: Int): Int =
    allCombinations(seq, rank).length

  def combinationCount(s: String, rank: Int): Int = combinationCount(s: Seq[Char], rank)

  override def allCombinations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] = {  // Seq("a", "a", "b")
    val multiplicity = calculateMultiplicity(seq)  // Map("a" -> 2, "b" -> 1)
    val keySeq = multiplicity.keys.toSeq  // Seq("a", "b")
    val intMap = keySeq.indices.map(i => (i, multiplicity(keySeq(i)))).toMap  // Map(0 -> 2, 1 -> 1)
    generateCombinations(intMap, rank)  // Seq(Seq(0, 0, 1), Seq(0, 1, 0), Seq(1, 0, 0))
      .map(_.map(keySeq))  // Seq(Seq("a", "a", "b"), Seq("a", "b", "a"), Seq("b", "a", "a"))
  }

  private def generateCombinations(map: Map[Int, Int], rank: Int): Seq[Seq[Int]] = {

    case class Builder(suffices: Vector[Int], available: Map[Int, Int])
      extends CombinatorialBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder], ite: Iterable[Int]): Seq[Builder] =
          ite.isEmpty match {
            case true  => accum
            case false =>
              val key = ite.head
              if(suffices.isEmpty || suffices.last <= key){
                val newSuffices = suffices :+ key
                val newAvailable = available(key) match {
                  case 1 => available - key
                  case i => available.updated(key, i-1)
                }
                val newBuilder = Builder(newSuffices, newAvailable)
                nextGeneration(accum :+ newBuilder, ite.tail)

              }else{
                nextGeneration(accum, ite.tail)
              }
          }

        nextGeneration(Vector(), available.keys)
      }
    }

    val init = Builder(Vector(), map)
    generateCombinatorial(init, rank).map(_.suffices)
  }
}
