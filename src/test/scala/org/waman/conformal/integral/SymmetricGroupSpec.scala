package org.waman.conformal.integral

import org.waman.conformal.ConformalCustomSpec
import org.scalatest.LoneElement._

class SymmetricGroupSpec extends ConformalCustomSpec{

  "permutations() method should" - {

    "generate permutations for degree = 1" in {
      __SetUp__
      val sg1 = SymmetricGroup(1)
      val expected = Permutation(0)
      __Exercise__
      val ps = sg1.permutations
      __Verify__
      ps.length should equal (1)
      ps.loneElement should equal (expected)
    }

    "generate permutations for degree = 2" in {
      __SetUp__
      val sg2 = SymmetricGroup(2)
      __Exercise__
      val ps = sg2.permutations
      __Verify__
      ps.length should equal (2)
      ps should contain allOf(Permutation(0, 1), Permutation(1, 0))
    }

    "generate permutations for degree = 3" in {
      __SetUp__
      val sg3 = SymmetricGroup(3)
      __Exercise__
      val ps = sg3.permutations
      __Verify__
      ps.length should equal (6)
      ps should contain allOf(
        Permutation(0, 1, 2), Permutation(0, 2, 1),
        Permutation(1, 0, 2), Permutation(1, 2, 0),
        Permutation(2, 0, 1), Permutation(2, 1, 0))
    }

    "generate permutations for degree = 4" in {
      __SetUp__
      val sg4 = SymmetricGroup(4)
      __Exercise__
      val ps = sg4.permutations
      __Verify__
      ps.length should equal (24)
      ps should contain allOf(
        Permutation(0, 1, 2, 3), Permutation(0, 1, 3, 2),
        Permutation(0, 2, 1, 3), Permutation(0, 2, 3, 1),
        Permutation(0, 3, 1, 2), Permutation(0, 3, 2, 1),

        Permutation(1, 0, 2, 3), Permutation(1, 0, 3, 2),
        Permutation(1, 2, 0, 3), Permutation(1, 2, 3, 0),
        Permutation(1, 3, 0, 2), Permutation(1, 3, 2, 0),

        Permutation(2, 0, 1, 3), Permutation(2, 0, 3, 1),
        Permutation(2, 1, 0, 3), Permutation(2, 1, 3, 0),
        Permutation(2, 3, 0, 1), Permutation(2, 3, 1, 0),

        Permutation(3, 0, 1, 2), Permutation(3, 0, 2, 1),
        Permutation(3, 1, 0, 2), Permutation(3, 1, 2, 0),
        Permutation(3, 2, 0, 1), Permutation(3, 2, 1, 0))
    }
  }

  "test" in {
    val n = 5

//    println(SymmetricGroup(n).permutations.length)

    val base = (0 until n).toList
    SymmetricGroup(n).permutations.foreach{ p =>
      print(s"""$p ${p.sgn}""")
      val ex = Permutation(p(base).toArray:_*).inverse
      println(s""" $ex ${ex.sgn}""")
    }
  }
}
