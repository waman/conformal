package org.waman.conformal.linear

import spire.algebra._
import spire.implicits._
import spire.math._

case class Vector2D[F: Fractional](elements: List[F]) {
  def this(es: F*) = this(es.toList)
  require(elements.length == 2)

  def dim = elements.length
  def apply(i: Int): F = elements(i)
}

class RealVector2D(es: Real*) extends Vector2D[Real](es.toList)

object RealVector2D{
  def apply(es: Real*): RealVector2D = new RealVector2D(es:_*)
}

class NormedVectorSpace2D[F: Fractional] extends NormedVectorSpace[Vector2D[F], F]{

  override def norm(v: Vector2D[F]): F = ???

  override implicit def scalar: Field[F] = ???

  override def timesl(r: F, v: Vector2D[F]): Vector2D[F] = ???

  override def negate(x: Vector2D[F]): Vector2D[F] = ???

  override def zero: Vector2D[F] = Vector2D(List(0, 0))

  override def plus(x: Vector2D[F], y: Vector2D[F]): Vector2D[F] =
    Vector2D(x.elements.zip(y.elements).map(p => p._1 + p._2))
}