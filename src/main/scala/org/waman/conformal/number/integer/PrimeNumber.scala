package org.waman.conformal.number.integer

import spire.math.Integral
import spire.implicits._

object PrimeNumber extends IntegerSequence{

  override def apply[I: Integral](n: I): I = stream.apply(n.toInt)

  override def stream[I: Integral]: Stream[I] =
    2 #:: IntegerSequence.from[I](3, 2).filter(isPrime(_))

  override def streamFrom[I: Integral](n: I): Stream[I] = stream.drop(n.toInt)

  def isPrime[I: Integral](n: I): Boolean =
    n > 1 && stream.takeWhile(p => p*p <= n).forall(n % _ != 0)
}
