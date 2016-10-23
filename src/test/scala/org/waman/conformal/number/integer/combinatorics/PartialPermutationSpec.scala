package org.waman.conformal.number.integer.combinatorics

import org.waman.conformal.ConformalCustomSpec

class PartialPermutationSpec extends ConformalCustomSpec{

  val perm = PartialPermutation(4)(3, 0, 1)

  "apply(Int) method should" - {

    "return the suffix corresponding to the argument index" in {
      val conversions =
        Table(
          ("p", "index", "expected"),
          (perm, 0, Some(1)),
          (perm, 1, Some(2)),
          (perm, 2, None),
          (perm, 3, Some(0))
        )

      forAll(conversions) { (p: PartialPermutation, index: Int, expected: Option[Int]) =>
        __Exercise__
        val sut = p(index)
        __Verify__
        sut should equal(expected)
      }
    }

    "throw an IllegalArgumentException if the corresponding suffix is not defined" in {
      __Verify__
      an [Exception] should be thrownBy{
        perm(100)
      }
    }
  }

  "apply(Seq[E]) method should" - {

    "execute place-base partial permutation of Seq" in {
      val conversions =
        Table(
          ("p", "degree", "expected"),
          (perm, 4, Seq(3, 0, 1))
        )

      forAll(conversions) { (p: PartialPermutation, degree: Int, expected: Seq[Int]) =>
        __SetUp__
        val args = 0 until degree
        __Exercise__
        val sut = p(args)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "Order related methods" - {

    "PartialPermutation implements Ordered trait" in {
      __SetUp__
      val p0 = PartialPermutation(4)(0, 1, 2)
      val p1 = PartialPermutation(4)(2, 0, 1)
      __Exercise__
      val sut = p0 < p1
      __Verify__
      sut should be (true)
    }

    "Two partial permutations can not be compared when these degrees do not equal" in {
      __SetUp__
      val p0 = PartialPermutation(4)(0, 2, 1)
      val p1 = PartialPermutation(4)(0, 1, 3, 2)
      __Verify__
      an [IllegalArgumentException] should be thrownBy{
        p0 < p1
      }
    }
  }

  "The methods of Any class" - {

    "toString method should" - {

      "create a String representation of permutation like [3 0 1 -]" in {
        val conversions =
          Table(
            ("p", "expected"),
            (PartialPermutation(4)(3, 0, 1), "[[3 0 1 -]]")
          )

        forAll(conversions) { (p: PartialPermutation, expected: String) =>
          __Exercise__
          val sut = p.toString
          __Verify__
          sut should equal(expected)
        }
      }
    }

    "equals() method should" - {

      "return true when the two partial permutation are equivalent even if not the same objects" in {
        val conversions =
          Table(
            ("p0", "p1"),
            (perm, perm),
            (perm, PartialPermutation(4)(3, 0, 1))
          )

        forAll(conversions) { (p0: PartialPermutation, p1: PartialPermutation) =>
          __Verify__
          p0 should equal(p1)
        }
      }
    }

    "hashCode property should" - {

      "return the same value if two partial permutations are equivalent" in {
        val conversions =
          Table(
            ("p0", "p1"),
            (perm, perm),
            (perm, PartialPermutation(4)(3, 0, 1))
          )

        forAll(conversions) { (p0: PartialPermutation, p1: PartialPermutation) =>
          __Verify__
          p0.hashCode should equal(p1.hashCode)
        }
      }
    }
  }

  "companion object" - {

    "The number of permutations" - {

      "permutationCount(Int, Int) method should" - {

        "return the value nPr" in {
          val conversions = Table(
            ("n", "r", "expected"),
            (0, 0, 1),

            (1, 0, 1), (1, 1, 1),

            (2, 0, 1), (2, 1, 2), (2, 2, 2),

            (5, 0, 1) , (5, 1, 5)  , (5, 2, 20),
            (5, 3, 60), (5, 4, 120), (5, 5, 120)
          )

          forAll(conversions){ (n: Int, r: Int, expected: Int) =>
            __Exercise__
            val sut = PartialPermutation.permutationCount(n, r)
            __Verify__
            sut should equal (expected)
          }
        }
      }


      "permutationCount1(Int, Int) method should" - {

        "return the value nPr" in {
          val conversions = Table(
            ("n", "r", "expected"),
            (0, 0, 1),

            (1, 0, 1), (1, 1, 1),

            (2, 0, 1), (2, 1, 2), (2, 2, 2),

            (5, 0, 1) , (5, 1, 5)  , (5, 2, 20),
            (5, 3, 60), (5, 4, 120), (5, 5, 120)
          )

          forAll(conversions){ (n: Int, r: Int, expected: Int) =>
            __Exercise__
            val sut = PartialPermutation.permutationCount1(n, r)
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "Partial permutation generation" - {

      "allPermutations(Int, Int) method should" - {

        "generate all partial permutations with the specified rank (length)" in {

          val conversions = Table(
            ("degree", "rank", "ex"),
            (3, 0, Set(Seq[Int]())),
            (3, 1, Set(Seq(0), Seq(1), Seq(2))),
            (3, 2, Set(
              Seq(0, 1), Seq(0, 2),
              Seq(1, 0), Seq(1, 2),
              Seq(2, 0), Seq(2, 1))),
            (3, 3, Set(
              Seq(0, 1, 2), Seq(0, 2, 1),
              Seq(1, 0, 2), Seq(1, 2, 0),
              Seq(2, 0, 1), Seq(2, 1, 0)))
          )

          forAll(conversions) { (degree: Int, rank: Int, ex: Set[Seq[Int]]) =>
            __SetUp__
            val expected = ex.map(PartialPermutation(degree, _))
            __Exercise__
            val sut = PartialPermutation.allPermutations(degree, rank)
            __Verify__
            sut.toSet should equal(expected)
          }
        }

        "generate all partial permutations in lexicographic order" in {
          val conversions = Table("degree", 1, 2, 3, 4, 5)

          forAll(conversions) { degree: Int =>
            (0 to degree).foreach{ rank: Int =>
              __Exercise__
              val sut = PartialPermutation.allPermutations(degree, rank)
              __Verify__
              sut should be (sorted)
            }
          }
        }
      }

      "allPartialPermuted(Seq[E], Int) method should" - {

        "return all partial permutations of the specified Seq with the specified rank (length)" in {
          val conversions = Table(
            ("rank", "expected"),
            (0, Set(Seq[String]())),
            (1, Set(Seq("a"), Seq("b"), Seq("c"))),
            (2, Set(Seq("a", "b"), Seq("a", "c"), Seq("b", "a"),
              Seq("b", "c"), Seq("c", "a"), Seq("c", "b"))),
            (3, Set(Seq("a", "b", "c"), Seq("a", "c", "b"), Seq("b", "a", "c"),
              Seq("b", "c", "a"), Seq("c", "a", "b"), Seq("c", "b", "a")))
          )

          forAll(conversions) { (rank: Int, expected: Set[Seq[String]]) =>
            __Exercise__
            val sut = PartialPermutation.allPermutations(Seq("a", "b", "c"), rank)
            __Verify__
            sut.toSet should equal(expected)
          }
        }
      }

      "allPermutations(String, Int) method should" - {

        "return all partial permutations of the specified String with the specified length (rank)" in {
          val conversions = Table(
            ("length", "expected"),
            (0, Set("")),
            (1, Set("a", "b", "c")),
            (2, Set("ab", "ac", "ba", "bc", "ca", "cb")),
            (3, Set("abc", "acb", "bac", "bca", "cab", "cba"))
          )

          forAll(conversions) { (length: Int, expected: Set[String]) =>
            __Exercise__
            val sut = PartialPermutation.allPermutations("abc", length)
            __Verify__
            sut.toSet should equal(expected)
          }
        }
      }
    }
  }
}
