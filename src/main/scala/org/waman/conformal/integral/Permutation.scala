package org.waman.conformal.integral

import spire.algebra.Group

trait Permutation extends PartialFunction[Int, Int]{

  val degree: Int
  protected lazy val indices: List[Int] = (0 until degree).toList

  override def isDefinedAt(i: Int): Boolean = indices.contains(i)

  override def apply(i: Int): Int
  def unapply(i: Int): Int = indices.find(apply(_) == i).get

  def apply[E](objs: List[E]): List[E] = {
    require(objs.length == degree)
    indices.map(unapply(_)).map(objs(_))
  }

  def *(p: Permutation): Permutation = {
    require(degree == p.degree)
    val th = this
    new Permutation {
      override val degree: Int = th.degree
      override def apply(i: Int): Int = th(p(i))
      override def sgn: Int = th.sgn * p.sgn
    }
  }

  def andThen(p: Permutation): Permutation = p * this

  def inverse: Permutation = {
    val th = this
    new Permutation {
      override val degree: Int = th.degree
      override def apply(i: Int): Int = th.unapply(i)
      override def unapply(i: Int): Int = th.apply(i)
      override def inverse: Permutation = th
      override def sgn: Int = th.sgn
    }
  }

  def sgn: Int

  override def equals(other: Any): Boolean = other match {
    case that: Permutation =>
      that.canEqual(this) &&
      degree == that.degree &&
      indices.forall(i => apply(i) == that.apply(i))
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Permutation]

  override def hashCode: Int = (degree::indices.map(apply(_))).hashCode

  override def toString: String = indices.map(apply(_)).mkString("(", " ", ")")
}

object Permutation{

  def identity(deg: Int): Permutation = new Permutation {
    override val degree: Int = deg
    override def apply(i: Int): Int = i
    override def unapply(i: Int): Int = i
    override def *(p: Permutation): Permutation = p
    override def inverse: Permutation = this
    override def sgn: Int = 1
    override def toString: String = indices.mkString("(", " ", ")")
  }

  def apply(to: Int*): Permutation = new ListPermutation(to.toList)
}

class ListPermutation(private val to: List[Int]) extends Permutation{

  override val degree: Int = to.length

  override def apply(i: Int): Int = to(i)
  override def unapply(i: Int): Int = to.indexOf(i)

  override lazy val sgn: Int = {  // (021453)
  def calculateSign(list: List[Int], n: Int): Int = list match {
      case _ :: Nil => 1
      case _ if list.head == n => calculateSign(list.tail, n+1)
      case _ =>  // args: list = List(2, 1, 4, 5, 3), n = 1
        val i = list.indexOf(n)  // i = 1

        val newList = exchangeHeadAndGetTail(list, i)
        // newList = List(1, 2, 4, 5, 3) ( = List(1) ::: 2 :: List(4, 5, 3))
        -calculateSign(newList, n+1)
    }

    def exchangeHeadAndGetTail(list: List[Int], i: Int): List[Int] =
      list.slice(1, i) ::: list.head :: list.slice(i+1, list.length)

    calculateSign(to, 0)
  }

  override def toString: String = to.mkString("(", " ", ")")
}


class SymmetricGroup(val degree: Int) extends Group[Permutation]{
  override def op(x: Permutation, y: Permutation): Permutation = x * y
  override val id: Permutation = Permutation.identity(degree)
  override def inverse(p: Permutation): Permutation = p.inverse
}

object SymmetricGroup{
  def apply(degree: Int): SymmetricGroup = new SymmetricGroup(degree)
}