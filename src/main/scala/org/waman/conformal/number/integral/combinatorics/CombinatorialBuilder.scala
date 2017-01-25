package org.waman.conformal.number.integral.combinatorics

private[integral]
trait CombinatorialBuilder[E, B <: CombinatorialBuilder[E, B]]{
  def nextGeneration: Seq[B]
}
