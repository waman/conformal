package org.waman.conformal.number.integral

import org.waman.conformal.ConformalCustomSpec

class PolygonalNumberSpec extends ConformalCustomSpec{

  val triangulars = Seq(0, 1, 3, 6, 10, 15)
  val squares     = Seq(0, 1, 4, 9, 16, 25)
  val pentagonals = Seq(0, 1, 5, 12, 22, 35)
  val hexagonals  = Seq(0, 1, 6, 15, 28, 45)
  val heptagonals = Seq(0, 1, 7, 18, 34, 55)

  val conversions = Table("n", 0, 1, 2, 3, 4, 5)

  "TriangularNumber object" - {

    "apply() method should" - {

      "calculate triangular numbers n*(n+1)/2" in {

        forAll(conversions){ n: Int =>
          __SetUp__
          val expected = triangulars(n)
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
        val sut = TriangularNumber.stream take 6
        __Verify__
        sut should equal (expected)
      }
    }

    "stream1 method should" - {

      "return a stream of triangular numbers without head 0" in {
        __SetUp__
        val expected = triangulars.tail
        __Exercise__
        val sut = TriangularNumber.stream1Of[Int] take 5
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
          val expected = squares(n)
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
        val sut = SquareNumber.stream take 6
        __Verify__
        sut should equal (expected)
      }
    }

    "stream1 method should" - {

      "return a stream of square numbers without head 0" in {
        __SetUp__
        val expected = squares.tail
        __Exercise__
        val sut = SquareNumber.stream1Of[Int] take 5
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
          val expected = pentagonals(n)
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
        val sut = PentagonalNumber.stream take 6
        __Verify__
        sut should equal (expected)
      }
    }

    "stream method should" - {

      "return a stream of pentagonal numbers without head 0" in {
        __SetUp__
        val expected = pentagonals.tail
        __Exercise__
        val sut = PentagonalNumber.stream1Of[Int] take 5
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
          val expected = hexagonals(n)
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
        val sut = HexagonalNumber.stream take 6
        __Verify__
        sut should equal (expected)
      }
    }

    "stream1 method should" - {

      "return a stream of hexagonal numbers with ascending" in {
        __SetUp__
        val expected = hexagonals.tail
        __Exercise__
        val sut = HexagonalNumber.stream1Of[Int] take 5
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
          val expected = heptagonals(n)
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
        val sut = hepta.stream take 6
        __Verify__
        sut should equal (expected)
      }
    }

    "stream1 method should" - {

      "return a stream of heptagonal numbers with ascending" in {
        __SetUp__
        val expected = heptagonals.tail
        __Exercise__
        val sut = hepta.stream1Of[Int] take 5
        __Verify__
        sut should equal (expected)
      }
    }

    "streamFrom(n: I) method should" - {

      "return a stream of heptagonal numbers from the n-th polygonal number (inclusive) with ascending" in {
        __SetUp__
        val expected = heptagonals.tail.tail
        __Exercise__
        val sut = hepta.streamNthFrom[Int](2) take 4
        __Verify__
        sut should equal (expected)
      }
    }
  }
}
