package org.waman.conformal

import spire.math._
import spire.implicits._

import scala.annotation.tailrec

package object computerscience {

  def toBinaryString(i: ULong): String = ??? //{
//    @tailrec
//    def toBinaryString(s: String, i: ULong): String = i match {
//      case _ if i == ULong(0) => s
//      case _ =>
//        val digit = if((i & 1) == ULong(1)) "1" else "0"
//        toBinaryString(digit + s, i >>> 1)
//    }
//
//    toBinaryString("", i)
//  }
}
