package org.waman.conformal.computerscience

import org.waman.conformal.ConformalCustomSpec

class ISBNSpec extends ConformalCustomSpec{

  "ISBN" - {

    "toIntSeq() method should" - {

      "convert the isbn String to Seq[Int]" in {
        val conversions =
          Table(
            ("isbn", "seq"),
            ("4-7741-1729-3", Seq(4, 7, 7, 4, 1, 1, 7, 2, 9, 3)),
            ("4-87408-852-X", Seq(4, 8, 7, 4, 0, 8, 8, 5, 2, 10)),
            ("978-4-7741-6366-6", Seq(9, 7, 8, 4, 7, 7, 4, 1, 6, 3, 6, 6, 6))
          )

        forAll(conversions){ (isbn: String, seq: Seq[Int]) =>
          __Exercise__
          val sut = ISBN.toIntSeq(isbn)
          __Verify__
          sut should contain theSameElementsInOrderAs seq
        }
      }
    }
  }

  "ISBN-10" - {

    "weight method should be the same Sequence as Seq(10, 9, 8,...)" in {
      __Exercise__
      val sut = ISBN10.weight
      __Verify__
      sut should contain theSameElementsInOrderAs Seq(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
    }

    "check() method should" - {

      "check the isbn validity" in {
        val conversions =
          Table(
            ("isbn", "validity"),
            ("4-7741-1729-3", true),
            ("4-87408-852-X", true),
            ("4-7741-1729-2", false)
          )

        forAll(conversions){ (isbn: String, validity: Boolean) =>
          __Exercise__
          val sut = ISBN10.check(isbn)
          __Verify__
          sut should be (validity)
        }
      }
    }

    "completeISBN() method should" - {

      "append the last digit of ISBN" in {
        val conversions =
          Table(
            ("isb", "isbn"),
            ("4-7741-1729-", "4-7741-1729-3"),
            ("4-7741-1729" , "4-7741-1729-3"),
            ("4-87408-852-", "4-87408-852-X"),
            ("4-87408-852" , "4-87408-852-X")
          )

        forAll(conversions){ (isb: String, isbn: String) =>
          __Exercise__
          val sut = ISBN10.completeISBN(isb)
          __Verify__
          sut should equal (isbn)
        }
      }
    }
  }

  "ISBN-13" - {

    "weight method should be the same Sequence as Seq(1, 3, 1, 3, ...)" in {
      __Exercise__
      val sut = ISBN13.weight
      __Verify__
      sut should contain theSameElementsInOrderAs Seq(1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1)
    }

    "check() method should" - {

      "check the isbn validity" in {
        val conversions =
          Table(
            ("isbn", "validity"),
            ("978-4-7741-6366-6", true),
            ("978-4-7741-6366-5", false)
          )

        forAll(conversions){ (isbn: String, validity: Boolean) =>
          __Exercise__
          val sut = ISBN13.check(isbn)
          __Verify__
          sut should be (validity)
        }
      }
    }

    "completeISBN() method should" - {

      "append the last digit of ISBN" in {
        val conversions =
          Table(
            ("isb", "isbn"),
            ("978-4-7741-6366-", "978-4-7741-6366-6"),
            ("978-4-7741-6366" , "978-4-7741-6366-6")
          )

        forAll(conversions){ (isb: String, isbn: String) =>
          __Exercise__
          val sut = ISBN13.completeISBN(isb)
          __Verify__
          sut should equal (isbn)
        }
      }
    }
  }
}
