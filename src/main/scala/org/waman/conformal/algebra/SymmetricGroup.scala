package org.waman.conformal.algebra

import org.waman.conformal.integral.combinatorial.Permutation
import spire.algebra.Group
import spire.implicits._
import scala.language.postfixOps

class SymmetricGroup(val degree: Int) extends Group[Permutation]{

  require(degree > 0, "Degree of permutation must be positive: " + degree)

  override def op(x: Permutation, y: Permutation): Permutation = x * y
  override val id: Permutation = Permutation.identity(degree)
  override def inverse(p: Permutation): Permutation = p.inverse

  def order: BigInt = degree!
  def elements: Seq[Permutation] = Permutation.allPermutations(degree)
}

object SymmetricGroup{
  def apply(degree: Int): SymmetricGroup = new SymmetricGroup(degree)
}