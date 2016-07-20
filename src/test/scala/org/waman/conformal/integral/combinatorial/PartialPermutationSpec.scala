package org.waman.conformal.integral.combinatorial

import org.waman.conformal.ConformalCustomSpec

class PartialPermutationSpec extends ConformalCustomSpec{

  "companion object" - {

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
