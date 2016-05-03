package org.waman.conformal

import spire.math.Integral
import spire.implicits._

import scala.annotation.tailrec

package object integral {

  @tailrec
  def factorial[I: Integral](prod: I, n: I): I = n match {
    case 0 => prod
    case _ => factorial(prod * n, n-1)
  }
}
