package org.waman.conformal.number.integer

import spire.math.Integral
import spire.implicits._

trait IntegerSequence {
  def apply[I: Integral](n: I): I

  /** This method is equivalent to streamFrom(0) */
  def stream[I: Integral]: Stream[I] = streamFrom(0)
  /** This method is equivalent to streamFrom(1) */
  def stream1[I: Integral]: Stream[I] = streamFrom(1)
  def streamFrom[I: Integral](n: I): Stream[I] = IntegerSequence.from[I](n).map(apply(_))
}

object IntegerSequence{

  def sequence[I: Integral]: Stream[I] = from(1)

  def from[I: Integral](start: I): Stream[I] = from(start, 1)
  def from[I: Integral](start: I, step: I): Stream[I] = start #:: from(start + step)
}