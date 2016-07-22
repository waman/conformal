package org.waman.conformal.integral.combinatorial

private[integral]
trait CombinatorialBuilder[E, B <: CombinatorialBuilder[E, B]]{
  def nextGeneration: Seq[B]
}
