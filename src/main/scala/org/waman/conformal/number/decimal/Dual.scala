package org.waman.conformal.number.decimal

import spire.implicits._
import spire.algebra._
import spire.syntax.field._
import spire.syntax.isReal._
import spire.syntax.nroot._

import scala.annotation.tailrec
import scala.language.implicitConversions

import org.waman.conformal.number._

object Dual extends DualInstances {

  def zero[T](implicit T: Semiring[T]): Dual[T] =
    new Dual(T.zero, T.zero)

  def one[T](implicit T: Rig[T]): Dual[T] =
    new Dual(T.one, T.zero)

  def epsilon[T](implicit T: Rig[T]): Dual[T] =
    new Dual(T.zero, T.one)

  def fromInt[T](n: Int)(implicit f: Ring[T]): Dual[T] =
    new Dual(f.fromInt(n), f.zero)

  implicit def intToDual(n: Int): Dual[Double] = new Dual(n.toDouble, 0.0)
  implicit def longToDual(n: Long): Dual[Double] = new Dual(n.toDouble, 0.0)
  implicit def floatToDual(n: Float): Dual[Float] = new Dual(n, 0.0F)
  implicit def doubleToDual(n: Double): Dual[Double] = new Dual(n, 0.0)

  implicit def bigIntToDual(n: BigInt): Dual[BigDecimal] =
    bigDecimalToDual(BigDecimal(n))

  implicit def bigDecimalToDual(n: BigDecimal): Dual[BigDecimal] = {
    implicit val mc = n.mc
    new Dual(n, BigDecimal(0))
  }

  def apply[T](a: T)(implicit T: Semiring[T]): Dual[T] = new Dual(a, T.zero)
}

