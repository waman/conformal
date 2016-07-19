package org.waman.conformal.integral.combinatorial

import org.scalatest.OptionValues._
import org.waman.conformal.ConformalCustomSpec
import org.waman.conformal.integral.FactorialRepresentation

class PassivePermutationSpec extends ConformalCustomSpec {

  val f1 = Permutation.passive("a")
  val f2 = Permutation.passive("a", "b")
  val f3 = Permutation.passive("a", "b", "c")
  val f4 = Permutation.passive("a", "b", "c", "d")
  val f5 = Permutation.passive("a", "b", "c", "d", "e")
  val f6 = Permutation.passive("a", "b", "c", "d", "e", "f")

  val perm0 = f4("d", "b", "a", "c")
  val perm1 = f4("a", "d", "c", "b")
  val id = f4.identity
  val prod = perm0 * perm1  // ("d", "c", "a", "b")
  val inv = perm0.inverse  // ("c", "b", "d", "a")

  def alphabet(n: Int): Seq[String] = ('a' to 'z').take(n).map(_.toString)

  /**
    * -Permutation
    * -identity
    * -*
    * -inverse
    */

  "apply(E) method should" - {

    "return the corresponding element" in {
      val conversions = Table(
        ("p", "arg", "expected"),
        (perm0, "a", "d"),
        (perm0, "b", "b"),
        (perm0, "c", "a"),
        (perm0, "d", "c"),
        (id, "a", "a"),
        (id, "b", "b"),
        (id, "c", "c"),
        (id, "d", "d"),
        (prod, "a", "d"),
        (prod, "b", "c"),
        (prod, "c", "a"),
        (prod, "d", "b"),
        (inv, "a", "c"),
        (inv, "b", "b"),
        (inv, "c", "d"),
        (inv, "d", "a")
      )

      forAll(conversions){ (p: PassivePermutation[String], arg: String, expected: String) =>
        __Exercise__
        val sut = p(arg)
        __Verify__
        sut should equal (expected)

      }
    }
  }

