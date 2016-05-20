package org.waman.conformal

class ConformalFunctionSpec  extends ConformalCustomSpec{

  "swap(Seq, Int, Int) method should" - {

    "exchange the two elements of 1st arg seq specified by the 2nd and 3rd arg" in {
      __SetUp__
      val seq = Seq("a", "b", "c", "d", "e")
      __Exercise__
      val sut = swap(seq, 0, 3)
      __Verify__
      sut should contain theSameElementsInOrderAs Seq("d", "b", "c", "a", "e")
    }
  }
}