@SerialVersionUID(0L)
final case class Dual[T: Fractional](a: T, b: T)
  extends GenericSpireDecimalNumber[T] with Serializable{ lhs =>

//  protected def algebra: Fractional[T] = implicitly[Fractional[T]]

  //***** Properties *****
  def isSingular(implicit o: IsReal[T]): Boolean = b.isSignZero
  override def isZero(implicit o: IsReal[T]): Boolean = isSingular && a.isSignZero

  //***** Sign *****
  def signum(implicit o: IsReal[T]): Int = a.signum match {
    case 0 => b.signum
    case n => n
  }

  def abs(implicit f: Field[T], o: IsReal[T], n: NRoot[T]): T = a.abs

  //***** Family *****
  def unary_-  (implicit r: Rng[T]): Dual[T] = Dual(-a, -b)
  def conjugate(implicit f: Rng[T]): Dual[T] = Dual(a, -b)

  //***** Operation with Dual *****
  /** (a1 + b1ε) + (a2 + b2ε) = (a1 + a2) + (b1 + b2)ε */
  def +(y: Dual[T])(implicit r: Semiring[T]): Dual[T] = Dual(a + y.a, b + y.b)

  /** (a1 + b1ε) - (a2 + b2ε) = (a1 - a2) + (b1 - b2)ε */
  def -(y: Dual[T])(implicit r: Rng[T]): Dual[T] = Dual(a - y.a, b - y.b)

  /** (a1 + b1ε) * (a2 + b2ε) = (a1 * a2) + (a1 * b2 + b1 * a2)ε */
  def *(y: Dual[T])(implicit r: Rng[T]): Dual[T] = Dual(a * y.a, a * y.b + b * y.a)

  /** (a1 + b1ε) / (a2 + b2ε) = ((a1 + b1ε) * (a2 - b2ε)) / (a2 * a2)
    *                         = (a1 / a2) + (b1 / a2 - (a1 / a2) * (b2 / a2)) ε
    */
  def /(y: Dual[T])(implicit f: Field[T], o: IsReal[T]): Dual[T] = {
    val aRatio = a / y.a
    Dual(aRatio, b / y.a - aRatio * (y.b / y.a))
  }

  def /~(y: Dual[T])(implicit f: Field[T], o: IsReal[T]): Dual[T] = (this / y).floor

  def %(y: Dual[T])(implicit f: Field[T], o: IsReal[T]): Dual[T] = this - (this /~ y) * y

  def /%(y: Dual[T])(implicit f: Field[T], o: IsReal[T]): (Dual[T], Dual[T]) = {
    val q = this /~ y
    (q, this - q * y)
  }

  def **(p: Dual[T])(implicit f: Field[T], n: NRoot[T], t: Trig[T], o: IsReal[T]): Dual[T] = pow(p)

  def pow(p: Dual[T])(implicit f: Field[T], n: NRoot[T], t: Trig[T], o: IsReal[T]): Dual[T] = ???

  //***** integral exponent and n-th root *****
  def **(p: Int)(implicit f: Field[T], n: NRoot[T], t: Trig[T], o: IsReal[T]): Dual[T] = pow(p)

  /** (a + bε)**p = a**p + (p-1)*a**(p-1)ε */
  def pow(p: Int)(implicit f: Field[T], n: NRoot[T], t: Trig[T], o: IsReal[T]): Dual[T] =
    Dual(a**p, (p-1)*(a**(p-1)))

  def nroot(k: Int)(implicit f: Field[T], n: NRoot[T], t: Trig[T], o: IsReal[T]): Dual[T] = ???
//    if (isZero) Dual.zero else pow(Dual(f.fromInt(k).reciprocal, f.zero))

  //***** Operation with T *****
  def +(rhs: T)(implicit r: Semiring[T]): Dual[T] = Dual(a + rhs, b)
  def -(rhs: T)(implicit r: Rng[T])     : Dual[T] = Dual(a - rhs, b)
  def *(rhs: T)(implicit r: Semiring[T]): Dual[T] = Dual(a * rhs, b * rhs)
  def /(rhs: T)(implicit r: Field[T])   : Dual[T] = Dual(a / rhs, b / rhs)

  def /~(rhs: T)(implicit f: Field[T], o: IsReal[T]): Dual[T] = (this / rhs).floor
  def %(rhs: T) (implicit f: Field[T], o: IsReal[T]): Dual[T] = this - (this /~ rhs) * rhs
  def /%(rhs: T)(implicit f: Field[T], o: IsReal[T]): (Dual[T], Dual[T]) = {
    val q = this /~ rhs
    (q, this - q * rhs)
  }

  def **(p: T)(implicit f: Field[T], n: NRoot[T], t: Trig[T], o: IsReal[T]): Dual[T] = this pow p

  def pow(p: T)(implicit f: Field[T], n: NRoot[T], t: Trig[T], o: IsReal[T]): Dual[T] =
    if (p.isSignZero) {
      Dual.one[T]
    } else if (this.isZero) {
      if (p < f.zero)
        throw new Exception("raising 0 to negative/complex power")
      Dual.zero[T]
    } else {
      Dual(a**p, (p-1)*(a**p))
    }

  //***** Type Conversions *****
  def asTuple: (T, T) = (a, b)

  //***** Equivalency (and Order) and methods of Any *****
  def eqv(that: Dual[T])(implicit o: Eq[T]): Boolean = a === that.a && b === that.b
  def neqv(that: Dual[T])(implicit o: Eq[T]): Boolean = a =!= that.a || b =!= that.b

  override def equals(other: Any): Boolean = other match {
    case that: Dual[_]    =>
      that.canEqual(this) &&
        a == that.a &&
        b == that.b
    case that: Numeric[T] =>
      isSingular && that == this.a
    case _ => false
  }

  def canEqual(other: Any): Boolean =
    other.isInstanceOf[Dual[_]] || Numeric[T]

  override def hashCode =
    if(anyIsZero(a)) a.##
    else asTuple.##

  override def toString: String = s"($a + ${b}ε)"

  //***** Mathematical Functions *****
//  def log(implicit f: Field[T], n: NRoot[T], t: Trig[T], o: IsReal[T]): Dual[T] = ???
//
//  def sqrt(implicit f: Field[T], n0: NRoot[T], o: IsReal[T]): Dual[T] = ???
//
  def floor(implicit o: IsReal[T]): Dual[T] = Dual(a.floor)
  def ceil (implicit o: IsReal[T]): Dual[T] = Dual(a.ceil)
  def round(implicit o: IsReal[T]): Dual[T] = Dual(a.round)

//  // acos(z) = -i*(log(z + i*(sqrt(1 - z*z))))
  //  def acos(implicit f: Field[T], n: NRoot[T], t: Trig[T], o: IsReal[T]): Dual[T] = {
  //    val z2 = this * this
  //    val s = new Dual(f.one - z2.a, -z2.b).sqrt
  //    val l = new Dual(a + s.b, b + s.a).log
  //    new Dual(l.b, -l.a)
  //  }
  //
  //  // asin(z) = -i*(log(sqrt(1 - z*z) + i*z))
  //  def asin(implicit f: Field[T], n: NRoot[T], t: Trig[T], o: IsReal[T]): Dual[T] = {
  //    val z2 = this * this
  //    val s = new Dual(f.one - z2.a, -z2.b).sqrt
  //    val l = new Dual(s.a + -b, s.b + a).log
  //    new Dual(l.b, -l.a)
  //  }
  //
  //  // atan(z) = (i/2) log((i + z)/(i - z))
  //  def atan(implicit f: Field[T], r: NRoot[T], t: Trig[T], o: IsReal[T]): Dual[T] = {
  //    val n = new Dual(a, b + f.one)
  //    val d = new Dual(-a, f.one - b)
  //    val l = (n / d).log
  //    new Dual(l.b / f.fromInt(-2), l.a / f.fromInt(2))
  //  }

//  // exp(a+ci) = (exp(a) * cos(c)) + (exp(a) * sin(c))i
//  def exp(implicit f: Field[T], t: Trig[T]): Dual[T] =
//    new Dual(t.exp(a) * t.cos(b), t.exp(a) * t.sin(b))
//
//  // sin(a+ci) = (sin(a) * cosh(c)) + (cos(a) * sinh(c))i
//  def sin(implicit f: Field[T], t: Trig[T]): Dual[T] =
//    new Dual(t.sin(a) * t.cosh(b), t.cos(a) * t.sinh(b))
//
//  // sinh(a+ci) = (sinh(a) * cos(c)) + (cosh(a) * sin(c))i
//  def sinh(implicit f: Field[T], t: Trig[T]): Dual[T] =
//    new Dual(t.sinh(a) * t.cos(b), t.cosh(a) * t.sin(b))
//
//  // cos(a+ci) = (cos(a) * cosh(c)) - (sin(a) * sinh(c))i
//  def cos(implicit f: Field[T], t: Trig[T]): Dual[T] =
//    new Dual(t.cos(a) * t.cosh(b), -t.sin(a) * t.sinh(b))
//
//  // cosh(a+ci) = (cosh(a) * cos(c)) + (sinh(a) * sin(c))i
//  def cosh(implicit f: Field[T], t: Trig[T]): Dual[T] =
//    new Dual(t.cosh(a) * t.cos(b), t.sinh(a) * t.sin(b))
//
//  // tan(a+ci) = (sin(a+a) + sinh(c+c)i) / (cos(a+a) + cosh(c+c))
//  def tan(implicit f: Field[T], t: Trig[T]): Dual[T] = {
//    val r2 = a + a
//    val i2 = b + b
//    val d = t.cos(r2) + t.cosh(i2)
//    new Dual(t.sin(r2) / d, t.sinh(i2) / d)
//  }
//
//  // tanh(a+ci) = (sinh(a+a) + sin(c+c)i) / (cosh(a+a) + cos(c+c))
//  def tanh(implicit f: Field[T], t: Trig[T]): Dual[T] = {
//    val r2 = a + a
//    val i2 = b + b
//    val d = t.cos(r2) + t.cosh(i2)
//    new Dual(t.sinh(r2) / d, t.sin(i2) / d)
//  }
}



