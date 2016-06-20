package org.waman.conformal

import spire.math.Integral
import scala.annotation.tailrec
import spire.implicits._

import scala.language.implicitConversions

package object integral {

  implicit def convertIntToFactorialRepresentation(i: Int): FactorialRepresentation =
    FactorialRepresentation.fromInt(i)

  def factorial[I: Integral](i: I): I = {
    @tailrec
    def factorial(prod: I, n: I): I = n match {
      case 0 => prod
      case _ => factorial(prod * n, n-1)
    }

    factorial(1, i)
  }

  //***** Combinatorial Builder *****
  private[integral] def generateCombinatorial[B <: CombinatorialBuilder[_, B]](init: B, n: Int): Stream[B] = {

    @tailrec
    def generateAll(stream: Stream[B], n: Int): Stream[B] =
      n match {
        case 0 => stream
        case _ =>
          val newStream = stream.flatMap(_.nextGeneration)
          generateAll(newStream, n-1)
      }

    generateAll(Stream(init), n)
  }

  //***** Permutation ******
  implicit object IntConstructorVarargManager
      extends Permutation.ConstructorVarargManager[Int]{

    override def apply(args: Seq[Int]): Seq[Int] = args
  }

  implicit object IntTupleConstructorVarargManager
      extends Permutation.ConstructorVarargManager[(Int, Int)]{

    override def apply(args: Seq[(Int, Int)]): Seq[Int] = {
      val mapping = args.toMap
      (0 until mapping.size).map(mapping(_))
    }
  }

  //***** PartialPermutation *****
  implicit object IntPartialPermutationConstructorManger
    extends PartialPermutation.ConstructorVarargManager[Int]{

    override def apply(args: Seq[Int]): Seq[Int] = args
  }

  implicit object IntTuplePartialPermutationConstructorManger
    extends PartialPermutation.ConstructorVarargManager[(Int, Int)]{

    override def apply(args: Seq[(Int, Int)]): Seq[Int] = {
      val invertedMap = args.map(e => (e._2, e._1)).toMap
      (0 until invertedMap.size).map(invertedMap(_))
    }
  }
}
