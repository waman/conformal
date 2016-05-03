package org.waman.conformal.integral

import spire.algebra.Group

import scala.annotation.tailrec


class SymmetricGroup(val degree: Int) extends Group[Permutation]{
  import ConformalIntegralOps._
  import scala.language.postfixOps

  require(degree > 0, "Degree of permutation must be positive: " + degree)

  override def op(x: Permutation, y: Permutation): Permutation = x * y
  override val id: Permutation = Permutation.identity(degree)
  override def inverse(p: Permutation): Permutation = p.inverse

  def order: Int = degree!
  def elements: Seq[Permutation] = permutations

  def permutations: Seq[Permutation] = degree match {
    case 1 => Seq(id)
    case 2 => Seq(id, signed(List(1, 0), -1))
    case 3 => Seq(id, signed(List(0, 2, 1), -1),
                  signed(List(1, 0, 2), -1), signed(List(1, 2, 0),  1),
                  signed(List(2, 0, 1),  1), signed(List(2, 1, 0), -1))
    case _ =>
      @tailrec
      def generateIntLists(seq: Seq[(List[Int], Int)], n: Int): Seq[(List[Int], Int)] = n match {
        case 1 => seq
        case _ =>
          val e = degree-n+1
          val newSeq = seq.flatMap{ case (list, sign) =>
            (e to 0 by -1).map{ i =>
              val newSign = if((e-i) % 2 == 0) sign else -sign
              val (first, second) = list.splitAt(i)
              (first ::: e :: second, newSign)
            }
          }
          generateIntLists(newSeq, n-1)
      }

      generateIntLists(Stream((List(0), 1)), degree).map(e => signed(e._1, e._2))
  }

  private[SymmetricGroup] def signed(to: List[Int], sign: Int): Permutation
    = new SignSpecifiedPermutation(to, sign)

  private[SymmetricGroup] class SignSpecifiedPermutation(protected val to: List[Int], val sgn: Int)
    extends AbstractListPermutation
}

object SymmetricGroup{
  def apply(degree: Int): SymmetricGroup = new SymmetricGroup(degree)
}