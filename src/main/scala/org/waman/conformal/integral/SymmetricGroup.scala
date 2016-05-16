package org.waman.conformal.integral

import spire.algebra.Group
import spire.implicits._

import scala.annotation.tailrec

class SymmetricGroup(val degree: Int) extends Group[Permutation]{
  import scala.language.postfixOps

  require(degree > 0, "Degree of permutation must be positive: " + degree)

  override def op(x: Permutation, y: Permutation): Permutation = x * y
  override val id: Permutation = Permutation.identity(degree)
  override def inverse(p: Permutation): Permutation = p.inverse

  def order: BigInt = degree!
  def elements: Seq[Permutation] = permutations

  def permutations: Seq[Permutation] = degree match {
    case 1 => Seq(id)
    case 2 => Seq(id, signed(List(1, 0), -1))
    case 3 => Seq(id, signed(List(0, 2, 1), -1),
                  signed(List(1, 0, 2), -1), signed(List(1, 2, 0),  1),
                  signed(List(2, 0, 1),  1), signed(List(2, 1, 0), -1))
    case _ =>
      @tailrec
      def generateSignedPermutations(seq: Seq[SignedPermutation], n: Int): Seq[SignedPermutation] =
        n match {
          case 1 => seq
          case _ =>
            val newSeq = seq.flatMap(_.generateHigherPermutations)
            generateSignedPermutations(newSeq, n-1)
        }

      val p1 = new SignedPermutation(List(0), 1)
      generateSignedPermutations(Stream(p1), degree)
  }

  private[SymmetricGroup] class SignedPermutation(protected val to: List[Int], val sgn: Int)
      extends AbstractListPermutation{

    def generateHigherPermutations: Seq[SignedPermutation] =
      (degree to 0 by -1).map{ i =>
        val (first, second) = to.splitAt(i)
        val newTo = first ::: degree :: second
        val newSign = if((degree-i) % 2 == 0) sgn else -sgn
        new SignedPermutation(newTo, newSign)
      }
  }

  private[SymmetricGroup] def signed(to: List[Int], sign: Int): Permutation
    = new SignedPermutation(to, sign)
}

object SymmetricGroup{
  def apply(degree: Int): SymmetricGroup = new SymmetricGroup(degree)
}