package org.waman.conformal.integral

import org.scalatest.OptionValues._
import org.scalatest.LoneElement._
import org.waman.conformal.ConformalCustomSpec

class PermutationSpec extends ConformalCustomSpec {

  "apply(List[E]) method should" - {

    "execute permutation of List" in {
      val conversions =
        Table(
          ("p", "arg", "expected"),
          (Permutation.identity(3), List("a", "b", "c"), "abc"),
          (Permutation(0, 2, 1), List("a", "b", "c"), "acb"),
          (Permutation(0, 2, 1, 4, 5, 3), List("a", "b", "c", "d", "e", "f"), "acbfde")
        )

      forAll(conversions) { (p: Permutation, objs: List[Any], expected: String) =>
        __Exercise__
        val sut = p(objs)
        __Verify__
        sut.mkString("") should equal(expected)
      }
    }
  }

  "* operator should" - {

    "compose two permutations" in {
      val conversions =
        Table(
          ("p0", "p1", "arg"),
          (Permutation.identity(3), Permutation(1, 2, 0), List("a", "b", "c")),
          (Permutation(0, 2, 1), Permutation(1, 2, 0), List("a", "b", "c")),
          (Permutation(0, 3, 2, 1), Permutation(3, 1, 2, 0), List("a", "b", "c", "d"))
        )

      forAll(conversions) { (p0: Permutation, p1: Permutation, arg: List[String]) =>
        __SetUp__
        val expected = p0(p1(arg))
        __Exercise__
        val sut = (p0 * p1) (arg)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "inverse method should" - {

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

  "sgn method should" - {

    "return sign of permutation" in {
      val conversions =
        Table(
          ("p", "expected"),
          (Permutation.identity(3), 1),
          (Permutation(0, 2, 1), -1),
          (Permutation(0, 2, 4, 1, 5, 3),  1),
          (Permutation(0, 2, 1, 4, 5, 3), -1),
          (Permutation(2, 0, 1), 1)
        )

      forAll(conversions) { (p: Permutation, expected: Int) =>
        __Exercise__
        val sut = p.sgn
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "Order related methods" - {

    "Permutation implements Ordered trait" in {
      __SetUp__
      val p0 = Permutation(0, 1, 2)
      val p1 = Permutation(2, 0, 1)
      __Exercise__
      val sut = p0 < p1
      __Verify__
      sut should be(true)
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

        forAll(conversions) { p: Permutation =>
          __Exercise__
          val sut = p.next
          __Verify__
          sut should equal(None)
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
          (Permutation(0, 2, 4, 1, 5, 3), Permutation(0, 2, 4, 3, 1, 5)),

          (Permutation(1, 2, 0) * Permutation(2, 1, 0) /* [0, 2, 1] */ , Permutation(1, 0, 2)),
          (Permutation(1, 2, 0).inverse /* [2, 0, 1] */ , Permutation(2, 1, 0))
        )

        forAll(conversions) { (p: Permutation, expected: Permutation) =>
          __Exercise__
          val sut = p.next
          __Verify__
          sut.value should equal(expected)
        }
      }
    }

    "sequenceNumber method should" - {

      "return the sequence number in lexicographic number" in {
        val conversions = Table(
          ("p", "expected"),
          (Permutation(0), 0),

          (Permutation(0, 1), 0),
          (Permutation(1, 0), 1),

          (Permutation(0, 1, 2), 0),
          (Permutation(0, 2, 1), 1),
          (Permutation(1, 0, 2), 2),
          (Permutation(1, 2, 0), 3),
          (Permutation(2, 0, 1), 4),
          (Permutation(2, 1, 0), 5),

          (Permutation(0, 1, 2, 3), 0),
          (Permutation(0, 3, 1, 2), 4),
          (Permutation(3, 1, 2, 0), 21)
        )

        forAll(conversions) { (p: Permutation, expected: Int) =>
          __Exercise__
          val sut = p.sequenceNumber
          __Verify__
          sut should equal(expected)
        }
      }
    }

    "sequenceNumberInFactorialRepresentation method should" - {

      "return the sequence number in lexicographic number as factorial representation" in {
        val conversions = Table(
          ("p", "expected"),
          (Permutation(0), List(0)),

          (Permutation(0, 1), List(0)),
          (Permutation(1, 0), List(1)),

          (Permutation(0, 1, 2), List(0)),
          (Permutation(0, 2, 1), List(1)),
          (Permutation(1, 0, 2), List(1, 0)),
          (Permutation(1, 2, 0), List(1, 1)),
          (Permutation(2, 0, 1), List(2, 0)),
          (Permutation(2, 1, 0), List(2, 1)),

          (Permutation(3, 1, 0, 5, 4, 2), List(3, 1, 0, 2, 1))
        )

        forAll(conversions) { (p: Permutation, expected: List[Int]) =>
          __Exercise__
          val sut = p.sequenceNumberInFactorialRepresentation
          __Verify__
          sut should equal (FactorialRepresentation(expected))
        }
      }
    }

    "The methods of Any class" - {

      "toString method should" - {

        "create a String representation of permutation like [1 3 2]" in {
          val conversions =
            Table(
              ("p", "expected"),
              (Permutation(0, 2, 1), "[0 2 1]"),
              (Permutation(0, 2, 1, 4, 5, 3), "[0 2 1 4 5 3]")
            )

          forAll(conversions) { (p: Permutation, expected: String) =>
            __Exercise__
            val sut = p.toString
            __Verify__
            sut should equal(expected)
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
          p0.hashCode should equal(p1.hashCode)
        }
      }
    }
  }

  "Companion object" - {

    "allPermutations(Int) method should" - {

      "generate all permutations of degree 1" in {
        val conversions = Table(
          "permutations",
          Permutation.allPermutationsInLexicographicOrder(1),
          Permutation.allPermutationsWithSignCalculated(1)
        )

        __SetUp__
        val expected = Set(Permutation(0))

        forAll(conversions){ ps: Seq[Permutation] =>
          __Verify__
          ps.toSet should equal (expected)
        }
      }

      "generate all permutations of degree 2" in {
        val conversions = Table(
          "permutations",
          Permutation.allPermutationsInLexicographicOrder(2),
          Permutation.allPermutationsWithSignCalculated(2)
        )

        val expected = Set(Permutation(0, 1), Permutation(1, 0))

        forAll(conversions){ ps: Seq[Permutation] =>
          __Verify__
          ps.toSet should equal (expected)
        }
      }

      "generate all permutations of degree 3" in {
        val conversions = Table(
          "permutations",
          Permutation.allPermutationsInLexicographicOrder(3),
          Permutation.allPermutationsWithSignCalculated(3)
        )

        val expected = Set(
          Permutation(0, 1, 2), Permutation(0, 2, 1),
          Permutation(1, 0, 2), Permutation(1, 2, 0),
          Permutation(2, 0, 1), Permutation(2, 1, 0))

        forAll(conversions){ ps: Seq[Permutation] =>
          __Verify__
          ps.toSet should equal (expected)
        }
      }

      "generate all permutations of degree 4" in {
        val conversions = Table(
          "permutations",
          Permutation.allPermutationsInLexicographicOrder(4),
          Permutation.allPermutationsWithSignCalculated(4)
        )

        __SetUp__
        val expected = Set(
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

        forAll(conversions){ ps: Seq[Permutation] =>
          val list = ps.toList
          __Verify__
          ps.toSet should equal (expected)
        }
      }

      "allPermutationsInLexicographicOrder method should" - {

        "return permutations in the lexicographic order" in {
          val conversions = Table("degree", 1, 2, 3, 4, 5)

          forAll(conversions){ degree: Int =>
            __Exercise__
            val sut = Permutation.allPermutationsInLexicographicOrder(degree)
            __Verify__
            sut should be (sorted)
          }
        }
      }
    }

    "allPermutations(Seq) method should" - {

      "generate all permutations of degree 1" in {
        __SetUp__
        val list = List("a")
        __Exercise__
        val sut = Permutation.allPermutations(list)
        __Verify__
        sut.loneElement should equal (List("a"))
      }

      "generate all permutations of degree 2" in {
        __SetUp__
        val list = List("a", "b")
        __Exercise__
        val sut = Permutation.allPermutations(list)
        __Verify__
        sut should equal (Seq(List("a", "b"), List("b", "a")))
      }

      "generate all permutations of degree 3" in {
        __SetUp__
        val list = List("a", "b", "c")
        __Exercise__
        val sut = Permutation.allPermutations(list)
        __Verify__
        sut should equal (Seq(
          List("a", "b", "c"), List("b", "a", "c"),
          List("a", "c", "b"), List("c", "a", "b"),
          List("c", "b", "a"), List("b", "c", "a")))
      }
    }

    "nthPermutation(Int, Int) method should " - {

      "return the permutation corresponding to the sequence number in lexicographic order" in {
        val conversions = Table(
          ("n", "degree", "expected"),
          (0, 3, Permutation(0, 1, 2)),
          (1, 3, Permutation(0, 2, 1)),
          (2, 3, Permutation(1, 0, 2)),
          (3, 3, Permutation(1, 2, 0)),
          (4, 3, Permutation(2, 0, 1)),
          (5, 3, Permutation(2, 1, 0)),

          (389, 6, Permutation(3, 1, 0, 5, 4, 2))
        )

        forAll(conversions) { (n: Int, degree: Int, expected: Permutation) =>
          __Exercise__
          val sut = Permutation.nthPermutation(n, degree)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "nthPermutation(FactorialRepresentation, Int) method should " - {

      "return the permutation corresponding to the sequence number in lexicographic order" in {
        val conversions = Table(
          ("n", "degree", "expected"),
          (List(0)   , 3, Permutation(0, 1, 2)),
          (List(1)   , 3, Permutation(0, 2, 1)),
          (List(1, 0), 3, Permutation(1, 0, 2)),
          (List(1, 1), 3, Permutation(1, 2, 0)),
          (List(2, 0), 3, Permutation(2, 0, 1)),
          (List(2, 1), 3, Permutation(2, 1, 0)),

          (List(3, 1, 0, 2, 1), 6, Permutation(3, 1, 0, 5, 4, 2))
        )

        forAll(conversions) { (n: List[Int], degree: Int, expected: Permutation) =>
          __SetUp__
          val fr = FactorialRepresentation(n)
          __Exercise__
          val sut = Permutation.nthPermutation(fr, degree)
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }
}