package org.waman.conformal.integral

import org.waman.conformal.ConformalCustomSpec

class PartialPermutationSpec extends ConformalCustomSpec{

  //  / 3 0 1 (2) \ = / 0 1 (2) 3 \    (a, b, c, d) -> (d, a, b)
  //  \ 0 1 2 (-) /   \ 1 2 (-) 0 /
  val partialPermutation = PartialPermutation(4, List(3, 0, 1))

  "apply() method" - {

    "apply(Int) method should" - {

      "return the permuted index specified by the argument one" in {
        val conversions = Table(
          ("i", "expected"),
          (3, 0),
          (0, 1),
          (1, 2)
        )

        forAll(conversions){ (i: Int, expected: Int) =>
          __Exercise__
          val sut = partialPermutation(i)
          __Verify__
          sut should equal (expected)
        }
      }

      "throw an exception if the argument is out of range" in {
        val conversions = Table(
          "i", 2, 5
        )

        forAll(conversions){ i: Int =>
          __Verify__
          an [Exception] should be thrownBy{ partialPermutation(i) }
        }
      }
    }

    "applyOption(Int) method should" - {

      "return Some of the permuted index specified by the argument one or None if not isDefinedAt" in {
        val conversions = Table(
          ("i", "expected"),
          (3, Some(0)),
          (0, Some(1)),
          (1, Some(2)),
          (2, None),
          (5, None)
        )

        forAll(conversions) { (i: Int, expected: Option[Int]) =>
          __Exercise__
          val sut = partialPermutation.applyOption(i)
          __Verify__
          sut should equal(expected)
        }
      }
    }

    "apply(Seq[Int]) method should" - {

      "return the permuted Seq" in {
        __SetUp__
        val arg = Seq("a", "b", "c", "d")
        val expected = Seq("d", "a", "b")
        __Exercise__
        val sut = partialPermutation(arg)
        __Verify__
        sut should equal(expected)
      }
    }

    "apply(String) method should" - {

      "return the permuted Seq" in {
        __SetUp__
        val arg = "abcd"
        val expected = "dab"
        __Exercise__
        val sut = partialPermutation(arg)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "indexOf(Int) method should" - {

    "return the index mapped to the specified Int" in {
      val conversions = Table(
        ("i", "expected"),
        (0, 3),
        (1, 0),
        (2, 1)
      )

      forAll(conversions) { (i: Int, expected: Int) =>
        __Exercise__
        val sut = partialPermutation.indexOf(i)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "Order related methods" - {

    "PartialPermutation implements the Ordered trait" in {
      val conversions = Table(
        ("p0", "p1", "expected"),
        (partialPermutation, PartialPermutation(4, List(0, 1, 2)), true),
        (partialPermutation, PartialPermutation(4, List(3, 2, 1)), false)
      )

      forAll(conversions) { (p0: PartialPermutation, p1: PartialPermutation, expected: Boolean) =>
        __Exercise__
        val sut = p0 > p1
        __Verify__
        sut should equal(expected)
      }
    }

    "Two Permutations can not be compared when these degrees do not equal" in {
      __SetUp__
      val p0 = Permutation(0, 2, 1)
      val p1 = Permutation(0, 1, 3, 2)
      __Verify__
      an [IllegalArgumentException] should be thrownBy{
        p0 < p1
      }
    }
  }

  "toMap method should" - {

    "convert the partial permutation to Map" in {
      __SetUp__
      val expected = Map(0 -> Some(1), 1 -> Some(2), 2 -> None, 3 -> Some(0))
      __Exercise__
      val sut = partialPermutation.toMap
      __Verify__
      sut should equal (expected)
    }
  }

  "Methods of Any" - {

    "== operator should" - {

      "evaluate equality of partial permutations" in {
        val conversions = Table(
          ("p0", "p1", "expected"),
          (partialPermutation, partialPermutation, true),
          (partialPermutation, PartialPermutation(4, List(3, 0, 1)), true),
          (partialPermutation, PartialPermutation(4, List(3, 0, 2)), false),
          (partialPermutation, PartialPermutation(5, List(3, 0, 1)), false)
        )

        forAll(conversions){ (p0: PartialPermutation, p1: PartialPermutation, expected: Boolean) =>
          __Exercise__
          val sut = p0 == p1
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "hashCode method should" - {

      "return the same value if two PartialPermutation objects are equivalent" in {
        __SetUp__
        val p = PartialPermutation(4, Vector(3, 0, 1))
        assume(!partialPermutation.eq(p))
        val expected = partialPermutation.hashCode
        __Exercise__
        val sut = p.hashCode
        __Verify__
        sut should equal (expected)
      }
    }

    "toString method should" - {

      "return String representation of the partial permutation" in {
        __Exercise__
        val sut = partialPermutation.toString
        __Verify__
        sut should equal ("[0 -> 1, 1 -> 2, 2 -> None, 3 -> 0]")
      }
    }
  }

  "PartialPermutation companion object" - {

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

    "apply() factory methods" - {

      "PartialPermutation(Int)(E*) method should" - {

        "instantiate a PartialPermutation object" in {
          val conversions = Table(
            ("sut", "expected"),
            (PartialPermutation(4)(3, 0, 1), partialPermutation),
            (PartialPermutation(4)(3 -> 0, 0 -> 1, 1 -> 2), partialPermutation)
          )

          forAll(conversions){ (sut: PartialPermutation, expected: PartialPermutation) =>
            __Verify__
            sut should equal (partialPermutation)
          }
        }
      }
    }

    "allPermutations() methods" - {

      "allPermutations(Int, Int) method should" - {

        "return all partial permutations with the specified degree and rank" in {
          val conversions = Table(
            ("degree", "rank", "properIndicesOfExpected"),
            (3, 0, Set(List[Int]())),
            (3, 2, Set(
              List(0, 1), List(0, 2),
              List(1, 0), List(1, 2),
              List(2, 0), List(2, 1)))
          )

          forAll(conversions){ (degree: Int, rank: Int, ex: Set[List[Int]]) =>
            __SetUp__
            val expected = ex.map(PartialPermutation(degree)(_))
            __Exercise__
            val sut = PartialPermutation.allPermutations(degree, rank)
            __Verify__
            sut.toSet should equal (expected)
          }
        }

        val degreeConversions = Table("degree", 1, 2, 3, 4, 5)

        "return all partial permutations in lexicographic order" in {
          forAll(degreeConversions) { degree: Int =>
            (0 until degree).foreach{ rank: Int =>
              __Exercise__
              val sut = PartialPermutation.allPermutations(degree, rank)
              __Verify__
              sut should be (sorted)
            }
          }
        }

        "return all permutations which map Seq to those in lexicographic order" in {
          forAll(degreeConversions) { degree: Int =>
            (0 until degree).foreach{ rank: Int =>
              __SetUp__
              val s = "abcde".substring(0, degree)
              __Exercise__
              val sut = PartialPermutation.allPermutations(degree, rank).map(_.apply(s))
              __Verify__
              sut should be (sorted)
            }
          }
        }
      }

      "allPermutations(Seq[E], Int) method should" - {

        "return all partial permutations of arg seq with the specified rank" in {
          val conversions = Table(
            ("seq", "rank", "expected"),
            (List("a", "b", "c"), 0, Seq("")),
            (List("a", "b", "c"), 2, Seq("ab", "ac", "ba", "bc", "ca", "cb"))
          )

          forAll(conversions){ (seq: Seq[String], rank: Int, expected: Seq[String]) =>
            __Exercise__
            val sut = PartialPermutation.allPermutations(seq, rank)
            __Verify__
            sut.map(_.mkString) should equal (expected)
          }
        }
      }

      "allPermutations(String) method should" - {

        "return all partial permutations of arg String with the specified length(rank)" in {
          val conversions = Table(
            ("String", "length", "expected"),
            ("abc", 0, Seq("")),
            ("abc", 2, Seq("ab", "ac", "ba", "bc", "ca", "cb"))
          )

          forAll(conversions){ (s: String, length: Int, expected: Seq[String]) =>
            __Exercise__
            val sut = PartialPermutation.allPermutations(s, length)
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }
  }
}