  "indexOf(E) method should" - {

    "return the index corresponding to the argument suffix" in {
      val conversions = Table(
        ("p", "index", "expected"),
        (perm0, "a", "c"),
        (perm0, "b", "b"),
        (perm0, "c", "d"),
        (perm0, "d", "a"),
        (id, "a", "a"),
        (id, "b", "b"),
        (id, "c", "c"),
        (id, "d", "d"),
        (prod, "a", "c"),
        (prod, "b", "d"),
        (prod, "c", "b"),
        (prod, "d", "a"),
        (inv, "a", "d"),
        (inv, "b", "b"),
        (inv, "c", "a"),
        (inv, "d", "c")
      )

      forAll(conversions) { (p: PassivePermutation[String], i: String, expected: String) =>
        __Exercise__
        val sut = p.indexOf(i)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "apply(Seq[E]) method should" - {

    "return the permuted Seq" in {
      val conversions = Table(
        ("p", "expected"),
        (perm0, Seq("d", "b", "a", "c")),
        (id, Seq("a", "b", "c", "d")),
        (prod, Seq("d", "c", "a", "b")),
        (inv, Seq("c", "b", "d", "a"))
      )
      
      forAll(conversions){ (p: PassivePermutation[String], expected: Seq[String]) =>
        __SetUp__
        val arg = alphabet(p.degree)
        __Exercise__
        val sut = p(arg)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "* operator should" - {

    "compose two permutations" in {
      val conversions =
        Table(
          ("p0", "p1", "arg"),
          (perm0, perm1, prod),
          (f4("a", "d", "c", "b"), f4("d", "b", "c", "a"), f4("b", "d", "c", "a"))
        )

      forAll(conversions) { (p0: PassivePermutation[String], p1: PassivePermutation[String], expected: PassivePermutation[String]) =>
        __Exercise__
        val sut = p0 * p1
        __Verify__
        sut should equal(expected)
      }
    }

    "compose two permutations to one which maps the argument Seq well" in {
      val conversions =
        Table(
          ("p0", "p1"),
          (perm0, perm1),
          (id, perm0),
          (prod, perm0),
          (inv, perm0)
        )

      forAll(conversions) { (p0: PassivePermutation[String], p1: PassivePermutation[String]) =>
        __SetUp__
        val arg = alphabet(p0.degree)
        val expected = p0(p1(arg))
        __Exercise__
        val sut = (p0 * p1)(arg)
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
          (perm0, f4("c", "b", "d", "a")),
          (id, f4("a", "b", "c", "d")),
          (prod, f4("c", "d", "b", "a")),
          (inv, f4("d", "b", "a", "c"))
        )

      forAll(conversions) { (p: PassivePermutation[String], expected: PassivePermutation[String]) =>
        __Exercise__
        val sut = p.inverse
        __Verify__
        sut should equal(expected)
      }
    }

    "return the inverse permutation which maps the argument Seq inversely" in {
      val conversions = Table(
        "p",
        perm0,
        id,
        prod,
        inv)

      forAll(conversions) { p: PassivePermutation[String] =>
        __SetUp__
        val expected = alphabet(p.degree)
        val arg = p(expected)
        __Exercise__
        val sut = p.inverse(arg)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "sign method should" - {

    "return sign of permutation" in {
      val conversions =
        Table(
          ("p", "expected"),
          (perm0, 1),
          (perm1, -1),
          (id,  1),
          (prod, -1),
          (inv, 1)
        )

      forAll(conversions) { (p: PassivePermutation[String], expected: Int) =>
        __Exercise__
        val sut = p.sign
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "Order related methods" - {

    "Permutation implements Ordered trait" in {
      __Exercise__
      val sut = perm0 > perm1
      __Verify__
      sut should be(true)
    }

    "Two Permutations can not be compared when these degrees do not equal" in {
      __SetUp__
      val perm = Permutation.passive("a", "b", "c").identity
      __Verify__
      an [IllegalArgumentException] should be thrownBy{
        perm0 < perm
      }
    }

    "next method should" - {

      "return None if the next permutation does not exist" in {
        val conversions = Table(
          "p",
          f1("a"),
          f2("b", "a"),
          f3("c", "b", "a"),
          f4("d", "c", "b", "a"),
          f5("e", "d", "c", "b", "a")
        )

        forAll(conversions) { p: PassivePermutation[String] =>
          __Exercise__
          val sut = p.next
          __Verify__
          sut should equal(None)
        }

      }

      "return the next permutation in lexicographic order" in {
        val conversions = Table(
          ("p", "expected"),
          (f2("a", "b"), f2("b", "a")),

          (f3("a", "b", "c"), f3("a", "c", "b")),
          (f3("a", "c", "b"), f3("b", "a", "c")),
          (f3("b", "a", "c"), f3("b", "c", "a")),
          (f3("c", "a", "b"), f3("c", "b", "a")),

          (f4("a", "b", "c", "d"), f4("a", "b", "d", "c")),
          (f4("a", "b", "d", "c"), f4("a", "c", "b", "d")),
          (f4("a", "d", "c", "b"), f4("b", "a", "c", "d")),
          (f4("b", "a", "c", "d"), f4("b", "a", "d", "c")),
          (f4("b", "d", "c", "a"), f4("c", "a", "b", "d")),
          (f4("c", "a", "b", "d"), f4("c", "a", "d", "b")),
          (f4("c", "d", "b", "a"), f4("d", "a", "b", "c")),

          (f6("a", "c", "e", "b", "f", "d"), f6("a", "c", "e", "d", "b", "f")),

          (f3("b", "c", "a") * f3("c", "b", "a") /* [acb] */ , f3("b", "a", "c")),
          (f3("b", "c", "a").inverse /* [cab] */ , f3("c", "b", "a"))
        )

        forAll(conversions) { (p: PassivePermutation[String], expected: PassivePermutation[String]) =>
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
          (f1("a"), 0),

          (f2("a", "b"), 0),
          (f2("b", "a"), 1),

          (f3("a", "b", "c"), 0),
          (f3("a", "c", "b"), 1),
          (f3("b", "a", "c"), 2),
          (f3("b", "c", "a"), 3),
          (f3("c", "a", "b"), 4),
          (f3("c", "b", "a"), 5),

          (f4("a", "b", "c", "d"), 0),
          (f4("a", "d", "b", "c"), 4),
          (f4("d", "b", "c", "a"), 21)
        )

        forAll(conversions) { (p: PassivePermutation[String], expected: Int) =>
          __Exercise__
          val sut = p.sequenceNumber.toInt
          __Verify__
          sut should equal(expected)
        }
      }
    }
  }

  "The methods of Any class" - {

    "toString method should" - {

      "create a String representation of permutation like [1 3 2]" in {
        val conversions =
          Table(
            ("p", "expected"),
            (perm0, "[d b a c]"),
            (Permutation.passive("aa", "bb", "cc")("bb", "cc", "aa"), "[bb cc aa]")
          )

        forAll(conversions) { (p: PassivePermutation[String], expected: String) =>
          __Exercise__
          val sut = p.toString
          __Verify__
          sut should equal(expected)
        }
      }
    }

    "equals() method should" - {

      "return true if " in {
        val conversions =
          Table(
            ("p0", "p1"),
            (perm0, perm0),
            (perm0, f4("d", "b", "a", "c")),
            (id, f4("a", "b", "c", "d")),
            (prod, f4("d", "c", "a", "b")),
            (inv, f4("c", "b", "d", "a"))
          )

        forAll(conversions) { (p0: PassivePermutation[String], p1: PassivePermutation[String]) =>
          __Verify__
          p0 should equal(p1)
        }
      }
    }

    "hashCode property should" - {

      "return the same value if two permutations are equivalent" in {
        val conversions =
          Table(
            ("p0", "p1"),
            (perm0, perm0),
            (perm0, f4("d", "b", "a", "c")),
            (id, f4("a", "b", "c", "d")),
            (prod, f4("d", "c", "a", "b")),
            (inv, f4("c", "b", "d", "a"))
          )

        forAll(conversions) { (p0: PassivePermutation[String], p1: PassivePermutation[String]) =>
          __Verify__
          p0.hashCode should equal(p1.hashCode)
        }
      }
    }
  }

  "Type conversions" - {

    "toPermutation method should" - {

      "convert a Permutation object to a corresponding Permutation one" - {
        val conversions = Table(
          ("permutation", "expected"),
          (perm0, Permutation(3, 1, 0, 2)),
          (id, Permutation(0, 1, 2, 3)),
          (prod, Permutation(3, 2, 0, 1)),
          (inv, Permutation(2, 1, 3, 0))
        )

        forAll(conversions){ (p: PassivePermutation[String], expected: Permutation) =>
          __Exercise__
          val sut = p.toPermutation
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "toMap() method should" - {

      "convert Permutation object to a Map[Int, Int] one" in {
        val conversions = Table(
          ("p", "expected"),
          (perm0, Map("a" -> "d", "b" -> "b", "c" -> "a", "d" -> "c"))
        )

        forAll(conversions){ (p: PassivePermutation[String], expected: Map[String, String]) =>
          __Exercise__
          val sut = p.toMap
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }

  "PassivePermutationFactory" - {

    "apply(E*) method should" - {

      "throw IllegalArgumentException if the passed elements do not adequate." in {
        __Verify__
        an [IllegalArgumentException] should be thrownBy{
          f4("a", "b", "c")
        }
      }
    }

    "fromPermutation(Permutation) method should" - {

      "convert Permutation object to PassivePermutation one" in {
        __SetUp__
        val p = Permutation(3, 1, 0, 2)
        __Exercise__
        val sut = f4.fromPermutation(p)
        __Verify__
        sut should equal (f4("d", "b", "a", "c"))
      }
    }

    "nthPermutation" - {

      "nthPermutation(Int, Int) method should " - {

        "return the permutation corresponding to the sequence number in lexicographic order" in {
          val conversions = Table(
            ("n", "degree", "expected"),
            (0, f3, f3("a", "b", "c")),
            (1, f3, f3("a", "c", "b")),
            (2, f3, f3("b", "a", "c")),
            (3, f3, f3("b", "c", "a")),
            (4, f3, f3("c", "a", "b")),
            (5, f3, f3("c", "b", "a")),

            (389, f6, f6("d", "b", "a", "f", "e", "c"))
          )

          forAll(conversions) { (n: Int, f: PassivePermutationFactory[String], expected: PassivePermutation[String]) =>
            __Exercise__
            val sut = f.nthPermutation(n)
            __Verify__
            sut should equal(expected)
          }
        }
      }

      "nthPermutation(FactorialRepresentation, Int) method should " - {

        "return the permutation corresponding to the sequence number in lexicographic order" in {
          val conversions = Table(
            ("n", "degree", "expected"),
            (List(0), f3, f3("a", "b", "c")),
            (List(1), f3, f3("a", "c", "b")),
            (List(1, 0), f3, f3("b", "a", "c")),
            (List(1, 1), f3, f3("b", "c", "a")),
            (List(2, 0), f3, f3("c", "a", "b")),
            (List(2, 1), f3, f3("c", "b", "a")),

            (List(3, 1, 0, 2, 1), f6, f6("d", "b", "a", "f", "e", "c"))
          )

          forAll(conversions) { (n: List[Int], f: PassivePermutationFactory[String], expected: PassivePermutation[String]) =>
            __SetUp__
            val fr = FactorialRepresentation(n)
            __Exercise__
            val sut = f.nthPermutation(fr)
            __Verify__
            sut should equal(expected)
          }
        }
      }
    }

    "Permutation generation" - {

      "allPermutations method should" - {

        "generate all permutations of degree 1" in {
          __SetUp__
          val expected = Set(f1("a"))
          __Exercise__
          val sut = f1.allPermutations
          __Verify__
          sut.toSet should equal(expected)
        }

        "generate all permutations of degree 2" in {
          __SetUp__
          val expected = Set(f2("a", "b"), f2("b", "a"))
          __Exercise__
          val sut = f2.allPermutations
          __Verify__
          sut.toSet should equal(expected)
        }

        "generate all permutations of degree 3" in {
          __SetUp__
          val expected = Set(
            f3("a", "b", "c"), f3("a", "c", "b"),
            f3("b", "a", "c"), f3("b", "c", "a"),
            f3("c", "a", "b"), f3("c", "b", "a"))
          __Exercise__
          val sut = f3.allPermutations
          __Verify__
          sut.toSet should equal(expected)
        }

        "generate all permutations of degree 4" in {
          __SetUp__
          val expected = Set(
            f4("a", "b", "c", "d"), f4("a", "b", "d", "c"),
            f4("a", "c", "b", "d"), f4("a", "c", "d", "b"),
            f4("a", "d", "b", "c"), f4("a", "d", "c", "b"),

            f4("b", "a", "c", "d"), f4("b", "a", "d", "c"),
            f4("b", "c", "a", "d"), f4("b", "c", "d", "a"),
            f4("b", "d", "a", "c"), f4("b", "d", "c", "a"),

            f4("c", "a", "b", "d"), f4("c", "a", "d", "b"),
            f4("c", "b", "a", "d"), f4("c", "b", "d", "a"),
            f4("c", "d", "a", "b"), f4("c", "d", "b", "a"),

            f4("d", "a", "b", "c"), f4("d", "a", "c", "b"),
            f4("d", "b", "a", "c"), f4("d", "b", "c", "a"),
            f4("d", "c", "a", "b"), f4("d", "c", "b", "a"))
          __Exercise__
          val sut = f4.allPermutations
          __Verify__
          sut.toSet should equal(expected)
        }

        "Lexicographic Order" - {

          val degreeConversions = Table("degree", 1, 2, 3, 4, 5)

          "allPermutations methods return permutations in lexicographic order" in {
            forAll(degreeConversions) { degree: Int =>
              __SetUp__
              val f = Permutation.passive(alphabet(degree): _*)
              __Exercise__
              val sut = f.allPermutations
              __Verify__
              sut should be(sorted)
            }
          }
        }
      }

      "allPermuted method should" - {

        "generate all permutations of the indices of the factory (degree 4)" in {
          __SetUp__
          val expected = Set(
            Seq("a", "b", "c", "d"), Seq("a", "b", "d", "c"),
            Seq("a", "c", "b", "d"), Seq("a", "c", "d", "b"),
            Seq("a", "d", "b", "c"), Seq("a", "d", "c", "b"),

            Seq("b", "a", "c", "d"), Seq("b", "a", "d", "c"),
            Seq("b", "c", "a", "d"), Seq("b", "c", "d", "a"),
            Seq("b", "d", "a", "c"), Seq("b", "d", "c", "a"),

            Seq("c", "a", "b", "d"), Seq("c", "a", "d", "b"),
            Seq("c", "b", "a", "d"), Seq("c", "b", "d", "a"),
            Seq("c", "d", "a", "b"), Seq("c", "d", "b", "a"),

            Seq("d", "a", "b", "c"), Seq("d", "a", "c", "b"),
            Seq("d", "b", "a", "c"), Seq("d", "b", "c", "a"),
            Seq("d", "c", "a", "b"), Seq("d", "c", "b", "a"))
          __Exercise__
          val sut = f4.allPermuted
          __Verify__
          sut.toSet should equal(expected)
        }
      }

      "allPartialPermuted(Int) method should" - {

        "return all partial permutations of the indices of factory with the specified rank" in {
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
            val sut = f3.allPartialPermuted(rank)
            __Verify__
            sut.toSet should equal(expected)
          }
        }
      }

      "allPermutedStrings method should" - {

        "return all permutations of the indices of the factory as Seq[String]" in {
          __SetUp__
          val expected = Set("abc", "acb", "bac", "bca", "cab", "cba")
          __Exercise__
          val sut = f3.allPermutedStrings
          __Verify__
          sut.toSet should equal(expected)
        }
      }

      "allPartialPermutedStrings(Int) method should" - {

        "return all partial permutations of the indices of the factory with the specified length (rank) as Seq[String]" in {
          val conversions = Table(
            ("length", "expected"),
            (0, Set("")),
            (1, Set("a", "b", "c")),
            (2, Set("ab", "ac", "ba", "bc", "ca", "cb")),
            (3, Set("abc", "acb", "bac", "bca", "cab", "cba"))
          )

          forAll(conversions) { (length: Int, expected: Set[String]) =>
            __Exercise__
            val sut = f3.allPartialPermutedStrings(length)
            __Verify__
            sut.toSet should equal(expected)
          }
        }

        "parity" - {

          "evenPermutations method should" - {

            "return all even permutations" in {
              val conversions = Table(
                ("factory", "expected"),
                (f1, Seq(f1("a"))),
                (f2, Seq(f2("a", "b"))),
                (f3, Seq(f3("a", "b", "c"), f3("b", "c", "a"), f3("c", "a", "b"))),
                (f4, Seq(
                  f4("a", "b", "c", "d"), f4("a", "c", "d", "b"), f4("a", "d", "b", "c"),
                  f4("b", "a", "d", "c"), f4("b", "c", "a", "d"), f4("b", "d", "c", "a"),
                  f4("c", "a", "b", "d"), f4("c", "b", "d", "a"), f4("c", "d", "a", "b"),
                  f4("d", "a", "c", "b"), f4("d", "b", "a", "c"), f4("d", "c", "b", "a")
                ))
              )

              forAll(conversions) { (f: PassivePermutationFactory[String], expected: Seq[PassivePermutation[String]]) =>
                __Exercise__
                val sut = f.evenPermutations
                __Verify__
                sut should equal(expected)
                sut.foreach { p =>
                  p.sign should equal(1)
                }
              }
            }
          }

          "oddPermutations method should" - {

            "return all even permutations" in {
              val conversions = Table(
                ("factory", "expected"),
                (f1, Seq[PassivePermutation[String]]()),
                (f2, Seq(f2("b", "a"))),
                (f3, Seq(f3("a", "c", "b"), f3("b", "a", "c"), f3("c", "b", "a"))),
                (f4, Seq(
                  f4("a", "b", "d", "c"), f4("a", "c", "b", "d"), f4("a", "d", "c", "b"),
                  f4("b", "a", "c", "d"), f4("b", "c", "d", "a"), f4("b", "d", "a", "c"),
                  f4("c", "a", "d", "b"), f4("c", "b", "a", "d"), f4("c", "d", "b", "a"),
                  f4("d", "a", "b", "c"), f4("d", "b", "c", "a"), f4("d", "c", "a", "b")
                ))
              )

              forAll(conversions) { (f: PassivePermutationFactory[String], expected: Seq[PassivePermutation[String]]) =>
                __Exercise__
                val sut = f.oddPermutations
                __Verify__
                sut should equal(expected)
                sut.foreach { p =>
                  p.sign should equal(-1)
                }
              }
            }
          }
        }

        "derangements method should" - {

          "return all derangements" in {
            val conversions = Table(
              ("factory", "expected"),
              (f1, Set[PassivePermutation[String]]()),
              (f2, Set(f2("b", "a"))),
              (f3, Set(f3("b", "c", "a"), f3("c", "a", "b"))),
              (f4, Set(
                f4("b", "a", "d", "c"), f4("b", "c", "d", "a"), f4("b", "d", "a", "c"),
                f4("c", "a", "d", "b"), f4("c", "d", "a", "b"), f4("c", "d", "b", "a"),
                f4("d", "a", "b", "c"), f4("d", "c", "a", "b"), f4("d", "c", "b", "a")
              ))
            )

            forAll(conversions) { (f: PassivePermutationFactory[String], expected: Set[PassivePermutation[String]]) =>
              __Exercise__
              val sut = f.derangementPermutations
              __Verify__
              sut.toSet should equal(expected)
            }
          }
        }
      }
    }
  }
}