package org.waman.conformal.integral.combinatorial

/**
  * Note that apply(Seq[E]) method work in a different way
  * from Permutation and PassivePermutation trait.
  *
  * The apply method is place-base permutation
  *
  * Example
  *   degree: 4
  *   rank: 3
  *
  * / 3 1 0 (2) \  <- proper indices in this order (NOT containing 2)
  * \ 0 1 2 (-) /
  *
  * = / 0 1 (2) 3 \  <- indices (containing 2)
  *   \ 2 1 (-) 0 /  <- suffices
  *
  * In the above example, an object at place 3 is moved to place 0
  */
trait PartialPermutation
    extends PartialIntCombinatorial[Int]
    with Ordered[PartialPermutation]{

  def properIndices: Seq[Int]

  override def apply[E](seq: Seq[E]): Seq[E] = properIndices.map(seq)

  //***** Order related *****
  override def compare(that: PartialPermutation): Int = {
    require(degree == that.degree,
      s"""Two partial permutations can be compared when these degrees are the same values:
        this degree: $degree
        that degree: ${that.degree}""")

    require(rank == that.rank,
      s"""Two partial permutations can be compared when these ranks are the same values:
        this rank: $rank
        that rank: ${that.rank}""")

    properIndices.zip(that.properIndices).find(p => p._1 != p._2) match {
      case None => 0
      case Some((x, y)) =>
        if (x < y) -1
        else 1
    }
  }

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: PartialPermutation =>
      that.canEqual(this) &&
        degree == that.degree &&
        properIndices == that.properIndices
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[PartialPermutation]

  // a call of asInstanceOf method is for suppressing warning
  override def hashCode: Int = (degree +: properIndices).hashCode

  override def toString: String = properIndices.mkString("[", " ", "]")
}

object PartialPermutation{

  //***** Partial permutation count *****
  def permutationCount(degree: Int, rank: Int): Int =
    Permutation.permutationCount(degree, rank)

  //***** apply() factory method *****
  def apply(degree: Int, properIndices: Seq[Int]): PartialPermutation = {
    require(degree > 0, s"The degree of partial, permutation must be positive: $degree")

    val range = 0 until degree

    properIndices.foreach{ e =>
      require(range.contains(e),
        s"All elements of proper indices must be included in [0, $degree): $e appears")
    }

    range.foreach { i =>
      val c = properIndices.count(_ == i)
      require(c == 0 || c == 1,
        s"""All integers in [0, $degree) must be included in the argument proper indices at most once:
              $i appears $c times""".stripMargin)
    }

    new SeqPartialPermutation(degree, properIndices.toVector)
  }

  class PartialPermutationFactory(val degree: Int){

    def apply(properIndices: Int*): PartialPermutation =
      PartialPermutation.this.apply(degree, properIndices.toVector)
  }

  def apply(degree: Int): PartialPermutationFactory =
    new PartialPermutationFactory(degree)

  //***** Permutation generation *****
  private[integral]
  class SeqPartialPermutation(val degree: Int, val properIndices: Seq[Int])
      extends PartialPermutation{

    override def rank = properIndices.length
    override def apply(i: Int) = properIndices.indexOf(i)
  }

  def allPermutations(degree: Int, rank: Int): Seq[PartialPermutation] =
    Permutation.generatePermutations(degree, rank).map(new SeqPartialPermutation(degree, _))

  def allPermutations[E](seq: Seq[E], rank: Int): Seq[Seq[E]] =
    Permutation.generatePermutations(seq.toVector, rank)

  def allPermutations(s: String, rank: Int): Seq[String] =
    allPermutations(s: Seq[Char], rank).map(_.mkString)
}