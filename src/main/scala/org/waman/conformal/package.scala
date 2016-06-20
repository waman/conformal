package org.waman

import scala.annotation.unchecked.uncheckedVariance
import scala.collection.generic.CanBuildFrom
import scala.language.higherKinds

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

  def swap[E](vec: Vector[E], i: Int, j: Int): Vector[E] = {
    if(i == j) vec
    else{
      val e = vec(i)
      vec.updated(i, vec(j)).updated(j, e)
    }
  }

  def insertAt[E](seq: Seq[E], i: Int, e: E): Seq[E] = i match {
    case 0 => e +: seq
    case _ if i == seq.length => seq :+ e
    case _ =>
      val (first, second) = seq.splitAt(i)
      first ++: e +: second
  }

  def insertAt[E](vec: Vector[E], i: Int, e: E): Vector[E] = insertAt(vec: Seq[E], i, e).toVector

  def removeAt[E](seq: Seq[E], i: Int): Seq[E] = i match {
    case 0 => seq.tail
    case _ =>
      val (first, second) = seq.splitAt(i)
      first ++: second.tail
  }

  def removeAt[E](seq: Vector[E], i: Int): Vector[E] = removeAt(seq: Seq[E], i).toVector

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