trait DualInstances0 {
  implicit def DualRing[A: Ring: IsReal]: Ring[Dual[A]] = new DualIsRingImpl[A]
}

trait DualInstances1 extends DualInstances0 {
  implicit def DualField[A: Field: IsReal]: Field[Dual[A]] = new DualIsFieldImpl[A]
}

trait DualInstances extends DualInstances1 {
  implicit def DualAlgebra[A: Fractional: Trig: IsReal]: DualAlgebra[A] =
    new DualAlgebra[A]

  implicit def DualEq[A: Eq]: Eq[Dual[A]] = new DualEq[A]
}

private[decimal] trait DualIsRing[A] extends Ring[Dual[A]] {

  implicit def algebra: Ring[A]
  implicit def order: IsReal[A]

  override def zero = Dual.zero
  override def one = Dual.one
  override def negate(a: Dual[A]) = -a
  override def plus(a: Dual[A], b: Dual[A]) = a + b
  override def minus(a: Dual[A], b: Dual[A]) = a - b
  override def times(a: Dual[A], b: Dual[A]) = a * b

  override def fromInt(n: Int) = Dual.fromInt[A](n)
}

private[decimal] trait DualIsField[A] extends DualIsRing[A] with Field[Dual[A]] {

  implicit def algebra: Field[A]

  override def div(a: Dual[A], b: Dual[A]) = a / b
  override def quot(a: Dual[A], b: Dual[A]) = a /~ b
  override def mod(a: Dual[A], b: Dual[A]) = a % b
  override def quotmod(a: Dual[A], b: Dual[A]) = a /% b

  override def fromDouble(n: Double) = Dual(algebra.fromDouble(n))

  override def gcd(a: Dual[A], b: Dual[A]) = ??? //{
//    @tailrec
//    def _gcd(a: Dual[A], b: Dual[A]): Dual[A] =
//      if (b.isZero) a else _gcd(b, a - (a / b).round * b)
//    _gcd(a, b)
//  }
}

