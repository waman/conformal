package org.waman.conformal.number.integral.combinatorics

import org.waman.conformal.ConformalCustomSpec

class CombinatorialGeneratorSpec extends ConformalCustomSpec{

  "Permutation" - {

    "permutationCount(Int) method should" - {

      "return the value n!" in {
        val conversions = Table(
          ("n", "expected"),
          (0, 1),
          (1, 1),
          (2, 2),
          (3, 6),
          (4, 24),
          (5, 120),
          (6, 720)
        )

        forAll(conversions){ (n: Int, expected: Int) =>
          __Exercise__
          val sut = CombinatorialGenerator.permutationCount(n)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "Permutation generation" - {

      "allPermutations(Int) method should" - {

        "generate all permutations of the specified degree" in {
          val conversions = Table(
            ("degree", "expected"),
            (0, Seq(Seq[Int]())),
            (1, Seq(Seq(0))),
            (2, Seq(Seq(0, 1), Seq(1, 0))),
            (3, Seq(Seq(0, 1, 2), Seq(0, 2, 1), Seq(1, 0, 2), Seq(1, 2, 0), Seq(2, 0, 1), Seq(2, 1, 0)))
          )

          forAll(conversions){ (degree: Int, expected: Seq[Seq[Int]]) =>
            __Exercise__
            val sut = CombinatorialGenerator.allPermutations(degree)
            __Verify__
            sut should equal (expected)
          }
        }
      }

      "allPermutations(String) method should" - {

        "generate all permutations of the specified String" in {
          val conversions = Table(
            ("arg", "expected"),
            ("", Seq("")),
            ("a", Seq("a")),
            ("ab", Seq("ab", "ba")),
            ("abc", Seq("abc", "acb", "bac", "bca", "cab", "cba"))
          )

          forAll(conversions){ (arg: String, expected: Seq[String]) =>
            __Exercise__
            val sut = CombinatorialGenerator.allPermutations(arg)
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }
  }

  "Partial Permutation" - {

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
          val sut = CombinatorialGenerator.permutationCount(n, r)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "allPermutations(Int, Int) method should" - {

      "generate all partial permutations of the specified degree and the specified rank" in {
        val conversions = Table(
          ("degree", "rank", "expected"),
          (0, 0, Seq(Seq[Int]())),

          (3, 0, Seq(Seq[Int]())),
          (3, 1, Seq(Seq(0), Seq(1), Seq(2))),
          (3, 2, Seq(Seq(0, 1), Seq(0, 2), Seq(1, 0), Seq(1, 2), Seq(2, 0), Seq(2, 1))),
          (3, 3, Seq(Seq(0, 1, 2), Seq(0, 2, 1), Seq(1, 0, 2), Seq(1, 2, 0), Seq(2, 0, 1), Seq(2, 1, 0)))
        )

        forAll(conversions){ (degree: Int, rank: Int, expected: Seq[Seq[Int]]) =>
          __Exercise__
          val sut = CombinatorialGenerator.allPermutations(degree, rank)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "allPermutations(String) method should" - {

      "generate all partial permutations of the specified String and the specified length (rank)" in {
        val conversions = Table(
          ("arg", "length", "expected"),
          ("", 0, Seq("")),

          ("abc", 0, Seq("")),
          ("abc", 1, Seq("a", "b", "c")),
          ("abc", 2, Seq("ab", "ac", "ba", "bc", "ca", "cb")),
          ("abc", 3, Seq("abc", "acb", "bac", "bca", "cab", "cba"))
        )

        forAll(conversions){ (arg: String, length: Int, expected: Seq[String]) =>
          __Exercise__
          val sut = CombinatorialGenerator.allPermutations(arg, length)
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }

  "Combination" - {

    "combinationCount(Int, Int) method should" - {

      "return the value nCr" in {
        val conversions = Table(
          ("n", "r", "expected"),
          (0, 0, 1),

          (1, 0, 1), (1, 1, 1),

          (2, 0, 1), (2, 1, 2), (2, 2, 1),

          (5, 0, 1), (5, 1, 5), (5, 2, 10),
          (5, 3, 10), (5, 4, 5), (5, 5, 1)
        )

        forAll(conversions) { (n: Int, r: Int, expected: Int) =>
          __Exercise__
          val sut = CombinatorialGenerator.combinationCount(n, r)
          __Verify__
          sut should equal(expected)
        }
      }

    }

    "allCombinations(Int, Int) method should" - {

      "generate all combinations of the specified degree and the specified rank" in {
        val conversions = Table(
          ("degree", "rank", "expected"),
          (0, 0, Seq(Seq[Int]())),

          (3, 0, Seq(Seq[Int]())),
          (3, 1, Seq(Seq(0), Seq(1), Seq(2))),
          (3, 2, Seq(Seq(0, 1), Seq(0, 2), Seq(1, 2))),
          (3, 3, Seq(Seq(0, 1, 2)))
        )

        forAll(conversions){ (degree: Int, rank: Int, expected: Seq[Seq[Int]]) =>
          __Exercise__
          val sut = CombinatorialGenerator.allCombinations(degree, rank)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "allCombinations(String) method should" - {

      "generate all combinations of the specified String and the specified length (rank)" in {
        val conversions = Table(
          ("arg", "length", "expected"),
          ("", 0, Seq("")),

          ("abc", 0, Seq("")),
          ("abc", 1, Seq("a", "b", "c")),
          ("abc", 2, Seq("ab", "ac", "bc")),
          ("abc", 3, Seq("abc"))
        )

        forAll(conversions){ (arg: String, length: Int, expected: Seq[String]) =>
          __Exercise__
          val sut = CombinatorialGenerator.allCombinations(arg, length)
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }
}
