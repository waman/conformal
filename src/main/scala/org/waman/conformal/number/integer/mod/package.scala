package org.waman.conformal.number.integer

import spire.math.Integral

package object mod {

  implicit class ConformalModuloNumber[I: Integral](n: I){

    def mod(m: I)(implicit ms: ModuloSupplier[I]): ModuloNumber[I] =
      ms(m).apply(n)
  }

  //********** Modulo **********
  implicit object IntModuloSupplier extends ModuloSupplier[Int]
  implicit object LongModuloSupplier extends ModuloSupplier[Long]
  implicit object BigModuloSupplier extends ModuloSupplier[BigInt]
}
