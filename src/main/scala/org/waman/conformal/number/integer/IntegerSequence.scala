package org.waman.conformal.number.integer

import spire.math.Integral
import spire.implicits._

trait IntegerSequence {
  def apply[I: Integral](n: I): I

  /** This method is equivalent to streamOf[Int] */
  def stream: Stream[Int] = streamOf[Int]
  /** This method is equivalent to stream1Of[Int] */
  def stream1: Stream[Int] = stream1Of[Int]
  /** This method is equivalent to streamAs[Int] */
  def streamFrom(n: Int): Stream[Int] = streamNthFrom[Int](n)

  /**
    * Return an integer stream starting at 0 index.
    * Use this method as <code>streamOf[Long]</code>
    * This method is equivalent to <code>streamFrom(0)</code>.
    */
  def streamOf[I: Integral]: Stream[I] = streamNthFrom(0)
  /** This method is equivalent to streamFrom(1) */
  def stream1Of[I: Integral]: Stream[I] = streamNthFrom(1)
  def streamNthFrom[I: Integral](n: I): Stream[I] = IntegerSequence.from[I](n).map(apply(_))
}

object IntegerSequence{

  def sequence[I: Integral]: Stream[I] = from(1)

  def from[I: Integral](start: I): Stream[I] = from(start, 1)
  def from[I: Integral](start: I, step: I): Stream[I] = start #:: from(start + step)
}