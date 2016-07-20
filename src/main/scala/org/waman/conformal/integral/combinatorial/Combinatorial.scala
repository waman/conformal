package org.waman.conformal.integral.combinatorial

trait Combinatorial[I, T]{

  def degree: Int

  def indices: Seq[I]
  def indexIsDefinedAt(i: I): Boolean = indices.contains(i)

  def apply(i: I): T
}

trait IntCombinatorial[T] extends Combinatorial[Int, T]{

  override def indices: Seq[Int] = 0 until degree
}

trait PartialIntCombinatorial[T] extends IntCombinatorial[T]{
  def rank: Int
  def apply[E](seq: Seq[E]): Seq[E]
  def apply(s: String): String = apply(s: Seq[Char]).mkString
}
