package org.waman.conformal

import scala.math.ScalaNumericConversions

package object number {

  /** Refer to spire.math.anyIsZero */
  private[number] def anyIsZero(n: Any): Boolean =
    n match {
      case x if x == 0 => true
      case c: ScalaNumericConversions => c.isValidInt && c.toInt == 0
      case _ => false
    }
}
