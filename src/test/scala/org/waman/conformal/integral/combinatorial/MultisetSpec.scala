package org.waman.conformal.integral.combinatorial

import org.waman.conformal.ConformalCustomSpec

import scala.collection.immutable.SortedMap

class MultisetSpec extends ConformalCustomSpec{

  "calculateMultiplicity(Seq[E]) method should" - {

    "return a multiplicity map of the specified seq of Int" in {
      val conversions = Table(
        ("seq", "expected"),
        (Seq(0, 0, 0, 1, 1, 2, 2, 3, 4, 4), SortedMap(0 -> 3, 1 -> 2, 2 -> 2, 3 -> 1, 4 -> 2)),
        (Seq(0, 1, 0, 2, 0, 1), SortedMap(2 -> 1, 1 -> 2, 0 -> 3))
      )

      forAll(conversions){ (seq: Seq[Int], expected: SortedMap[Int, Int]) =>
        __Exercise__
        val sut = Multiset.calculateMultiplicity(seq)
        __Verify__
        sut should equal (expected)
      }
    }

    "return a multiplicity map of the specified seq of String" in {
      val conversions = Table(
        ("seq", "expected"),
        (Seq("a", "c", "b", "a", "b", "d", "a"), Map("a" -> 3, "b" -> 2, "c" -> 1, "d" -> 1))
      )

      forAll(conversions){ (seq: Seq[String], expected: Map[String, Int]) =>
        __Exercise__
        val sut = Multiset.calculateMultiplicity(seq)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "Permutation" - {

    "permutationCount(Seq[Any]) method should" - {

      "calculate the number of permutations of the specified seq" in {
        val conversions = Table(
          ("seq", "expected"),
          (Seq(0, 0, 0, 1, 1, 2, 2, 3, 4, 4), 75600),
          (Seq("a", "c", "b", "a", "b", "d", "a"), 420)
        )

        forAll(conversions){ (seq: Seq[Any], expected: Int) =>
          __Exercise__
          val sut = Multiset.permutationCount(seq)
          __Verify__
          sut should equal (expected)
        }
      }
    }
    
    "allPermutations(Seq[Any]) method should" - {

      "calculate the number of permutations of the specified seq" in {
        val conversions = Table(
          ("seq", "expected"),
          
          (Seq(0, 0, 1), Seq(
            Seq(0, 0, 1), Seq(0, 1, 0), Seq(1, 0, 0))),
          
          (Seq(0, 0, 1, 1), Seq(
            Seq(0, 0, 1, 1), Seq(0, 1, 0, 1), Seq(0, 1, 1, 0),
            Seq(1, 0, 0, 1), Seq(1, 0, 1, 0), Seq(1, 1, 0, 0)))
        )

        forAll(conversions){ (seq: Seq[Int], expected: Seq[Seq[Int]]) =>
          __Exercise__
          val sut = Multiset.allPermutations(seq)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    // This method behaves in the same way as Multiset#allPermutations(Int)
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
          val sut = Multiset.allPermutations(degree)
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
          ("aa", Seq("aa")),
          
          ("abc", Seq("abc", "acb", "bac", "bca", "cab", "cba")),
          ("aab", Seq("aab", "aba", "baa")),
          ("aba", Seq("aab", "aba", "baa"))
        )

        forAll(conversions){ (arg: String, expected: Seq[String]) =>
          __Exercise__
          val sut = Multiset.allPermutations(arg)
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }

  "Partial Permutation" - {

    "permutationCount(Int, Int) method should" - {

      "return the number of partial permutations" in {
        val conversions = Table(
          ("s", "r", "expected"),
          ("", 0, 1),

          ("a", 0, 1), ("a", 1, 1),

          ("ab", 0, 1), ("ab", 1, 2), ("ab", 2, 2),
          ("aa", 0, 1), ("aa", 1, 1), ("aa", 2, 1),

          ("abc", 0, 1), ("abc", 1, 3), ("abc", 2, 6), ("abc", 3, 6),
          ("aab", 0, 1), ("aab", 1, 2), ("aab", 2, 3), ("aab", 3, 3)
        )

        forAll(conversions){ (s: String, r: Int, expected: Int) =>
          __Exercise__
          val sut = Multiset.permutationCount(s, r)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    // This method behaves in the same way as Multiset#allPermutations(Int, Int)
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
          val sut = Multiset.allPermutations(degree, rank)
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
          ("abc", 3, Seq("abc", "acb", "bac", "bca", "cab", "cba")),

          ("aab", 0, Seq("")),
          ("aab", 1, Seq("a", "b")),
          ("aab", 2, Seq("aa", "ab", "ba")),
          ("aab", 3, Seq("aab", "aba", "baa"))
        )

        forAll(conversions){ (arg: String, length: Int, expected: Seq[String]) =>
          __Exercise__
          val sut = Multiset.allPermutations(arg, length)
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }

  "Combination" - {

    "combinationCount(Int, Int) method should" - {

      "return the number of combinations" in {
        val conversions = Table(
          ("s", "r", "expected"),
          ("", 0, 1),

          ("a", 0, 1), ("a", 1, 1),

          ("ab", 0, 1), ("ab", 1, 2), ("ab", 2, 1),
          ("aa", 0, 1), ("aa", 1, 1), ("aa", 2, 1),

          ("abc", 0, 1), ("abc", 1, 3), ("abc", 2, 3), ("abc", 3, 1),
          ("aab", 0, 1), ("aab", 1, 2), ("aab", 2, 2), ("aab", 3, 1)
        )

        forAll(conversions){ (s: String, r: Int, expected: Int) =>
          __Exercise__
          val sut = Multiset.combinationCount(s, r)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    // This method behaves in the same way as Multiset#allCombinations(Int, Int)
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
          val sut = Multiset.allCombinations(degree, rank)
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
          ("abc", 3, Seq("abc")),

          ("aab", 0, Seq("")),
          ("aab", 1, Seq("a", "b")),
          ("aab", 2, Seq("aa", "ab")),
          ("aab", 3, Seq("aab"))
        )

        forAll(conversions){ (arg: String, length: Int, expected: Seq[String]) =>
          __Exercise__
          val sut = Multiset.allCombinations(arg, length)
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }
}