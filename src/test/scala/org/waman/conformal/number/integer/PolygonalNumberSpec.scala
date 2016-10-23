package org.waman.conformal.number.integer

import org.waman.conformal.ConformalCustomSpec

class PolygonalNumberSpec extends ConformalCustomSpec{

  val triangulars = Seq(1, 3, 6, 10, 15)
  val squares     = Seq(1, 4, 9, 16, 25)
  val pentagonals = Seq(1, 5, 12, 22, 35)
  val hexagonals  = Seq(1, 6, 15, 28, 45)
  val heptagonals = Seq(1, 7, 18, 34, 55)

  val conversions = Table("n", 1, 2, 3, 4, 5)

  "TriangularNumber object" - {

    "apply() method should" - {

      "calculate triangular numbers n*(n+1)/2" in {

        forAll(conversions){ n: Int =>
          __SetUp__
          val expected = triangulars(n-1)
          __Exercise__
          val sut = TriangularNumber(n)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "stream method should" - {

      "return a stream of triangular numbers with ascending" in {
        __SetUp__
        val expected = triangulars
        __Exercise__
        val sut = TriangularNumber.stream[Int] take 5
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "SquareNumber object" - {

    "apply() method should" - {

      "calculate square numbers n**2" in {

        forAll(conversions){ n: Int =>
          __SetUp__
          val expected = squares(n-1)
          __Exercise__
          val sut = SquareNumber(n)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "stream method should" - {

      "return a stream of square numbers with ascending" in {
        __SetUp__
        val expected = squares
        __Exercise__
        val sut = SquareNumber.stream[Int] take 5
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "PentagonalNumber object" - {

    "apply() method should" - {

      "calculate pentagonal numbers n(3n-1)/2" in {

        forAll(conversions){ n: Int =>
          __SetUp__
          val expected = pentagonals(n-1)
          __Exercise__
          val sut = PentagonalNumber(n)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "stream method should" - {

      "return a stream of pentagonal numbers with ascending" in {
        __SetUp__
        val expected = pentagonals
        __Exercise__
        val sut = PentagonalNumber.stream[Int] take 5
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "HexagonalNumber object" - {

    "apply() method should" - {

      "calculate hexagonal numbers n(2n-1)" in {

        forAll(conversions){ n: Int =>
          __SetUp__
          val expected = hexagonals(n-1)
          __Exercise__
          val sut = HexagonalNumber(n)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "stream method should" - {

      "return a stream of hexagonal numbers with ascending" in {
        __SetUp__
        val expected = hexagonals
        __Exercise__
        val sut = HexagonalNumber.stream[Int] take 5
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "PolygonalNumber object" - {

    val hepta = PolygonalNumber(7)

    "apply() method should" - {

      "calculate heptagonal numbers n(5n-3)/2" in {

        forAll(conversions){ n: Int =>
          __SetUp__
          val expected = heptagonals(n-1)
          __Exercise__
          val sut = hepta(n)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "stream method should" - {

      "return a stream of heptagonal numbers with ascending" in {
        __SetUp__
        val expected = heptagonals
        __Exercise__
        val sut = hepta.stream[Int] take 5
        __Verify__
        sut should equal (expected)
      }
    }
  }
}