package org.waman.conformal

class ConformalFunctionSpec  extends ConformalCustomSpec{

  "swap(Seq, Int, Int) method should" - {

    "exchange the two elements of 1st arg seq specified by the 2nd and 3rd arg" in {
      val conversions = Table(
        ("i", "j", "expected"),
        (0, 3, Seq("d", "b", "c", "a", "e")),
        (1, 1, Seq("a", "b", "c", "d", "e"))
      )

      __SetUp__
      val seq = Seq("a", "b", "c", "d", "e")

      forAll(conversions){ (i: Int, j: Int, expected: Seq[String]) =>
        __Exercise__
        val sut = swap(seq, i, j)
        __Verify__
        sut should contain theSameElementsInOrderAs expected
      }
    }
  }

  "removeAt(Seq, Int) method should" - {

    "remove the element of 1st arg seq specified by the 2nd arg" in {
      val conversions = Table(
        ("i", "expected"),        (0, Seq("b", "c", "d", "e")),
        (1, Seq("a", "c", "d", "e"))
      )

      __SetUp__
      val seq = Seq("a", "b", "c", "d", "e")

      forAll(conversions){ (i: Int, expected: Seq[String]) =>
        __Exercise__
        val sut = removeAt(seq, i)
        __Verify__
        sut should contain theSameElementsInOrderAs expected
      }
    }
  }

  "indexOfMax method should" - {

    "return the index of the max element" in {
      val conversions =
        Table(
          ("seq", "expected"),
          (Seq(2, 1, 3, 0), 2)
        )

      forAll(conversions){ (seq: Seq[Int], expected: Int) =>
        __Exercise__
        val sut = indexOfMax(seq)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "indexOfMin method should" - {

    "return the index of the min element" in {
      val conversions =
        Table(
          ("seq", "expected"),
          (Seq(2, 1, 3, 0), 3)
        )

      forAll(conversions){ (seq: Seq[Int], expected: Int) =>
        __Exercise__
        val sut = indexOfMin(seq)
        __Verify__
        sut should equal (expected)
      }
    }
  }
}
