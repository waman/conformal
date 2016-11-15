package org.waman.conformal.number.integer

import spire.implicits._
import spire.math.Integral

trait Modulo[I]{

  def module: I
  protected def algebra: Integral[I]

  def apply(value: I): ModuloNumber[I]
  def apply(s: String): ModuloNumber[I] = apply(algebra.fromBigInt(BigInt(s)))

  override def equals(other: Any): Boolean =
    other match {
      case that: Modulo[_] =>
        that.canEqual(this) &&
        this.module == that.module &&
        this.algebra == that.algebra
    }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Modulo[_]]

  override def hashCode: Int = (module, algebra).##
}

object Modulo{

  case class SimpleModulo[I: Integral](module: I)
      extends Modulo[I]{

    require(module > 0, s"Module must be positive: $module")

    override protected def algebra = implicitly[Integral[I]]

    override def apply(n: I): ModuloNumber[I] =
      if(n < 0) -apply(-n)
      else SimpleModuloNumber(n % module, this)
  }

  case class SimpleModuloNumber[I: Integral](value: I, modulo: SimpleModulo[I])
    extends ModuloNumber[I]{ lhs =>

    /** Create ModuloNumber object with the row value */
    override protected def create(n: I) = SimpleModuloNumber(n, modulo)

    /** Create ModuloNumber object with the value modified
      * (available even when more p-bit BigInt value) */
    override protected def build(n: I) = modulo(n)

    override def isZero = value.isZero
    override def isOne  = value.isOne

    override protected def calculateNegate =
      create(module - lhs.value)

    override protected def calculateSum(rhs: ModuloNumber[I]) =
      build(lhs.value + rhs.value)

    override protected def calculateProduct(rhs: ModuloNumber[I]) =
      build(lhs.value * rhs.value)
  }

  def apply[I: Integral](module: I): SimpleModulo[I] =
    SimpleModulo(module)
}

trait ModuloNumber[I]{ lhs: ModuloNumber[I] =>

  def modulo: Modulo[I]
  def module: I = modulo.module
  def value: I

  /** Create ModuloNumber object with the row value */
  protected def create(value: I): ModuloNumber[I]
  /** Create ModuloNumber object with the value modified */
  protected def build(value: I): ModuloNumber[I]

  def isZero: Boolean
  def isOne : Boolean

  protected def matchModules(rhs: ModuloNumber[I]): Unit =
    require(lhs.module == rhs.module,
      s"Modulus of two numbers must be the same value: ${lhs.module} and ${rhs.module}")

  def unary_- : ModuloNumber[I] =
    if(isZero) this
    else       calculateNegate

  protected def calculateNegate: ModuloNumber[I]

  def +(rhs: ModuloNumber[I]): ModuloNumber[I] = {
    if (lhs.isZero) rhs
    else if (rhs.isZero) lhs
    else calculateSum(rhs)
  }

  protected def calculateSum(rhs: ModuloNumber[I]): ModuloNumber[I]

  def -(rhs: ModuloNumber[I]): ModuloNumber[I] = {
    matchModules(rhs)
    if (lhs.isZero) -rhs
    else if (rhs.isZero) lhs
    else calculateSum(rhs.calculateNegate)
  }

  def *(rhs: ModuloNumber[I]): ModuloNumber[I] = {
    matchModules(rhs)
    if (lhs.isZero | rhs.isOne) lhs
    else if (lhs.isOne | rhs.isZero) rhs
    else calculateProduct(rhs)
  }

  protected def calculateProduct(rhs: ModuloNumber[I]): ModuloNumber[I]

//  def toInt: Int = value.toInt
//  def toLong: Long = lhs.toLong
//  def toBigInt: BigInt = lhs.toBigInt

  override def equals(other: Any): Boolean =
    other match {
      case that: ModuloNumber[_] =>
        that.canEqual(this) &&
          this.modulo == that.modulo &&
          this.value == that.value
    }

  def canEqual(other: Any): Boolean = other.isInstanceOf[ModuloNumber[_]]

  override def hashCode: Int = (modulo, value).##

  override def toString: String = s"$value (mod $module)"
}

abstract class ModuloSupplier[I: Integral]{

  import org.waman.conformal.memoize

  private lazy val cache: I => Modulo[I] = memoize(Modulo(_))

  def apply(m: I): Modulo[I] = cache(m)
}