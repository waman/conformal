package org.waman.conformal.integral.combinatorial

trait Combinatorial[I, T]{

  def degree: Int
  def rank: Int

  def indices: Seq[I]
  def indexIsDefinedAt(i: I): Boolean = indices.contains(i)

  def apply(i: I): T

  def apply(seq: Seq[I]): Seq[I]
}

trait IntCombinatorial[T] extends Combinatorial[Int, T]{

  override def indices: Seq[Int] = 0 until degree
}
