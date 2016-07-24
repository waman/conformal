package org.waman.conformal.integral.combinatorial

import org.waman.conformal.ConformalCustomSpec

class WithRepetitionSpec extends ConformalCustomSpec{

  "WithRepetition companion object" - {

    "Partial permutation generation with repetition" - {

      "allPermutationWithRepetition(Seq[E]) method should" - {

        "return all permutations with repetition of the specified Seq" in {
          __Exercise__
          val sut = WithRepetition.allPermutations(Seq(0, 1, 2, 3), 3)
          __Verify__
          sut.foreach(println)
          println(sut.length)
        }
      }
    }

    "allCombinationsWithRepetition() method" - {

      "allCombinationWithRepetition(Seq[E]) method should" - {

        "return all combinations with repetition of the specified Seq" in {
          __Exercise__
          val sut = WithRepetition.allCombinations(4, 3)
          __Verify__
          sut.foreach(println)
          println(sut.length)
        }
      }
    }
  }
}
