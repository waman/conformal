package org.waman.conformal.integral

import org.waman.conformal.ConformalCustomSpec

class PermutationSpec extends ConformalCustomSpec{

  "toString method should" - {

    "create a String representation of permutation like (132)" in {
      __SetUp__
      val p = new Permutation(0, 2, 1, 4, 5, 3)
      __Exercise__
      val sut = p.toString
      __Verify__
      sut should equal ("(021453)")
    }
  }

  "apply() method should" - {

    "execute permutation to List" in {
      __SetUp__
      val p = new Permutation(0, 2, 1, 4, 5, 3)
      val objs = List("a", "b", "c", "d", "e", "f")
      __Exercise__
      val sut = p(objs)
      __Verify__
      sut should equal (List("a", "c", "b", "e", "f", "d"))
    }
  }

  "sgn() method should" - {

    "return sign of permutation" in {
      __SetUp__
      val p = new Permutation(0, 2, 1, 4, 5, 3)
      __Exercise__
      val sut = p.sgn
      __Verify__
      sut should equal (-1)
    }
  }
}
