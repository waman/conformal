package org.waman.conformal.number.integer.combinatorics

private[integer]
trait CombinatorialBuilder[E, B <: CombinatorialBuilder[E, B]]{
  def nextGeneration: Seq[B]
}
