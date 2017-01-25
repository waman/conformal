package org.waman.conformal.number.integral

import spire.math.Integral
import spire.implicits._

trait PolygonalNumber extends IntegralSequence

object PolygonalNumber{

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