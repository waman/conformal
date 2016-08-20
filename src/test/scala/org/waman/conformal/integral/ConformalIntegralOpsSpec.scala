package org.waman.conformal.integral

import org.waman.conformal.ConformalCustomSpec
import org.waman.conformal.integral.ConformalIntegralOps._
import org.waman.conformal.integral.combinatorial.{Combination, Permutation, WithRepetition}

import scala.language.postfixOps

class ConformalIntegralOpsSpec extends ConformalCustomSpec{

  "combinatorial" - {

    val conversions = Table("n", 0, 1, 2, 3, 4, 5, 6)

    "! operator should" - {

      "return factorial of the specified integer" in {
        //import spire.implicits._
        forAll(conversions){ n: Int=>
          __SetUp__
          val expected = factorial(n)
          __Exercise__
          val sut = n!;
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "!! operator should" - {

      "return factorial of the specified integer" in {

        forAll(conversions){ n: Int=>
          __SetUp__
          val expected = doubleFactorial(n)
          __Exercise__
          val sut = n!!;
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "P(Int) method should" - {

      "return nPr (permutation count)" in {
        forAll(conversions) { n: Int =>
          (0 to n).foreach { r =>
            __SetUp__
            val expected = Permutation.permutationCount(n, r)
            __Exercise__
            val sut = n P r
            __Verify__
            sut should equal(expected)
          }
        }
      }
    }

    "C(Int) method should" - {

      "return nCr (combination count)" - {

        forAll(conversions){ n: Int =>
          (0 to n).foreach{ r =>
            __SetUp__
            val expected = Combination.combinationCount(n, r)
            __Exercise__
            val sut = n C r
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }

    "H(Int) method should" - {

      "return nHr (the number of combinations with repetition)" - {

        forAll(conversions){ n: Int =>
          (0 to (n+2)).foreach{ r =>
            __SetUp__
            val expected = WithRepetition.combinationCount(n, r)
            __Exercise__
            val sut = n H r
            __Verify__
            sut should equal (expected)
          }
        }
      }
    }
  }
}
