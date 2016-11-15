package org.waman

import scala.language.higherKinds
import scala.collection.mutable

package object conformal {

  def memoize[K, V](f: K => V): collection.Map[K, V] = new mutable.HashMap[K, V](){
    override def apply(key: K): V = getOrElseUpdate(key, f(key))
  }

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

  def indexOfMax[T: Ordering](seq: Seq[T]): Int =
    seq.zipWithIndex.maxBy(_._1)._2

  def indexOfMin[T: Ordering](seq: Seq[T]): Int =
    seq.zipWithIndex.minBy(_._1)._2

  def groupSequentialDuplicates[A](seq: Seq[A]): Stream[(A, Int)] = seq.isEmpty match {
    case true  => Stream()
    case false =>
      val head = seq.head
      val dup = seq.takeWhile(_ == head)
      val n = dup.length
      (head, n) #:: groupSequentialDuplicates(seq.drop(n))
  }
}
