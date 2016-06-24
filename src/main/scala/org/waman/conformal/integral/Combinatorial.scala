package org.waman.conformal.integral

trait Combinatorial[T]{

  def degree: Int
  def rank: Int

  def indices: Seq[Int] = 0 until degree
  def indexIsDefinedAt(i: Int): Boolean = indices.contains(i)

  def apply(i: Int): T

  def apply[E](seq: Seq[E]): Seq[E]
  def apply(s: String): String = apply(s: Seq[Char]).mkString
}
