package org.waman.conformal.number.integer.combinatorics

import org.scalatest.OptionValues._
import org.waman.conformal.ConformalCustomSpec
import org.waman.conformal.number.integer.FactorialRepresentation

class PermutationSpec extends ConformalCustomSpec{

  val perm = Permutation(3, 1, 0, 2)
  val id = Permutation.identity(4)
  val prod = perm * Permutation(1, 2, 0, 3)  // [1032]
  val inv = perm.inverse // [2130]
  val permPI = Permutation.byProperIndices(2, 1, 3, 0)

  def range(n: Int): Seq[Int] = 0 until n
  def alphabet(n: Int): String = ('a' to 'z').take(n).mkString

  /**
    * -Permutation
    * -identity
    * -*
    * -inverse
    */
  "apply(Int) method should" - {

    "return the suffix corresponding to the argument index" in {
      val conversions =
        Table(
          ("p", "index", "expected"),
          (perm, 0, 3),
          (perm, 1, 1),
          (perm, 2, 0),
          (perm, 3, 2),
          (id, 0, 0),
          (id, 1, 1),
          (id, 2, 2),
          (id, 3, 3),
          (prod, 0, 1),
          (prod, 1, 0),
          (prod, 2, 3),
          (prod, 3, 2),
          (inv, 0, 2),
          (inv, 1, 1),
          (inv, 2, 3),
          (inv, 3, 0),
          (permPI, 0, 3),
          (permPI, 1, 1),
          (permPI, 2, 0),
          (permPI, 3, 2)
        )

      forAll(conversions) { (p: Permutation, index: Int, expected: Int) =>
        __Exercise__
        val sut = p(index)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "indexOf(Int) method should" - {

    "return the index corresponding to the argument suffix" in {
      val conversions =
        Table(
          ("p", "index", "expected"),
          (perm, 0, 2),
          (perm, 1, 1),
          (perm, 2, 3),
          (perm, 3, 0),
          (id, 0, 0),
          (id, 1, 1),
          (id, 2, 2),
          (id, 3, 3),
          (prod, 0, 1),
          (prod, 1, 0),
          (prod, 2, 3),
          (prod, 3, 2),
          (inv, 0, 3),
          (inv, 1, 1),
          (inv, 2, 0),
          (inv, 3, 2),
          (permPI, 0, 2),
          (permPI, 1, 1),
          (permPI, 2, 3),
          (permPI, 3, 0)
        )

      forAll(conversions) { (p: Permutation, i: Int, expected: Int) =>
        __Exercise__
        val sut = p.indexOf(i)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "apply(Seq[E]) method should" - {

    "execute permutation of Seq" in {
      val conversions =
        Table(
          ("p", "expected"),
          (perm, Seq(2, 1, 3, 0)),
          (Permutation(2, 5, 3, 1, 0, 4), Seq(4, 3, 0, 2, 5, 1)),
          (id, Seq(0, 1, 2, 3)),
          (prod, Seq(1, 0, 3, 2)),
          (inv, Seq(3, 1, 0, 2)),
          (permPI, Seq(2, 1, 3, 0))
        )

      forAll(conversions) { (p: Permutation, expected: Seq[Int]) =>
        __SetUp__
        val args = range(p.degree)
        __Exercise__
        val sut = p(args)
        __Verify__
        sut should equal(expected)
      }
    }
  }

  "* operator should" - {

    "compose two permutations" in {
      val conversions =
        Table(
          ("p0", "p1", "arg"),
          (Permutation.identity(3), Permutation(1, 2, 0), Permutation(1, 2, 0)),
          (Permutation(0, 2, 1), Permutation(1, 2, 0), Permutation(2, 1, 0)),
          (Permutation(0, 3, 2, 1), Permutation(3, 1, 2, 0), Permutation(1, 3, 2, 0))
        )

      forAll(conversions) { (p0: Permutation, p1: Permutation, expected: Permutation) =>
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
          (Permutation.identity(3), Permutation(1, 2, 0)),
          (Permutation(0, 2, 1, 3), Permutation(1, 3, 2, 0)),  // [2310]
          (Permutation(0, 3, 2, 1), Permutation(3, 1, 2, 0))
        )

      forAll(conversions) { (p0: Permutation, p1: Permutation) =>
        __SetUp__
        val arg = range(p0.degree)
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
          (perm, Permutation(2, 1, 3, 0)),
          (Permutation(0, 2, 1), Permutation(0, 2, 1)),
          (id, Permutation(0, 1, 2, 3)),
          (prod, Permutation(1, 0, 3, 2)),  // [1032]^{-1} = [1032]
          (inv, Permutation(3, 1, 0, 2)),
          (permPI, Permutation(2, 1, 3, 0))
        )

      forAll(conversions) { (p: Permutation, expected: Permutation) =>
        __Exercise__
        val sut = p.inverse
        __Verify__
        sut should equal(expected)
      }
    }

    "return the inverse permutation which maps the argument Seq inversely" in {
      val conversions = Table(
        "p",
        Permutation(0, 2, 1),
        perm,
        id,
        prod,
        inv,
        permPI)

      forAll(conversions) { p: Permutation =>
        __SetUp__
        val expected = range(p.degree)
        val arg = p(expected)
        __Exercise__
        val sut = p.inverse(arg)
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
        val sut = p.sign
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
      sut should be (true)
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

          (prod /* [1032] */, Permutation(1, 2, 0, 3)),
          (inv /* [2130] */ , Permutation(2, 3, 0, 1)),
          (permPI /* [3102] */, Permutation(3, 1, 2, 0))
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

      "return the sequence number in lexicographic order as factorial representation" in {
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
          val sut = p.sequenceNumber.toInt
          __Verify__
          sut should equal(expected)
        }
      }
    }
  }

  "Cycles" - {

    "cycles method should" - {

      "return cycles of this permutation" in {
        val conversions = Table(
          ("p", "expected"),
          (Permutation(1, 0, 3, 2), Set(Cycle(0, 1), Cycle(2, 3)))
        )

        forAll(conversions){ (p: Permutation, expected: Set[Cycle]) =>
          __Exercise__
          val sut = p.cycles.toSet
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "toCycleNotation method should" - {

      "return String representation of this permutation in cycle notation" in {

        val conversions = Table(
          ("p", "expected"),
          (Permutation(1, 0, 3, 2), "(0 1)(2 3)"),
          (Permutation(3, 1, 0, 2), "(0 3 2)"),
          (Permutation.identity(4), "")
        )

        forAll(conversions){ (p: Permutation, expected: String) =>
          __Exercise__
          val sut = p.toCycleNotation
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }

  "Type conversions" - {

    "toMap() method should" - {

      "convert Permutation object to a Map[Int, Int] one" in {
        val conversions = Table(
          ("permutation", "expected"),
          (perm, Map(0 -> 3, 1 -> 1, 2 -> 0, 3 -> 2))
        )

        forAll(conversions){ (p: Permutation, expected: Map[Int, Int]) =>
          __Exercise__
          val sut = p.toMap
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }

  "The methods of Any class" - {

    "toString method should" - {

      "create a String representation of permutation like [0 2 1]" in {
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

      "return true when the two permutation are equivalent even if not the same objects" in {
        val conversions =
          Table(
            ("p0", "p1"),
            (perm, perm),
            (perm, Permutation(3, 1, 0, 2)),
            (id, Permutation(0, 1, 2, 3)),
            (prod, Permutation(1, 0, 3, 2)),
            (inv, Permutation(2, 1, 3, 0)),
            (permPI, perm)
          )

        forAll(conversions) { (p0: Permutation, p1: Permutation) =>
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
            (perm, perm),
            (perm, Permutation(3, 1, 0, 2)),
            (id, Permutation(0, 1, 2, 3)),
            (prod, Permutation(1, 0, 3, 2)),
            (inv, Permutation(2, 1, 3, 0)),
            (permPI, perm)
          )

        forAll(conversions) { (p0: Permutation, p1: Permutation) =>
          __Verify__
          p0.hashCode should equal(p1.hashCode)
        }
      }
    }
  }

  "Permutation Companion object" - {

    "permutationCount() method" - {

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
            val sut = Permutation.permutationCount(n)
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "apply() factory methods" - {

      "apply((Int, Int)*) method should" - {

        "create Permutation object" in {
          __SetUp__
          val expected = perm
          __Exercise__
          val sut = Permutation(2 -> 0, 1 -> 1, 3 -> 2, 0 -> 3)  // [3102]
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "nthPermutation" - {

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
            sut should equal(expected)
          }
        }
      }

      "nthPermutation(FactorialRepresentation, Int) method should " - {

        "return the permutation corresponding to the sequence number in lexicographic order" in {
          val conversions = Table(
            ("n", "degree", "expected"),
            (List(0), 3, Permutation(0, 1, 2)),
            (List(1), 3, Permutation(0, 2, 1)),
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
            sut should equal(expected)
          }
        }
      }
    }

    "Permutation generation" - {

      "allPermutations(Int) method should" - {

        "generate all permutations of degree 0" in {
          val conversions = Table(
            "permutations",
            Permutation.generatePermutations(0)//,
//            Permutation.generatePermutationsWithSign(0)
          )

          __SetUp__
          val expected = Set(Permutation(List[Int]()))

          forAll(conversions) { ps: Seq[Permutation] =>
            __Verify__
            ps.toSet should equal(expected)
          }
        }

        "generate all permutations of degree 1" in {
          val conversions = Table(
            "permutations",
            Permutation.generatePermutations(1),
            Permutation.generatePermutationsWithSign(1)
          )

          __SetUp__
          val expected = Set(Permutation(0))

          forAll(conversions) { ps: Seq[Permutation] =>
            __Verify__
            ps.toSet should equal(expected)
          }
        }

        "generate all permutations of degree 2" in {
          val conversions = Table(
            "permutations",
            Permutation.generatePermutations(2),
            Permutation.generatePermutationsWithSign(2)
          )

          val expected = Set(Permutation(0, 1), Permutation(1, 0))

          forAll(conversions) { ps: Seq[Permutation] =>
            __Verify__
            ps.toSet should equal(expected)
          }
        }

        "generate all permutations of degree 3" in {
          val conversions = Table(
            "permutations",
            Permutation.generatePermutations(3),
            Permutation.generatePermutationsWithSign(3)
          )

          val expected = Set(
            Permutation(0, 1, 2), Permutation(0, 2, 1),
            Permutation(1, 0, 2), Permutation(1, 2, 0),
            Permutation(2, 0, 1), Permutation(2, 1, 0))

          forAll(conversions) { ps: Seq[Permutation] =>
            __Verify__
            ps.toSet should equal(expected)
          }
        }

        "generate all permutations of degree 4" in {
          val conversions = Table(
            "permutations",
            Permutation.generatePermutations(4),
            Permutation.generatePermutationsWithSign(4)
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

          forAll(conversions) { ps: Seq[Permutation] =>
            __Verify__
            ps.toSet should equal(expected)
          }
        }

        "allPermutations(Int) like methods return permutations in lexicographic order" in {

          val allPermutationsConversions = Table(
            "allPermutations",
            Permutation.allPermutations(_:Int, calculateSign = true),
            Permutation.allPermutations(_:Int, calculateSign = false),
            Permutation.generatePermutations(_:Int),  // package private method
            Permutation.generatePermutationsWithSign(_:Int)  // package private method
          )

          val degreeConversions = Table("degree", 1, 2, 3, 4, 5)

          forAll(allPermutationsConversions){ gen: (Int => Seq[Permutation]) =>
            forAll(degreeConversions) { degree: Int =>
              __Exercise__
              val sut = gen(degree)
              __Verify__
              sut should be (sorted)
            }
          }
        }
      }

      "allPermutationsWithSign(Int) methods should" - {

        "return permutations with correct sign" in {
          val conversions = Table("degree", 1, 2, 3, 4, 5)

          forAll(conversions){ degree: Int =>
            Permutation.generatePermutationsWithSign(degree).foreach{ p =>
              __SetUp__
              val expected = Permutation(p.suffices:_*).sign
              __Verify__
              p.sign should equal (expected)
            }
          }
        }
      }

      // For implementation interest
      "allPermutations2(Int) method" in {
        val conversions = Table(
          ("degree", "expected"),
          (0, Seq(Nil)),
          (1, Seq(Seq(0))),
          (2, Seq(Seq(0, 1), Seq(1, 0))),
          (3, Seq(
            Seq(0, 1, 2), Seq(0, 2, 1), Seq(2, 0, 1),
            Seq(1, 0, 2), Seq(1, 2, 0), Seq(2, 1, 0))),
          (4, Seq(
            Seq(0, 1, 2, 3), Seq(0, 1, 3, 2), Seq(0, 3, 1, 2), Seq(3, 0, 1, 2),
            Seq(0, 2, 1, 3), Seq(0, 2, 3, 1), Seq(0, 3, 2, 1), Seq(3, 0, 2, 1),
            Seq(2, 0, 1, 3), Seq(2, 0, 3, 1), Seq(2, 3, 0, 1), Seq(3, 2, 0, 1),
            Seq(1, 0, 2, 3), Seq(1, 0, 3, 2), Seq(1, 3, 0, 2), Seq(3, 1, 0, 2),
            Seq(1, 2, 0, 3), Seq(1, 2, 3, 0), Seq(1, 3, 2, 0), Seq(3, 1, 2, 0),
            Seq(2, 1, 0, 3), Seq(2, 1, 3, 0), Seq(2, 3, 1, 0), Seq(3, 2, 1, 0)))
        )

        forAll(conversions){ (degree: Int, expected: Seq[Seq[Int]]) =>
          __Exercise__
          val sut = Permutation.allPermutations2(degree)
          __Verify__
          sut should equal (expected)
        }
      }

      // For implementation interest
      "allPermutations3(Seq[E]) method" in {
        val conversions = Table(
          ("degree", "expected"),
          (0, Seq(Nil)),
          (1, Seq(Seq(0))),
          (2, Seq(Seq(0, 1), Seq(1, 0))),
          (3, Seq(
            Seq(0, 1, 2), Seq(1, 0, 2),
            Seq(0, 2, 1), Seq(2, 0, 1),
            Seq(2, 1, 0), Seq(1, 2, 0))),
          (4, Seq(
            Seq(0, 1, 2, 3), Seq(1, 0, 2, 3), Seq(0, 2, 1, 3), Seq(2, 0, 1, 3), Seq(2, 1, 0, 3), Seq(1, 2, 0, 3),
            Seq(0, 1, 3, 2), Seq(1, 0, 3, 2), Seq(0, 3, 1, 2), Seq(3, 0, 1, 2), Seq(3, 1, 0, 2), Seq(1, 3, 0, 2),
            Seq(0, 3, 2, 1), Seq(3, 0, 2, 1), Seq(0, 2, 3, 1), Seq(2, 0, 3, 1), Seq(2, 3, 0, 1), Seq(3, 2, 0, 1),
            Seq(3, 1, 2, 0), Seq(1, 3, 2, 0), Seq(3, 2, 1, 0), Seq(2, 3, 1, 0), Seq(2, 1, 3, 0), Seq(1, 2, 3, 0)))
        )

        forAll(conversions){ (degree: Int, expected: Seq[Seq[Int]]) =>
          __Exercise__
          val sut = Permutation.allPermutations3(0 until degree)
          __Verify__
          sut should equal (expected)
        }
      }

      // For implementation interest
      "allPermutations4(Seq[E])" - {

        "factorialCounters(Int)" in {
          val conversions = Table(
            ("degree", "expected"),
            (0, Seq(Nil)),
            (1, Seq(Seq(0))),
            (2, Seq(Seq(0, 1), Seq(0, 0))),
            (3, Seq(
              Seq(0, 1, 2),
              Seq(0, 0, 2),
              Seq(0, 1, 1),
              Seq(0, 0, 1),
              Seq(0, 1, 0),
              Seq(0, 0, 0)
            ))
          )

          forAll(conversions){ (degree: Int, expected: Seq[Seq[Int]]) =>
            __Exercise__
            val sut = Permutation.factorialCounters(degree).map(_.c)
            __Verify__
            sut should equal (expected)
          }
        }

        "allPermutations4(Seq[E])" in {
          val conversions = Table(
            ("degree", "expected"),
            (0, Seq(Nil)),
            (1, Seq(Seq(0))),
            (2, Seq(Seq(0, 1), Seq(1, 0))),
            (3, Seq(
              Seq(0, 1, 2),
              Seq(1, 0, 2),
              Seq(2, 0, 1),
              Seq(0, 2, 1),
              Seq(1, 2, 0),
              Seq(2, 1, 0))),
            (4, Seq(
              Seq(0, 1, 2, 3), Seq(1, 0, 2, 3), Seq(2, 0, 1, 3), Seq(0, 2, 1, 3), Seq(1, 2, 0, 3), Seq(2, 1, 0, 3),
              Seq(2, 1, 3, 0), Seq(1, 2, 3, 0), Seq(3, 2, 1, 0), Seq(2, 3, 1, 0), Seq(1, 3, 2, 0), Seq(3, 1, 2, 0),
              Seq(3, 0, 2, 1), Seq(0, 3, 2, 1), Seq(2, 3, 0, 1), Seq(3, 2, 0, 1), Seq(0, 2, 3, 1), Seq(2, 0, 3, 1),
              Seq(1, 0, 3, 2), Seq(0, 1, 3, 2), Seq(3, 1, 0, 2), Seq(1, 3, 0, 2), Seq(0, 3, 1, 2), Seq(3, 0, 1, 2)))
          )

          forAll(conversions){ (degree: Int, expected: Seq[Seq[Int]]) =>
            __Exercise__
            val sut = Permutation.allPermutations4(0 until degree)
            __Verify__
            sut should equal (expected)
          }
        }
      }

      "parity" - {

        "evenPermutations(Int) method should" - {

          "return all even permutations" in {
            val conversions = Table(
              ("degree", "expected"),
              (1, Seq(Permutation(0))),
              (2, Seq(Permutation(0, 1))),
              (3, Seq(Permutation(0, 1, 2), Permutation(1, 2, 0), Permutation(2, 0, 1))),
              (4, Seq(
                Permutation(0, 1, 2, 3), Permutation(0, 2, 3, 1), Permutation(0, 3, 1, 2),
                Permutation(1, 0, 3, 2), Permutation(1, 2, 0, 3), Permutation(1, 3, 2, 0),
                Permutation(2, 0, 1, 3), Permutation(2, 1, 3, 0), Permutation(2, 3, 0, 1),
                Permutation(3, 0, 2, 1), Permutation(3, 1, 0, 2), Permutation(3, 2, 1, 0)
              ))
            )

            forAll(conversions){ (degree: Int, expected: Seq[Permutation]) =>
              __Exercise__
              val sut = Permutation.evenPermutations(degree)
              __Verify__
              sut should equal (expected)
            }
          }

          "return permutations whose sign is 1" in {
            val conversions = Table("degree", 1, 2, 3, 4, 5)

            forAll(conversions){ degree: Int =>
              __Exercise__
              val suts = Permutation.evenPermutations(degree)
              __Verify__
              suts.foreach{ sut =>
                sut.sign should equal (1)
              }
            }
          }
        }

        "evenPermutations(String) method should" - {

          "return all even permutations of string characters" in {
            val conversions = Table(
              ("degree", "expected"),
              (1, Set("a")),
              (2, Set("ab")),
              (3, Set("abc", "bca", "cab")),
              (4, Set(
                "abcd", "acdb", "adbc",
                "badc", "bcad", "bdca",
                "cabd", "cbda", "cdab",
                "dacb", "dbac", "dcba"))
            )

            forAll(conversions){ (degree: Int, expected: Set[String]) =>
              __Exercise__
              val sut = Permutation.evenPermutations(alphabet(degree)).toSet
              __Verify__
              sut should equal (expected)
            }
          }
        }

        "oddPermutations(Int) method should" - {

          "return all even permutations" in {
            val conversions = Table(
              ("degree", "expected"),
              (1, Seq()),
              (2, Seq(Permutation(1, 0))),
              (3, Seq(Permutation(0, 2, 1), Permutation(1, 0, 2), Permutation(2, 1, 0))),
              (4, Seq(
                Permutation(0, 1, 3, 2), Permutation(0, 2, 1, 3), Permutation(0, 3, 2, 1),
                Permutation(1, 0, 2, 3), Permutation(1, 2, 3, 0), Permutation(1, 3, 0, 2),
                Permutation(2, 0, 3, 1), Permutation(2, 1, 0, 3), Permutation(2, 3, 1, 0),
                Permutation(3, 0, 1, 2), Permutation(3, 1, 2, 0), Permutation(3, 2, 0, 1)
              ))
            )

            forAll(conversions){ (degree: Int, expected: Seq[Permutation]) =>
              __Exercise__
              val sut = Permutation.oddPermutations(degree)
              __Verify__
              sut should equal (expected)
            }
          }

          "return permutations whose sign is -1" in {
            val conversions = Table("degree", 1, 2, 3, 4, 5)

            forAll(conversions){ degree: Int =>
              __Exercise__
              val suts = Permutation.oddPermutations(degree)
              __Verify__
              suts.foreach{ sut =>
                sut.sign should equal (-1)
              }
            }
          }
        }

        "oddPermutations(String) method should" - {

          "return all odd permutations of string characters" in {
            val conversions = Table(
              ("degree", "expected"),
              (1, Set[String]()),
              (2, Set("ba")),
              (3, Set("acb", "bac", "cba")),
              (4, Set(
                "abdc", "acbd", "adcb",
                "bacd", "bcda", "bdac",
                "cadb", "cbad", "cdba",
                "dabc", "dbca", "dcab"))
            )

            forAll(conversions){ (degree: Int, expected: Set[String]) =>
              __Exercise__
              val sut = Permutation.oddPermutations(alphabet(degree)).toSet
              __Verify__
              sut should equal (expected)
            }
          }
        }
      }

      "derangements" - {

        "derangementCount(Int) method should" - {

          "return the number of derangements with the specified degree" in {
            val conversions = Table(
              ("degree", "expected"),
              (1, 0),
              (2, 1),
              (3, 2),
              (4, 9),
              (5, 44)
            )

            forAll(conversions){ (degree: Int, expected: Int) =>
              __Exercise__
              val sut = Permutation.derangementCount(degree)
              __Verify__
              sut should equal (expected)
            }

//            println(Permutation.derangementCount[BigInt](20))
          }
        }

        "derangements(Int) method should" - {

          "return all derangements" in {
            val conversions = Table(
              ("degree", "expected"),
              (1, Set[Permutation]()),
              (2, Set(Permutation(1, 0))),
              (3, Set(Permutation(1, 2, 0), Permutation(2, 0, 1))),
              (4, Set(
                Permutation(1, 0, 3, 2), Permutation(1, 2, 3, 0), Permutation(1, 3, 0, 2),
                Permutation(2, 0, 3, 1), Permutation(2, 3, 0, 1), Permutation(2, 3, 1, 0),
                Permutation(3, 0, 1, 2), Permutation(3, 2, 0, 1), Permutation(3, 2, 1, 0)
              ))
            )

            forAll(conversions){ (degree: Int, expected: Set[Permutation]) =>
              __Exercise__
              val sut = Permutation.derangements(degree)
              __Verify__
              sut.toSet should equal (expected)
            }
          }
        }

        "derangements(String) method should" - {

          "return all derangement permutations of string characters" in {
            val conversions = Table(
              ("degree", "expected"),
              (1, Set[String]()),
              (2, Set("ba")),
              (3, Set("bca", "cab")),
              (4, Set(
                "badc", "bcda", "bdac",
                "cadb", "cdab", "cdba",
                "dabc", "dcab", "dcba"))
            )

            forAll(conversions){ (degree: Int, expected: Set[String]) =>
              __Exercise__
              val sut = Permutation.derangements(alphabet(degree)).toSet
              __Verify__
              sut should equal (expected)
            }
          }
        }
      }
    }
  }
}
