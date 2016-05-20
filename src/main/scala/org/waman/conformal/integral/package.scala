package org.waman.conformal

import spire.math.Integral
import scala.annotation.tailrec
import spire.implicits._

package object integral {


  def factorial[I: Integral](i: I): I = factorial(1, i)

  @tailrec
  private def factorial[I: Integral](prod: I, n: I): I = n match {
    case 0 => prod
    case _ => factorial(prod * n, n-1)
  }

  def permutationCount[I: Integral](n: I, r: I): I = permutationCount(1, n, r)

  @tailrec
  private def permutationCount[I: Integral](prod: I, n: I, r: I): I = r match {
    case 0 => prod
    case _ => permutationCount(prod * n, n-1, r-1)
  }
}
