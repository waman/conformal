package org.waman.conformal.integral

import org.waman.conformal.ConformalCustomSpec

class PermutationSpec extends ConformalCustomSpec{

  "toString method should" - {

    "create a String representation of permutation like (1 3 2)" in {
      __Exercise__
      val conversions =
        Table(
          ("p", "expected"),
          (new Permutation(0, 2, 1)         , "(0 2 1)"),
          (new Permutation(0, 2, 1, 4, 5, 3), "(0 2 1 4 5 3)")
        )

      __Verify__
      forAll(conversions){ (p: Permutation, expected: String) =>
        val sut = p.toString
        sut should equal (expected)
      }
    }
  }

  "apply() method should" - {

    "execute permutation of List" in {
      __Exercise__
      val conversions =
        Table(
          ("p", "arg", "expected"),
          (new Permutation(0, 2, 1)         , List("a", "b", "c")               , "acb"),
          (new Permutation(0, 2, 1, 4, 5, 3), List("a", "b", "c", "d", "e", "f"), "acbefd")
        )

      __Verify__
      forAll(conversions){ (p: Permutation, objs: List[Any], expected: String) =>
        val sut = p(objs)
        sut.mkString("") should equal (expected)
      }
    }
  }

  "*() method should" - {

    "compose two permutations" in {
      __Exercise__
      val conversions =
        Table(
          ("p0", "p1", "arg"),
          (new Permutation(0, 2, 1), new Permutation(1, 2, 0), List("a", "b", "c")),
          (new Permutation(0, 3, 2, 1), new Permutation(3, 1, 2, 0), List("a", "b", "c", "d"))
        )

      __Verify__
      forAll(conversions) { (p0: Permutation, p1: Permutation, arg: List[String]) =>
        val sut = (p0 * p1)(arg)
        val expected = p0(p1(arg))
        sut should equal(expected)
      }
    }
  }

  "sgn() method should" - {

    "return sign of permutation" in {
      __Exercise__
      val conversions =
        Table(
          ("p", "expected"),
          (new Permutation(0, 2, 1)         , -1),
          (new Permutation(0, 2, 1, 4, 5, 3), -1),
          (new Permutation(2, 0, 1)         ,  1)
        )

      __Verify__
      forAll(conversions) { (p: Permutation, expected: Int) =>
        val sut = p.sgn
        sut should equal(expected)
      }
    }
  }
}
