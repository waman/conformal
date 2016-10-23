package org.waman.conformal.number.integer.combinatorics

import org.waman.conformal.ConformalCustomSpec

class PermutationNumberSpec extends ConformalCustomSpec{

  "Companion object" - {

    "encode(Permutation) method should" - {

      "encode Permutation objects to Ints uniquely" in {
        val conversions = Table("degree", 1, 2, 3, 4, 5)

        forAll(conversions){ degree: Int=>
          __SetUp__
          val allPerms = Permutation.allPermutations(degree)
          __Exercise__
          val sut = allPerms.map(PermutationNumber.encode)
          __Verify__
          (0 until degree).foreach{ i =>
            sut should contain (i)
          }
        }
      }
    }

    "decode(Long) method should" - {

      "decode Ints to Permutation objects in the reverse way of encode() method" in {
        val conversions = Table("degree", 1, 2, 3, 4, 5)

        forAll(conversions){ degree: Int =>
          __SetUp__
          val expected = Permutation.allPermutations(degree)
          val encoded = expected.map(PermutationNumber.encode)
          __Exercise__
          val sut = encoded.map(PermutationNumber.decode(_, degree))
          __Verify__
          sut should equal (expected)
        }
      }
    }
  }
}
