package org.waman.conformal.number.integer.mod

import org.waman.conformal.ConformalCustomSpec

class ModulusSpec extends ConformalCustomSpec{

  "apply method should" - {

    "create ModularNumber object" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (7, 0, 0),
        (7, 1, 1),
        (7, 2, 2),
        (7, 3, 3),
        (7, 4, 4),
        (7, 5, 5),
        (7, 6, 6),

        (7, 7, 0),
        (7, 8, 1),
        (7, 9, 2),
        (7, 10, 3),
        (7, 11, 4),
        (7, 12, 5),
        (7, 13, 6),

        (7, 14, 0),
        (7, 15, 1),

        (7, -1, 6),
        (7, -2, 5),
        (7, -3, 4),
        (7, -4, 3),
        (7, -5, 2),
        (7, -6, 1),
        (7, -7, 0),

        (13, 26, 0),
        (13, 22, 9)
      )

      forAll(conversions){ (m: Int, n: Int, expected: Int) =>
        __SetUp__
        val mod = Modulus(m)
        __Exercise__
        val sut = mod(n)
        __Verify__
        sut.valueAs[Int] should equal (expected)
      }
    }
  }

  "Methods of Any" - {

      val m = Modulus(7)

      "equals() method should" - {

        "return true when the arg is the equivalent value even if they are the different objects" in {
          val conversions = Table(
            ("x", "y", "expected"),
            (m, m, true),
            (m, Modulus(7), true),
            (m, Modulus(3), false)
          )

          forAll(conversions){ (x: Modulus, y: Modulus, expected: Boolean) =>
            __Exercise__
            val sut = x == y
            __Verify__
            sut should be (expected)
          }
        }

        "return false when two Modulo objects have the same value of module but integral types are different" in {
          __SetUp__
          val x = Modulus(7)
          val y = Modulus(7L)
          __Exercise__
          val sut = x == y
          __Verify__
          sut should be (false)
        }
      }

      "hashCode method should" - {

        "return the same value when the two objects are the equivalent value even if different objects" in {
          val conversions = Table(
            ("x", "y"),
            (m, m),
            (m, Modulus(7)),
            (Modulus(11), Modulus(11))
          )

          forAll(conversions){ (x: Modulus, y: Modulus) =>
            __Exercise__
            val sut = x.hashCode == y.hashCode
            __Verify__
            sut should be (true)
          }
        }
      }
    }
}
