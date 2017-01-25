package org.waman.conformal.number.integral.combinatorics

import org.waman.conformal.ConformalCustomSpec

class WithRepetitionSpec extends ConformalCustomSpec{

  "Permutation" - {

    "permutationCount(Int) method should" - {

      "return the value n^n (in this case, 0^0 = 1)" in {
        val conversions = Table(
          ("n", "expected"),
          (0, 1),
          (1, 1),
          (2, 4),
          (3, 27),
          (4, 256)
        )

        forAll(conversions){ (n: Int, expected: Int) =>
          __Exercise__
          val sut = WithRepetition.permutationCount(n)
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
            (2, Seq(Seq(0, 0), Seq(0, 1), Seq(1, 0), Seq(1, 1))),
            (3, Seq(
              Seq(0, 0, 0), Seq(0, 0, 1), Seq(0, 0, 2),
              Seq(0, 1, 0), Seq(0, 1, 1), Seq(0, 1, 2),
              Seq(0, 2, 0), Seq(0, 2, 1), Seq(0, 2, 2),

              Seq(1, 0, 0), Seq(1, 0, 1), Seq(1, 0, 2),
              Seq(1, 1, 0), Seq(1, 1, 1), Seq(1, 1, 2),
              Seq(1, 2, 0), Seq(1, 2, 1), Seq(1, 2, 2),

              Seq(2, 0, 0), Seq(2, 0, 1), Seq(2, 0, 2),
              Seq(2, 1, 0), Seq(2, 1, 1), Seq(2, 1, 2),
              Seq(2, 2, 0), Seq(2, 2, 1), Seq(2, 2, 2)))
          )

          forAll(conversions){ (degree: Int, expected: Seq[Seq[Int]]) =>
            __Exercise__
            val sut = WithRepetition.allPermutations(degree)
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
            ("ab", Seq("aa", "ab", "ba", "bb")),
            ("abc", Seq(
              "aaa", "aab", "aac",
              "aba", "abb", "abc",
              "aca", "acb", "acc",

              "baa", "bab", "bac",
              "bba", "bbb", "bbc",
              "bca", "bcb", "bcc",

              "caa", "cab", "cac",
              "cba", "cbb", "cbc",
              "cca", "ccb", "ccc"))
          )

          forAll(conversions){ (arg: String, expected: Seq[String]) =>
            __Exercise__
            val sut = WithRepetition.allPermutations(arg)
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
          (0, 1, 0),
          (0, 2, 0),

          (3, 0, 1),
          (3, 1, 3),
          (3, 2, 9),
          (3, 3, 27),
          (3, 4, 81)
        )

        forAll(conversions){ (n: Int, r: Int, expected: Int) =>
          __Exercise__
          val sut = WithRepetition.permutationCount(n, r)
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
          (0, 1, Seq[Seq[Int]]()),

          (3, 0, Seq(Seq[Int]())),
          (3, 1, Seq(Seq(0), Seq(1), Seq(2))),
          (3, 2, Seq(
            Seq(0, 0), Seq(0, 1), Seq(0, 2),
            Seq(1, 0), Seq(1, 1), Seq(1, 2),
            Seq(2, 0), Seq(2, 1), Seq(2, 2))),

          (4, 2, Seq(
            Seq(0, 0), Seq(0, 1), Seq(0, 2), Seq(0, 3),
            Seq(1, 0), Seq(1, 1), Seq(1, 2), Seq(1, 3),
            Seq(2, 0), Seq(2, 1), Seq(2, 2), Seq(2, 3),
            Seq(3, 0), Seq(3, 1), Seq(3, 2), Seq(3, 3)))
        )

        forAll(conversions){ (degree: Int, rank: Int, expected: Seq[Seq[Int]]) =>
          __Exercise__
          val sut = WithRepetition.allPermutations(degree, rank)
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
          ("", 1, Seq[String]()),

          ("abc", 0, Seq("")),
          ("abc", 1, Seq("a", "b", "c")),
          ("abc", 2, Seq(
            "aa", "ab", "ac",
            "ba", "bb", "bc",
            "ca", "cb", "cc")),

          ("abcd", 2, Seq(
            "aa", "ab", "ac", "ad",
            "ba", "bb", "bc", "bd",
            "ca", "cb", "cc", "cd",
            "da", "db", "dc", "dd"))
        )

        forAll(conversions){ (arg: String, length: Int, expected: Seq[String]) =>
          __Exercise__
          val sut = WithRepetition.allPermutations(arg, length)
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }

  "Combination" - {

    "combinationCount(Int, Int) method should" - {

      "return the value nHr = (n+r-1)Cr" in {
        val conversions = Table(
          ("n", "r", "expected"),
          (0, 0, 1),
          (0, 1, 0),

          (1, 0, 1), (1, 1, 1), (1, 2, 1),

          (2, 0, 1), (2, 1, 2), (2, 2, 3), (2, 3, 4),

          (3, 0, 1), (3, 1, 3), (3, 2, 6), (3, 3, 10), (3, 4, 15),

          (4, 0, 1), (4, 1, 4), (4, 2, 10), (4, 3, 20), (4, 4, 35), (4, 5, 56)
        )

        forAll(conversions) { (n: Int, r: Int, expected: Int) =>
          __Exercise__
          val sut = WithRepetition.combinationCount(n, r)
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
          (0, 1, Seq[Seq[Int]]()),

          (3, 0, Seq(Seq[Int]())),
          (3, 1, Seq(Seq(0), Seq(1), Seq(2))),
          (3, 2, Seq(
            Seq(0, 0), Seq(0, 1), Seq(0, 2),
            Seq(1, 1), Seq(1, 2), Seq(2, 2))),
          (3, 3, Seq(
            Seq(0, 0, 0), Seq(0, 0, 1), Seq(0, 0, 2), Seq(0, 1, 1), Seq(0, 1, 2),
            Seq(0, 2, 2), Seq(1, 1, 1), Seq(1, 1, 2), Seq(1, 2, 2), Seq(2, 2, 2))),
          (3, 4, Seq(
            Seq(0, 0, 0, 0), Seq(0, 0, 0, 1), Seq(0, 0, 0, 2), Seq(0, 0, 1, 1), Seq(0, 0, 1, 2),
            Seq(0, 0, 2, 2), Seq(0, 1, 1, 1), Seq(0, 1, 1, 2), Seq(0, 1, 2, 2), Seq(0, 2, 2, 2),
            Seq(1, 1, 1, 1), Seq(1, 1, 1, 2), Seq(1, 1, 2, 2), Seq(1, 2, 2, 2), Seq(2, 2, 2, 2)))
        )

        forAll(conversions){ (degree: Int, rank: Int, expected: Seq[Seq[Int]]) =>
          __Exercise__
          val sut = WithRepetition.allCombinations(degree, rank)
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
          ("", 1, Seq[String]()),

          ("abc", 0, Seq("")),
          ("abc", 1, Seq("a", "b", "c")),
          ("abc", 2, Seq("aa", "ab", "ac", "bb", "bc", "cc")),
          ("abc", 3, Seq(
            "aaa", "aab", "aac", "abb", "abc",
            "acc", "bbb", "bbc", "bcc", "ccc")),
          ("abc", 4, Seq(
            "aaaa", "aaab", "aaac", "aabb", "aabc",
            "aacc", "abbb", "abbc", "abcc", "accc",
            "bbbb", "bbbc", "bbcc", "bccc", "cccc"))
        )

        forAll(conversions){ (arg: String, length: Int, expected: Seq[String]) =>
          __Exercise__
          val sut = WithRepetition.allCombinations(arg, length)
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }
}
