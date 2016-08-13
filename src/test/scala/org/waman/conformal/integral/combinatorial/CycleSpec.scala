package org.waman.conformal.integral.combinatorial

import org.waman.conformal.ConformalCustomSpec

class CycleSpec extends ConformalCustomSpec{

  val cycle = Cycle(0, 1, 3)

  "nextTo(Int) method should" - {

    "return the next Int value of the argument" in {
      val conversions = Table(
        ("arg", "expected"),
        (0, 1),
        (1, 3),
        (3, 0)
      )

      forAll(conversions){ (arg: Int, expected: Int) =>
        __Exercise__
        val sut = cycle.nextTo(arg)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "previousTo(Int) method should" - {

    "return the previous Int value of the argument" in {
      val conversions = Table(
        ("arg", "expected"),
        (0, 3),
        (1, 0),
        (3, 1)
      )

      forAll(conversions){ (arg: Int, expected: Int) =>
        __Exercise__
        val sut = cycle.previousTo(arg)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "The methods of Any class" - {

    "toString method should" - {

      "create a String representation of cycle like (1 3 2)" in {
        val conversions =
          Table(
            ("c", "expected"),
            (cycle, "(0 1 3)"),
            (Cycle(0, 1, 5, 3), "(0 1 5 3)")
          )

        forAll(conversions) { (c: Cycle, expected: String) =>
          __Exercise__
          val sut = c.toString
          __Verify__
          sut should equal(expected)
        }
      }
    }

    "equals() method should" - {

      "return true when the two cycles are equivalent even if not the same objects" in {
        val conversions =
          Table(
            ("c0", "c1"),
            (cycle, cycle),
            (cycle, Cycle(0, 1, 3)),
            (cycle, Cycle(1, 3, 0)),
            (cycle, Cycle(3, 0, 1))
          )

        forAll(conversions) { (c0: Cycle, c1: Cycle) =>
          __Verify__
          c0 should equal(c1)
        }
      }
    }

    "hashCode property should" - {

      "return the same value if two cycles are equivalent" in {
        val conversions =
          Table(
            ("c0", "c1"),
            (cycle, cycle),
            (cycle, Cycle(0, 1, 3)),
            (cycle, Cycle(1, 3, 0)),
            (cycle, Cycle(3, 0, 1))
          )

        forAll(conversions) { (c0: Cycle, c1: Cycle) =>
          __Verify__
          c0.hashCode should equal(c1.hashCode)
        }
      }
    }
  }
}
