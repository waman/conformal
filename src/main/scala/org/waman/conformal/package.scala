package org.waman

package object conformal {

  def swap[E](seq: Seq[E], i: Int, j: Int): Seq[E] =
    if(i == j) seq
    else{
      seq.indices.map {
        case x if x == i => seq(j)
        case x if x == j => seq(i)
        case x => seq(x)
      }
    }

  def swap[E](list: List[E], i: Int, j: Int): List[E] = swap(list: Seq[E], i, j).toList

  def indexOfMax[T](seq: Seq[T])(implicit ord: Ordering[T]): Int =
    seq.zipWithIndex.reduceLeft[(T, Int)]{ case (x, y) =>
      if(ord.compare(x._1, y._1) > 0) x
      else y
    }._2

  def indexOfMin[T](seq: Seq[T])(implicit ord: Ordering[T]): Int =
    seq.zipWithIndex.reduceLeft[(T, Int)]{ case (x, y) =>
      if(ord.compare(x._1, y._1) < 0) x
      else y
    }._2
}
