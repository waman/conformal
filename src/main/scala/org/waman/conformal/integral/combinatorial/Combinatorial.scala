package org.waman.conformal.integral.combinatorial

trait Combinatorial[T]{

  def degree: Int
  def rank: Int

  def indices: Seq[Int] = 0 until degree
  def indexIsDefinedAt(i: Int): Boolean = indices.contains(i)

  def apply(i: Int): T
  def apply[E](seq: Seq[E]): Seq[E]
  def apply(s: String): String = apply(s: Seq[Char]).mkString
}

object Combinatorial{

  def validateArgument(arg: Seq[Int], s: String): Unit =
    validateArgument(arg.length, arg, s)

  def validateArgument(degree: Int, arg: Traversable[Int], s: String): Unit = {

    require(degree > 0, s"The degree of the partial combinatorial must be positive: $degree")

    val range = 0 until degree

    arg.foreach{ e =>
      require(range.contains(e),
        s"All $s must be included in [0, $degree): $e appears")
    }

    range.foreach { i =>
      val c = arg.count(_ == i)
      require(c == 0 || c == 1,
        s"""All integers in [0, $degree) must be included in the $s at most once:
              $i appears $c times""".stripMargin)
    }
  }

  def validateComparablity[C <: Combinatorial[_]](thiz: C, that: C): Unit = {

    require(thiz.degree == that.degree,
      s"""Two partial permutations can be compared when these degrees are the same values:
        this degree: ${thiz.degree}
        that degree: ${that.degree}""")

    require(thiz.rank == that.rank,
      s"""Two partial permutations can be compared when these ranks are the same values:
        this rank: ${thiz.rank}
        that rank: ${that.rank}""")
  }
}