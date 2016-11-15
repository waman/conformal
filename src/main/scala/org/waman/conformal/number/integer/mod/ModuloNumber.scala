package org.waman.conformal.number.integer.mod


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
    matchModules(rhs)
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
      case _ => false
    }

  def canEqual(other: Any): Boolean = other.isInstanceOf[ModuloNumber[_]]

  override def hashCode: Int = (modulo, value).##

  override def toString: String = s"$value (mod $module)"
}