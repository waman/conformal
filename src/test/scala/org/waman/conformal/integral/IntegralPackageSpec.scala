package org.waman.conformal.integral

import org.waman.conformal.ConformalCustomSpec

class IntegralPackageSpec extends ConformalCustomSpec{

  "gcd(Int, Int) method should" - {

    "return gcd of the specified Int values" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (18, 24, 6),
        (21, 91, 7),
        (51, 13, 1)
      )

      forAll(conversions){ (m: Int, n: Int, expected: Int) =>
        __Exercise__
        val sut = gcd(m, n)
        __Verify__
        sut should equal (expected)
      }
    }

    "return gcd of the specified Long values" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (18L, 24L, 6L),
        (21L, 91L, 7L),
        (51L, 13L, 1L)
      )

      forAll(conversions){ (m: Long, n: Long, expected: Long) =>
        __Exercise__
        val sut = gcd(m, n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  // For implementation interest
  "gcd1(Int, Int) method should" - {

    "return gcd of the specified Int values" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (18, 1, 1),
        (18, 24, 6),
        (21, 91, 7),
        (51, 13, 1)
      )

      forAll(conversions){ (m: Int, n: Int, expected: Int) =>
        __Exercise__
        val sut = gcd1(m, n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "ngcd(Int, Int*) method should" - {

    "accept Int values as vararg" in {
      noException should be thrownBy{
        ngcd(1, 2, 3, 4)
      }
    }

    "return gcd of the specified Int values" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (18, Seq(24, 6), 6),
        (21, Seq(91), 7),
        (51, Seq(13, 26, 101), 1)
      )

      forAll(conversions){ (m: Int, ns: Seq[Int], expected: Int) =>
        __Exercise__
        val sut = ngcd(m, ns:_*)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "lcm(Int, Int) method should" - {

    "return lcm of the specified Int values" in {
      val conversions = Table(
        ("m", "n", "expected"),
        (18, 24, 72),
        (21, 91, 273),
        (51, 13, 663)
      )

      forAll(conversions){ (m: Int, n: Int, expected: Int) =>
        __Exercise__
        val sut = lcm(m, n)
        __Verify__
        sut should equal (expected)
      }
    }
  }
}
