package org.waman.conformal

import spire.math.Integral
import scala.annotation.tailrec
import spire.implicits._

package object integral {

  def factorial[I: Integral](i: I): I = {
    @tailrec
    def factorial(prod: I, n: I): I = n match {
      case 0 => prod
      case _ => factorial(prod * n, n-1)
    }

    factorial(1, i)
  }

  def permutationCount[I: Integral](n: I, r: I): I = {
    @tailrec
    def permutationCount(prod: I, n: I, r: I): I = r match {
      case 0 => prod
      case _ => permutationCount(prod * n, n-1, r-1)
    }

    permutationCount(1, n, r)
  }
}
