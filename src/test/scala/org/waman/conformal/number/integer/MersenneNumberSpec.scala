package org.waman.conformal.number.integer

import org.waman.conformal.ConformalCustomSpec
import org.waman.conformal.tags.ForImplementationInterest

class MersenneNumberSpec extends ConformalCustomSpec{

  "Constructor should" - {

    "throw an exception if the argument is negative" in {
      __Verify__
      an [Exception] should be thrownBy {
        MersenneNumber(-4)
      }
    }
  }

  "isPrime method should" - {

    "return true for the following integers" in {
      val conversions = Table("p", 2, 3, 5, 7, 13, 17, 19, 31, 61, 89, 107, 127, 521, 607)

      forAll(conversions){ p: Int =>
        __SetUp__
        val mn = MersenneNumber(p)
        __Exercise__
        val sut = mn.isPrime
        __Verify__
        sut should be (true)
      }
    }

    "return false for the following integers" in {
      val conversions = Table("p", 0, 1, 67, 257)

      forAll(conversions){ p: Int =>
        __SetUp__
        val mn = MersenneNumber(p)
        __Exercise__
        val sut = mn.isPrime
        __Verify__
        sut should be (false)
      }
    }
  }

//  "isPrime2 method should" - {
//
//    "return true for the following integers" taggedAs ForImplementationInterest ignore {
//      val conversions = Table("p", 2, 3, 5, 7, 13, 17, 19, 31, 61, 89, 107, 127, 521, 607)
//
//      forAll(conversions){ p: Int =>
//        __SetUp__
//        val mn = MersenneNumber(p)
//        __Exercise__
//        val sut = mn.isPrime2
//        __Verify__
//        sut should be (true)
//      }
//    }
//
//    "return false for the following integers" taggedAs ForImplementationInterest ignore {
//      val conversions = Table("p", 0, 1, 67, 257)
//
//      forAll(conversions){ p: Int =>
//        __SetUp__
//        val mn = MersenneNumber(p)
//        __Exercise__
//        val sut = mn.isPrime2
//        __Verify__
//        sut should be (false)
//      }
//    }
//  }
}
