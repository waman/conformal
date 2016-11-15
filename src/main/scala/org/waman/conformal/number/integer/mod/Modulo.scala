package org.waman.conformal.number.integer.mod

import spire.math.Integral
import spire.implicits._

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
      case _ => false
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