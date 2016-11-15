package org.waman.conformal.number.integer.combinatorics

trait Cycle{

  def length: Int = elements.length
  def elements: Seq[Int]
  def min: Int = elements.min
  def elementsWithMinHead: Seq[Int] = {
    val (first, second) = elements.splitAt(elements.indexOf(min))
    second ++: first
  }

  def previousTo(i: Int): Int
  def nextTo(i: Int): Int

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: Cycle =>
      that.canEqual(this) &&
        length == that.length &&
        elementsWithMinHead == that.elementsWithMinHead
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Cycle]

  // a call of asInstanceOf method is for suppressing warning
  override lazy val hashCode: Int = (length, elementsWithMinHead).##

  override def toString: String = elementsWithMinHead.mkString("(", " ", ")")
}

object Cycle{

  private[combinatorics]
  class SeqCycle(val elements: Vector[Int]) extends Cycle{

    override def previousTo(i: Int): Int = elements.indexOf(i)-1 match {
      case -1 => elements.last
      case  j => elements(j)
    }

    override def nextTo(i: Int): Int = elements.indexOf(i)+1 match {
      case j if j == length => elements.head
      case j => elements(j)
    }
  }

  def apply(es: Int*): Cycle = {
    require(es.distinct == es, s"Cycle must not contain duplicate elements")
    new SeqCycle(es.toVector)
  }
}