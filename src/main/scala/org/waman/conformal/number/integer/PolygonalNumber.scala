package org.waman.conformal.number.integer

import spire.math.Integral
import spire.implicits._

trait PolygonalNumber {
  def apply[I: Integral](n: I): I
  def stream[I: Integral]: Stream[I] = PolygonalNumber.integers[I].map(apply(_))
}

object PolygonalNumber{
  private[integer]
  def integers[I: Integral]: Stream[I] = Stream.from(1).map(implicitly[Integral[I]].fromInt)

  def apply(s: Int): PolygonalNumber = s match {
    case 3 => TriangularNumber
    case 4 => SquareNumber
    case 5 => PentagonalNumber
    case 6 => HexagonalNumber
    case _ => new PolygonalNumberImpl(s)
  }

  private class PolygonalNumberImpl(s: Int) extends PolygonalNumber{
    override def apply[I: Integral](n: I): I = (s-2)*n*(n-1)/~2 + n
  }
}

object TriangularNumber extends PolygonalNumber{
  override def apply[I: Integral](n: I): I = n*(n+1)/~2
}

object SquareNumber extends PolygonalNumber{
  override def apply[I: Integral](n: I): I = n*n
}

object PentagonalNumber extends PolygonalNumber{
  override def apply[I: Integral](n: I): I = n*(3*n-1)/~2
}

object HexagonalNumber extends PolygonalNumber{
  override def apply[I: Integral](n: I): I = n*(2*n-1)
}