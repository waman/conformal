package org.waman.conformal.integral.combinatorial

import org.waman.conformal.ConformalCustomSpec

class MultisetSpec extends ConformalCustomSpec{

  "Multiset companion object" - {

    "calculateMultiplicity(Seq[E]) method should" - {

      "return a multiplicity map of the specified seq of Int" in {
        val conversions = Table(
          ("seq", "expected"),
          (Seq(0, 0, 0, 1, 1, 2, 2, 3, 4, 4), Map(0 -> 3, 1 -> 2, 2 -> 2, 3 -> 1, 4 -> 2)),
          (Seq(0, 1, 0, 2, 0, 1), Map(0 -> 3, 1 -> 2, 2 -> 1))
        )

        forAll(conversions){ (seq: Seq[Int], expected: Map[Int, Int]) =>
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
            Seq(0, 0, 1), Seq(0, 1, 0), Seq(1, 0, 0)
          )),
          (Seq(0, 0, 1, 1), Seq(
            Seq(0, 0, 1, 1), Seq(0, 1, 0, 1), Seq(0, 1, 1, 0),
            Seq(1, 0, 0, 1), Seq(1, 0, 1, 0), Seq(1, 1, 0, 0)
          ))
        )

        forAll(conversions){ (seq: Seq[Int], expected: Seq[Seq[Int]]) =>
          __Exercise__
          val sut = Multiset.allPermutations(seq)
          __Verify__
          sut.toSet should equal (expected.toSet)
        }
      }
    }
  }
}
