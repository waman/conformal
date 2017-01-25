package org.waman.conformal.number.integral

import spire.math.Integral
import spire.implicits._

object PrimeNumber extends IntegralSequence{

  override def apply[I: Integral](n: I): I = streamOf.apply(n.toInt)

  override def streamOf[I: Integral]: Stream[I] =
    2 #:: IntegralSequence.from[I](3, 2).filter(isPrime(_))

  override def streamNthFrom[I: Integral](n: I): Stream[I] = streamOf.drop(n.toInt)

  def isPrime[I: Integral](n: I): Boolean =
    n > 1 && streamOf.takeWhile(p => p*p <= n).forall(n % _ != 0)
}