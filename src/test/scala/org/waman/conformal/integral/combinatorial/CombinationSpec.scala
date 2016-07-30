package org.waman.conformal.integral.combinatorial

import org.waman.conformal.ConformalCustomSpec

class CombinationSpec extends ConformalCustomSpec{
  
  val combination = Combination(4)(0, 1, 3)

  "apply() method" - {

    "apply(Int) method should" - {

      "return the Boolean value which the argument Int value is contained or not" in {
        val conversions = Table(
          ("i", "expected"),
          (0, true),
          (1, true),
          (2, false),
          (3, true)
        )

        forAll(conversions){ (i: Int, expected: Boolean) =>
          __Exercise__
          val sut = combination(i)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "apply(Seq[Int]) method should" - {

      "return the combination of the argument Seq" in {
        __SetUp__
        val arg = Seq("a", "b", "c", "d")
        val expected = Seq("a", "b", "d")
        __Exercise__
        val sut = combination(arg)
        __Verify__
        sut should equal(expected)
      }
    }

    "apply(String) method should" - {

      "return the character combination of the argument String" in {
        __SetUp__
        val arg = "abcd"
        val expected = "abd"
        __Exercise__
        val sut = combination(arg)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "Order related methods" - {

    "Combination implements the Ordered trait" in {
      val conversions = Table(
        ("p0", "p1", "expected"),
        (combination, Combination(4, Set(0, 1, 2)), true),
        (combination, Combination(4, Set(3, 2, 1)), false)
      )

      forAll(conversions) { (c0: Combination, c1: Combination, expected: Boolean) =>
        __Exercise__
        val sut = c0 > c1
        __Verify__
        sut should equal(expected)
      }
    }

    "Two Permutations can not be compared when these degrees do not equal" in {
      __SetUp__
      val c0 = Permutation(0, 2, 1)
      val c1 = Permutation(0, 1, 3, 2)
      __Verify__
      an [IllegalArgumentException] should be thrownBy{
        c0 < c1
      }
    }
  }

  "toMap method should" - {

    "convert the Combination to a Map" in {
      __SetUp__
      val expected = Map(0 -> true, 1 -> true, 2 -> false, 3 -> true)
      __Exercise__
      val sut = combination.toMap
      __Verify__
      sut should equal (expected)
    }
  }

  "Methods of Any" - {

    "== operator should" - {

      "evaluate equality of combinations" in {
        val conversions = Table(
          ("p0", "p1", "expected"),
          (combination, combination, true),
          (combination, Combination(4, Set(3, 0, 1)), true),
          (combination, Combination(4, Set(3, 0, 2)), false),
          (combination, Combination(5, Set(3, 0, 1)), false)
        )

        forAll(conversions){ (p0: Combination, p1: Combination, expected: Boolean) =>
          __Exercise__
          val sut = p0 == p1
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "hashCode method should" - {

      "return the same value if two Combination objects are equivalent" in {
        __SetUp__
        val p = Combination(4, Set(3, 0, 1))
        assume(!combination.eq(p))
        val expected = combination.hashCode
        __Exercise__
        val sut = p.hashCode
        __Verify__
        sut should equal (expected)
      }
    }

    "toString method should" - {

      "return String representation of the partial combination" in {
        __Exercise__
        val sut = combination.toString
        __Verify__
        sut should equal ("(0, 1, [2], 3)")
      }
    }
  }

  "Combination companion object" - {

    "combinationCount(Int, Int) method should" - {

      "return the value nCr" in {
        val conversions = Table(
          ("n", "r", "expected"),
          (0, 0, 1),

          (1, 0, 1), (1, 1, 1),

          (2, 0, 1), (2, 1, 2), (2, 2, 1),

          (5, 0, 1) , (5, 1, 5), (5, 2, 10),
          (5, 3, 10), (5, 4, 5), (5, 5, 1)
        )

        forAll(conversions){ (n: Int, r: Int, expected: Int) =>
          __Exercise__
          val sut = Combination.combinationCount(n, r)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "apply() factory methods" - {

      "Combination(Int)(Int*) method should" - {

        "instantiate a Combination object" in {
          val conversions = Table(
            ("sut", "expected"),
            (Combination(4)(3, 0, 1), combination)
          )

          forAll(conversions){ (sut: Combination, expected: Combination) =>
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "allCombinations() methods" - {

      "allCombinations(Int, Int) method should" - {

        "return all combinations with the specified degree and rank" in {
          val conversions = Table(
            ("degree", "rank", "properIndicesOfExpected"),
            (1, 0, Seq(Set[Int]())),
            (1, 1, Seq(Set(0))),

            (3, 0, Seq(Set[Int]())),
            (3, 1, Seq(Set(0), Set(1), Set(2))),
            (3, 2, Seq(Set(0, 1), Set(0, 2), Set(1, 2))),
            (3, 3, Seq(Set(0, 1, 2)))
          )

          forAll(conversions){ (degree: Int, rank: Int, ex: Seq[Set[Int]]) =>
            __SetUp__
            val expected = ex.map(Combination(degree, _))
            __Exercise__
            val sut = Combination.allCombinations(degree, rank)
            __Verify__
            sut should equal (expected)
          }
        }

//        val degreeConversions = Table("degree", 1, 2, 3, 4, 5)
//
//        "return all partial combinations in lexicographic order" in {
//          forAll(degreeConversions) { degree: Int =>
//            (0 until degree).foreach{ rank: Int =>
//              __Exercise__
//              val sut = Combination.allCombinations(degree, rank)
//              __Verify__
//              sut should be (sorted)
//            }
//          }
//        }
//
//        "return all combinations which map Seq to those in lexicographic order" in {
//          forAll(degreeConversions) { degree: Int =>
//            (0 until degree).foreach{ rank: Int =>
//              __SetUp__
//              val s = "abcde".substring(0, degree)
//              __Exercise__
//              val sut = Combination.allCombinations(degree, rank).map(_.apply(s))
//              __Verify__
//              sut should be (sorted)
//            }
//          }
//        }
      }

      "allCombinations(Seq[E], Int) method should" - {

        "return all combinations of arg Seq with the specified rank" in {
          val conversions = Table(
            ("seq", "rank", "expected"),
            (Seq("a", "b", "c"), 0, Seq(Seq[String]())),
            (Seq("a", "b", "c"), 1, Seq(Seq("a"), Seq("b"), Seq("c"))),
            (Seq("a", "b", "c"), 2, Seq(Seq("a", "b"), Seq("a", "c"), Seq("b", "c"))),
            (Seq("a", "b", "c"), 3, Seq(Seq("a", "b", "c")))
          )

          forAll(conversions){ (seq: Seq[String], rank: Int, expected: Seq[Seq[String]]) =>
            __Exercise__
            val sut = Combination.allCombinations(seq, rank)
            __Verify__
            sut should equal (expected)
          }
        }
      }

      "allCombinations(String) method should" - {

        "return all combinations of arg String with the specified length(rank)" in {
          val conversions = Table(
            ("String", "length", "expected"),
            ("abc", 0, Seq("")),
            ("abc", 1, Seq("a", "b", "c")),
            ("abc", 2, Seq("ab", "ac", "bc")),
            ("abc", 3, Seq("abc"))
          )

          forAll(conversions){ (s: String, length: Int, expected: Seq[String]) =>
            __Exercise__
            val sut = Combination.allCombinations(s, length)
            __Verify__
            sut should equal (expected)
          }
        }
      }

      "allCombinations(Set[E], Int) method should" - {

        "return all combinations of arg Set with the specified rank" in {
          val conversions = Table(
            ("seq", "rank", "expected"),
            (Set("a", "b", "c"), 0, Seq(Set[String]())),
            (Set("a", "b", "c"), 1, Seq(Set("a"), Set("b"), Set("c"))),
            (Set("a", "b", "c"), 2, Seq(Set("a", "b"), Set("a", "c"), Set("b", "c"))),
            (Set("a", "b", "c"), 3, Seq(Set("a", "b", "c")))
          )

          forAll(conversions){ (seq: Set[String], rank: Int, expected: Seq[Set[String]]) =>
            __Exercise__
            val sut = Combination.allCombinations(seq, rank)
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }
  }
}
