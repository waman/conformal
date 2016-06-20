package org.waman.conformal.integral

private[integral] trait CombinatorialBuilder[E, B <: CombinatorialBuilder[E, B]]{
  def nextGeneration: Seq[B]
}
