package org.waman.conformal

import spire.algebra.{NRoot, Semiring, Trig}
import spire.math.Complex

import scala.language.implicitConversions

package object math {

  implicit def convertToComplex[T: Semiring](x: T): Complex[T] = Complex(x)

  //***** NRoot *****
  def nroot[T](x: T, n: Int)(implicit t: NRoot[T]): T = t.nroot(x, n)
  def sqrt[T](x: T)(implicit t: NRoot[T]): T = t.sqrt(x)
  def cbrt[T](x: T)(implicit t: NRoot[T]): T = t.nroot(x, 3)
  def fpow[T](x: T, y: T)(implicit t: NRoot[T]): T = t.fpow(x, y)

  //***** Trig *****
  def e[T](implicit t: Trig[T]): T = t.e
  def pi[T](implicit t: Trig[T]): T = t.pi
  
  def exp[T](x: T)(implicit t: Trig[T]): T = t.exp(x)
  def expm1[T](x: T)(implicit t: Trig[T]): T = t.expm1(x)
  def log[T](x: T)(implicit t: Trig[T]): T = t.log(x)
  def log1p[T](x: T)(implicit t: Trig[T]): T = t.log1p(x)
  
  def sin[T](x: T)(implicit t: Trig[T]): T = t.sin(x)
  def cos[T](x: T)(implicit t: Trig[T]): T = t.cos(x)
  def tan[T](x: T)(implicit t: Trig[T]): T = t.tan(x)
  
  def asin[T](x: T)(implicit t: Trig[T]): T = t.asin(x)
  def acos[T](x: T)(implicit t: Trig[T]): T = t.acos(x)
  def atan[T](x: T)(implicit t: Trig[T]): T = t.atan(x)
  def atan2[T](x: T, y: T)(implicit t: Trig[T]): T = t.atan2(x, y)
  
  def sinh[T](x: T)(implicit t: Trig[T]): T = t.sinh(x)
  def cosh[T](x: T)(implicit t: Trig[T]): T = t.cosh(x)
  def tanh[T](x: T)(implicit t: Trig[T]): T = t.tanh(x)
}
