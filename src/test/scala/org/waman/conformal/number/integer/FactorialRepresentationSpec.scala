package org.waman.conformal.number.integer

import org.waman.conformal.ConformalCustomSpec

class FactorialRepresentationSpec extends ConformalCustomSpec{

  "Coefficient accessors" - {

    "coefficient(Int) method should" - {

      "return the coefficient of n! term" in {
        val conversions =
          Table(
            ("n", "expected"),
            (6, 3),
            (4, 2)
          )

        __SetUp__
        val fr = FactorialRepresentation(3, 3, 2, 1, 0, 1)

        forAll(conversions){ (n: Int, expected: Int) =>
          __Exercise__
          val sut = fr.coefficient(n)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "coefficientsInDescendant method should" - {

      "return the coefficient in descendant order" in {
        __SetUp__
        val fr = FactorialRepresentation(1, 3, 0, 1)
        __Exercise__
        val sut = fr.coefficientsInDescendant
        __Verify__
        sut should contain theSameElementsInOrderAs Seq(1, 3, 0, 1)
      }
    }

    "coefficientsInAscendant method should" - {

      "return the coefficient in ascendant order" in {
        __SetUp__
        val fr = FactorialRepresentation(1, 3, 0, 1)
        __Exercise__
        val sut = fr.coefficientsInAscendant
        __Verify__
        sut should contain theSameElementsInOrderAs Seq(1, 0, 3, 1)
      }
    }

    "coefficientsAsNthOrderInDescendant(Int) method should" - {

      "return the coefficient in descendant order with pad 0" in {
        __SetUp__
        val fr = FactorialRepresentation(1, 3, 0, 1)
        __Exercise__
        val sut = fr.coefficientsAsNthOrderInDescendant(6)
        __Verify__
        sut should contain theSameElementsInOrderAs Seq(0, 0, 1, 3, 0, 1)
      }
    }

    "coefficientsAsNthOrderInAscendant(Int) method should" - {

      "return the coefficient in ascendant order with pad 0" in {
        __SetUp__
        val fr = FactorialRepresentation(1, 3, 0, 1)
        __Exercise__
        val sut = fr.coefficientsAsNthOrderInAscendant(6)
        __Verify__
        sut should contain theSameElementsInOrderAs Seq(1, 0, 3, 1, 0, 0)
      }
    }
  }

  "toInt method should" - {

    "convert a factorial representation value to an Int value" in {
      val conversions =
        Table(
          ("coefficients", "expected"),
          (List(), 0),
          (List(0), 0),
          (List(1), 1),
          (List(1, 2, 0, 1), 37),
          (List(3, 3, 2, 1, 0, 1), 2575)
        )

      forAll(conversions){ (cs: List[Int], expected: Int) =>
        __SetUp__
        val value = FactorialRepresentation(cs)
        __Exercise__
        val sut = value.toInt
        __Verify__
        sut should equal (expected)
      }
    }

  }

  "Methods of Any" - {

    "equals() method should" - {

      "return true when the arg is the equivalent value even if they are the different objects" in {
        __SetUp__
        val fr0 = FactorialRepresentation(3, 1, 2, 0, 0)
        val fr1 = FactorialRepresentation(3, 1, 2, 0, 0)
        assume( !fr0.eq(fr1) )
        __Exercise__
        val sut = fr0 == fr1
        __Verify__
        sut should be (true)
      }
    }

    "hashCode method should" - {

      "return the same value when the two objects are the equivalent value even if different objects" in {
        __SetUp__
        val fr0 = FactorialRepresentation(3, 1, 2, 0, 0)
        val fr1 = FactorialRepresentation(3, 1, 2, 0, 0)
        assume( fr0 == fr1 )
        __Exercise__
        val sut = fr0.hashCode == fr1.hashCode
        __Verify__
        sut should be (true)
      }
    }

    "toString method should" - {

      "return a string representation of factorial representation" in {
        val conversions =
          Table(
            ("coefficients", "expected"),
            (Nil, "0!*0"),
            (List(3, 1, 2, 0, 0), "5!*3 + 4!*1 + 3!*2 + 2!*0 + 1!*0")
          )

        forAll(conversions){ (coefficients: List[Int], expected: String) =>
          __SetUp__
          val fr = FactorialRepresentation(coefficients)
          __Exercise__
          val sut = fr.toString
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }


  "Companion object" - {

    "apply() factory method should" - {

      "work well when an argument is one Int value" in {
        __Exercise__
        val sut  = FactorialRepresentation()
        val sut0 = FactorialRepresentation(0)
        val sut1 = FactorialRepresentation(1)
        __Verify__
        sut  should equal (FactorialRepresentation(Nil))
        sut0 should equal (FactorialRepresentation(List(0)))
        sut1 should equal (FactorialRepresentation(List(1)))

        an [IllegalArgumentException] should be thrownBy{
          FactorialRepresentation(2)
        }
      }

      "throw Exception if some coefficient is out of the range" in {
        __Verify__
        an [IllegalArgumentException] should be thrownBy{
          FactorialRepresentation(3, 1)
        }
      }

      "drop head zeros" in {
        val conversions =
          Table(
            ("coefficients", "expected"),
            (List(0, 0, 0), Nil),
            (List(0, 0, 1, 1), List(1, 1))
          )

        forAll(conversions){ (coefficients: List[Int], expected: List[Int]) =>
          __Exercise__
          val sut = FactorialRepresentation(coefficients)
          __Verify__
          sut should equal (FactorialRepresentation(expected))
        }
      }
    }

    "fromInt() method should" - {

      "convert an Int value to a equivalent factorial representation value" in {
        val conversions =
          Table(
            ("i", "expected"),
            (2575, FactorialRepresentation(3, 3, 2, 1, 0, 1))
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