//private[continuous] trait DualIsTrig[A] extends Trig[Dual[A]] {
//    implicit def algebra: Field[A]
//    implicit def nroot: NRoot[A]
//    implicit def trig: Trig[A]
//    implicit def order: IsReal[A]
//
//  override def e = new Dual[A](trig.e, algebra.zero)
//  override def pi = new Dual[A](trig.pi, algebra.zero)
//
//  override def exp(a: Dual[A]) = a.exp
//  override def expm1(a: Dual[A]) = a.exp - algebra.one
//  override def log(a: Dual[A]) = a.log
//  override def log1p(a: Dual[A]) = (a + algebra.one).log
//
//  override def sin(a: Dual[A]) = a.sin
//  override def cos(a: Dual[A]) = a.cos
//  override def tan(a: Dual[A]) = a.tan
//
//  override def asin(a: Dual[A]) = a.sin
//  override def acos(a: Dual[A]) = a.cos
//  override def atan(a: Dual[A]) = a.tan
//  override def atan2(y: Dual[A], x: Dual[A]) =
//    new Dual(x.a, y.b).atan
//
//  override def sinh(x: Dual[A]) = x.sinh
//  override def cosh(x: Dual[A]) = x.cosh
//  override def tanh(x: Dual[A]) = x.tanh
//
//  override def toRadians(a: Dual[A]) = a
//  override def toDegrees(a: Dual[A]) = a
//}
//
//private[continuous] trait DualIsNRoot[A] extends NRoot[Dual[A]] {
//  implicit def algebra: Field[A]
//  implicit def nroot: NRoot[A]
//  implicit def trig: Trig[A]
//  implicit def order: IsReal[A]
//
//  override def nroot(a: Dual[A], k: Int) = a.nroot(k)
//  override def sqrt(a: Dual[A]) = a.sqrt
//  override def fpow(a: Dual[A], b: Dual[A]) = a.pow(b)
//}

private[continuous] trait DualIsSigned[A] extends Signed[Dual[A]] {
  implicit def algebra: Field[A]
  implicit def nroot: NRoot[A]
  implicit def order: IsReal[A]

  override def signum(a: Dual[A]) = a.signum
  override def abs(a: Dual[A]) = new Dual[A](a.abs, algebra.zero)
}

@SerialVersionUID(1L)
private[decimal] class DualEq[A: Eq] extends Eq[Dual[A]] with Serializable {
  override def eqv(x: Dual[A], y: Dual[A]) = x eqv y
  override def neqv(x: Dual[A], y: Dual[A]) = x neqv y
}

@SerialVersionUID(1L)
private[decimal] final class DualIsRingImpl[A](implicit val algebra: Ring[A], val order: IsReal[A])
  extends DualIsRing[A] with Serializable

@SerialVersionUID(1L)
private[decimal] final class DualIsFieldImpl[A](implicit val algebra: Field[A], val order: IsReal[A])
  extends DualIsField[A] with Serializable

@SerialVersionUID(1L)
private[decimal] class DualAlgebra[A]
    (implicit val algebra: Field[A], val nroot: NRoot[A], val trig: Trig[A], val order: IsReal[A])
  extends DualIsField[A]
//    with DualIsTrig[A]
//    with DualIsNRoot[A]
//    with DualIsSigned[A]
    with InnerProductSpace[Dual[A], A]
    with FieldAlgebra[Dual[A], A]
    with Serializable {

  override def scalar: Field[A] = algebra
  override def timesl(p: A, v: Dual[A]) = new Dual(p * v.a, p * v.b)
  override def dot(x: Dual[A], y: Dual[A]) = scalar.plus(scalar.times(x.a, y.a), scalar.times(x.b, y.b))

  override def pow(a: Dual[A], b: Int) = a.pow(b)
}