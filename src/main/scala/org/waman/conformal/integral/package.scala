package org.waman.conformal

import spire.math.Integral
import spire.implicits._
import scala.language.implicitConversions

import scala.annotation.tailrec

package object integral {

  implicit def convertIntToFactorialRepresentation(i: Int): FactorialRepresentation =
    FactorialRepresentation.fromInt(i)

  implicit def convertFactorialRepresentationToInt(fr: FactorialRepresentation): Int =
    fr.toInt

  def factorial[I: Integral](i: I): I = {
    @tailrec
    def factorial(prod: I, n: I): I = n match {
      case 0 => prod
      case _ => factorial(prod * n, n-1)
    }

    factorial(1, i)
  }

}
