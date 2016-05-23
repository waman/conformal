package org.waman.conformal.integral

import org.waman.conformal.ConformalCustomSpec

class FactorialRepresentationSpec extends ConformalCustomSpec{

  "The constructor should" - {

    "work well when an argument is one Int value" in {
      __Exercise__
      val sut0 = new FactorialRepresentation(0)
      val sut1 = new FactorialRepresentation(1)
      __Verify__
      sut0 should equal (new FactorialRepresentation(Seq(0)))
      sut1 should equal (new FactorialRepresentation(Seq(1)))

      an [IllegalArgumentException] should be thrownBy{
        new FactorialRepresentation(2)
      }
    }

    "throw Exception if some coefficient is out of the range" in {
      __Verify__
      an [IllegalArgumentException] should be thrownBy{
        new FactorialRepresentation(3, 1)
      }
    }
  }

  "coefficient(Int) method should" - {

    "return the coefficient of n! term" in {
      val conversions =
        Table(
          ("n", "expected"),
          (6, 3),
          (4, 2)
        )

      __SetUp__
      val fr = new FactorialRepresentation(3, 3, 2, 1, 0, 1)

      forAll(conversions){ (n: Int, expected: Int) =>
        __Exercise__
        val sut = fr.coefficient(n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "toInt method should" - {

    "convert a factorial representation value to an Int value" in {
      val conversions =
        Table(
          ("coefficients", "expected"),
          (Seq(), 0),
          (Seq(0), 0),
          (Seq(1), 1),
          (Seq(1, 2, 0, 1), 37),
          (Seq(3, 3, 2, 1, 0, 1), 2575)
        )

      forAll(conversions){ (cs: Seq[Int], expected: Int) =>
        __SetUp__
        val value = new FactorialRepresentation(cs)
        __Exercise__
        val sut = value.toInt
        __Verify__
        sut should equal (expected)
      }
    }
  }


  "Companion object" - {

    "fromInt() method should" - {

      "convert an Int value to a equivalent factorial representation value" in {
        val conversions =
          Table(
            ("i", "expected"),
            (2575, new FactorialRepresentation(3, 3, 2, 1, 0, 1))
          )

        forAll(conversions){ (i: Int, expected: FactorialRepresentation) =>
          __Exercise__
          val sut = FactorialRepresentation.fromInt(i)
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }
}
