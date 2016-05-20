package org.waman.conformal.integral

import org.scalatest.OptionValues._
import org.waman.conformal.ConformalCustomSpec

class PermutationSpec extends ConformalCustomSpec{

  "test" in {
    (0 to 0).sliding(3).foreach(println)
  }

  "apply(List[E]) method should" - {

    "execute permutation of List" in {
      val conversions =
        Table(
          ("p", "arg", "expected"),
          (Permutation.identity(3)      , List("a", "b", "c")               , "abc"),
          (Permutation(0, 2, 1)         , List("a", "b", "c")               , "acb"),
          (Permutation(0, 2, 1, 4, 5, 3), List("a", "b", "c", "d", "e", "f"), "acbfde")
        )

      forAll(conversions){ (p: Permutation, objs: List[Any], expected: String) =>
        __Exercise__
        val sut = p(objs)
        __Verify__
        sut.mkString("") should equal (expected)
      }
    }
  }

  "*() method should" - {

    "compose two permutations" in {
      val conversions =
        Table(
          ("p0", "p1", "arg"),
          (Permutation.identity(3), Permutation(1, 2, 0)   , List("a", "b", "c")),
          (Permutation(0, 2, 1)   , Permutation(1, 2, 0)   , List("a", "b", "c")),
          (Permutation(0, 3, 2, 1), Permutation(3, 1, 2, 0), List("a", "b", "c", "d"))
        )

      forAll(conversions) { (p0: Permutation, p1: Permutation, arg: List[String]) =>
        __SetUp__
        val expected = p0(p1(arg))
        __Exercise__
        val sut = (p0 * p1)(arg)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "inverse() method should" - {

    "return the inverse permutation" in {
      val conversions =
        Table(
          ("p", "expected"),
          (Permutation.identity(3), List("a", "b", "c")),
          (Permutation(0, 2, 1), List("a", "b", "c")),
          (Permutation(0, 3, 2, 1), List("a", "b", "c", "d"))
        )

      forAll(conversions) { (p: Permutation, expected: List[String]) =>
        __SetUp__
        val recip = p.inverse
        val arg = p(expected)
        __Exercise__
        val sut = recip(arg)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "sgn() method should" - {

    "return sign of permutation" in {
      val conversions =
        Table(
          ("p", "expected"),
          (Permutation.identity(3)      ,  1),
          (Permutation(0, 2, 1)         , -1),
          (Permutation(0, 2, 1, 4, 5, 3), -1),
          (Permutation(2, 0, 1)         ,  1)
        )

      forAll(conversions) { (p: Permutation, expected: Int) =>
        __Exercise__
        val sut = p.sgn
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "next method should" - {

    "return None if the next permutation does not exist" in {
      val conversions = Table(
        "p",
        Permutation(0),
        Permutation(1, 0),
        Permutation(2, 1, 0),
        Permutation(3, 2, 1, 0),
        Permutation(4, 3, 2, 1, 0)
      )

      forAll(conversions){ p: Permutation =>
        __Exercise__
        val sut = p.next
        __Verify__
        sut should equal (None)
      }

    }

    "return the next permutation in lexicographic order" in {
      val conversions = Table(
        ("p", "expected"),
        (Permutation(0, 1), Permutation(1, 0)),
        (Permutation(0, 1, 2), Permutation(0, 2, 1)),
        (Permutation(0, 2, 1), Permutation(1, 0, 2)),
        (Permutation(1, 0, 2), Permutation(1, 2, 0)),
        (Permutation(2, 0, 1), Permutation(2, 1, 0)),
        (Permutation(0, 1, 2, 3), Permutation(0, 1, 3, 2)),
        (Permutation(0, 1, 3, 2), Permutation(0, 2, 1, 3)),
        (Permutation(0, 3, 2, 1), Permutation(1, 0, 2, 3)),
        (Permutation(1, 0, 2, 3), Permutation(1, 0, 3, 2)),
        (Permutation(1, 3, 2, 0), Permutation(2, 0, 1, 3)),
        (Permutation(2, 0, 1, 3), Permutation(2, 0, 3, 1)),
        (Permutation(2, 3, 1, 0), Permutation(3, 0, 1, 2)),
        (Permutation(0, 2, 4, 1, 5, 3), Permutation(0, 2, 4, 3, 1, 5))
      )

      forAll(conversions){ (p: Permutation, expected: Permutation) =>
        __Exercise__
        val sut = p.next
        __Verify__
        sut.value should equal (expected)
      }
    }
  }

  "The methods of Any class" - {

    "toString method should" - {

      "create a String representation of permutation like [1 3 2]" in {
        val conversions =
          Table(
            ("p", "expected"),
            (Permutation(0, 2, 1)         , "[0 2 1]"),
            (Permutation(0, 2, 1, 4, 5, 3), "[0 2 1 4 5 3]")
          )

        forAll(conversions){ (p: Permutation, expected: String) =>
          __Exercise__
          val sut = p.toString
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "equals() method should" - {

      "return true if " in {
        val p = Permutation(0, 2, 1, 3)
        val conversions =
          Table(
            ("p0", "p1"),
            (p, p),
            (Permutation(0, 3, 1, 2), Permutation(0, 3, 1, 2)),
            (Permutation.identity(3), Permutation(0, 1, 2))
          )

        forAll(conversions) { (p0: Permutation, p1: Permutation) =>
          __Verify__
          p0 should equal(p1)
        }
      }
    }

    "hashCode property should" - {

      "return the same value if two permutations are equivalent" in {
        __SetUp__
        val p0 = Permutation(0, 3, 1, 2)
        val p1 = Permutation(0, 3, 1, 2)
        assume(p0 ne p1)
        __Verify__
        p0.hashCode should equal (p1.hashCode)
      }
    }
  }
}