package org.waman

package object conformal {

  def swap[E](seq: Seq[E], i: Int, j: Int): Seq[E] = seq.indices.map{
    case x if x == i => seq(j)
    case x if x == j => seq(i)
    case x => seq(x)
  }

  def swap[E](list: List[E], i: Int, j: Int): List[E] = swap(list: Seq[E], i, j).toList
}
