package org.waman.conformal.number.integral

import org.waman.conformal.number.integral.combinatorics.Permutation.ConstructorVarargManager

import scala.annotation.tailrec
import scala.language.implicitConversions

package object combinatorics {

  //***** Combinatorial Builder *****
  private[combinatorics]
  def generateCombinatorials[B <: CombinatorialBuilder[_, B]](start: B, n: Int): Stream[B] = {

    @tailrec
    def generateAll(stream: Stream[B], n: Int): Stream[B] =
      n match {
        case 0 => stream
        case _ =>
          val newStream = stream.flatMap(_.nextGeneration)
          generateAll(newStream, n-1)
      }

    generateAll(Stream(start), n)
  }

  //***** Permutation ******
  implicit object IntConstructorVarargManager
      extends ConstructorVarargManager[Int]{

    override def apply(args: Seq[Int]): Seq[Int] = args.toVector
  }

  implicit object IntTupleConstructorVarargManager
      extends ConstructorVarargManager[(Int, Int)]{

    override def apply(args: Seq[(Int, Int)]): Seq[Int] = {
      val mapping = args.toMap
      (0 until mapping.size).map(mapping)
    }
  }
}
