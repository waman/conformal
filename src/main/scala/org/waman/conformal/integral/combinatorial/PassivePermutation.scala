package org.waman.conformal.integral.combinatorial

import org.waman.conformal.integral.FactorialRepresentation

/**
  * Permutation[E] trait represents (passive) permutations.
  *
  * Example
  *   degree: 4
  *   E (type parameter): String
  *
  * / "a", "b", "c", "d" \  <- indices (in this order)
  * \ "d", "b", "a", "c" /  <- suffices (in this order)
  *
  *   apply("a") == "d", apply("c") == "a", apply("z") is not defined (maybe throws an exception)
  *   indexOf("d") == "a", indexOf("a") == "c", indexOf("z") is not defined (maybe throws an exception)
  *
  *   apply(Seq("a", "b", "c", "d")) == Seq("d", "b", "a", "c")
  */
trait PassivePermutation[E] extends Combinatorial[E, E]
    with PartialFunction[E, E]
    with Ordered[PassivePermutation[E]] {

  def suffices: Seq[E] = indices.map(apply)
  def sufficesToIntSeq: Seq[Int] = suffices.map(indices.indexOf)

  override def isDefinedAt(e: E): Boolean = indices.contains(e)

  def indexOf(e: E): E

  def apply(seq: Seq[E]): Seq[E] = {
    require(seq.length == degree,
      "The argument Seq must have the same length as the degree of this permutation.")

    seq.map(apply)
  }

  def *(that: PassivePermutation[E]): PassivePermutation[E]
  def andThen(that: PassivePermutation[E]): PassivePermutation[E] = that * this
  def inverse: PassivePermutation[E]

  /**
    * Note that the default implementation calculate sign every time,
    * so it is recommended to override this implementation in subclasses
    */
  def sign: Int = toPermutation.sign
  final def sgn = sign

  def next: Option[PassivePermutation[E]]

  //***** Methods of Order *****
  /**
    * Return the sequence number in the lexicographic order.
    *
    * Returned object is a FactorialRepresentation one,
    * so if you want it as Int value, call 'toInt' method of it
    * or import 'org.waman.conformal.integral._' to convert type implicitly.
    */
  def sequenceNumber: FactorialRepresentation =
    toPermutation.sequenceNumber

  //***** Type converters *****
  def toPermutation: Permutation = new Permutation.SeqPermutation(sufficesToIntSeq)
  def toMap: Map[E, E] = indices.map{i => (i, apply(i))}.toMap

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: PassivePermutation[E] =>
      that.canEqual(this) &&
        degree == that.degree &&
        indices.forall(i => apply(i) == that.apply(i))
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[PassivePermutation[_]]

  // a call of asInstanceOf method is for suppressing warning
  override def hashCode: Int = (degree +: suffices.asInstanceOf[Seq[Any]]).hashCode

  override def toString: String = suffices.mkString("[", " ", "]")
}

class PassivePermutationFactory[E](val indices: Seq[E]){

  def degree = indices.length

  //***** Implementations of Permutation trait *****
  trait PassivePermutationAdapter extends PassivePermutation[E] {

    override def degree = PassivePermutationFactory.this.degree
    override def indices = PassivePermutationFactory.this.indices

    override def *(that: PassivePermutation[E]) = {
      val thiz = this
      new PassivePermutationAdapter{
        override def degree = thiz.degree
        override def indices = thiz.indices
        override def apply(e: E) = thiz(that(e))
        override def indexOf(e: E) = that.indexOf(thiz.indexOf(e))
        override def sign = thiz.sign * that.sign
      }
    }

    override def inverse = {
      val thiz = this
      new PassivePermutationAdapter{
        override def degree = thiz.degree
        override def indices = thiz.indices
        override def apply(e: E) = thiz.indexOf(e)
        override def indexOf(e: E) = thiz(e)
        override def sign = thiz.sign
      }
    }

    def next: Option[PassivePermutation[E]] = toPermutation.next match {
      case Some(p) => Some(fromPermutation(p))
      case _ => None
    }

    //***** Order related *****
    override def compare(that: PassivePermutation[E]): Int =
      toPermutation.compare(that.toPermutation)
  }

  lazy val identity: PassivePermutation[E] = new PassivePermutationAdapter {
    override def apply(e: E) = e
    override def apply(seq: Seq[E]) = seq
    override def indexOf(e: E) = e
    override def suffices: Seq[E] = indices
    override def sign = 1
  }

  /**
    * This class is private to avoid validation of argument Seq.
    * The validation is done in apply() factory methods.
    */
  private[combinatorial]
  class SeqPassivePermutation(suffices: Seq[E])
      extends PassivePermutationAdapter {

    override def apply(e: E) = suffices(indices.indexOf(e))
    override def indexOf(e: E) = indices(suffices.indexOf(e))
    override def toMap = indices.zip(suffices).toMap
  }

  def apply(suffices: E*): PassivePermutation[E] = {
    require(suffices.length == degree,
      s"The suffices of the permutation must have $degree elements: only ${suffices.length} elements appear.")

    suffices.foreach{ e =>
      require(indices.contains(e),
        s"The suffices of the permutation must contain the element $e")
    }

    new SeqPassivePermutation(suffices.toVector)
  }

  def fromPermutation(p: Permutation): PassivePermutation[E] =
    apply(p.suffices.map(indices):_*)

  //***** Sequence number in lexicographic order *****
  def nthPermutation(n: Int, degree: Int): PassivePermutation[E] =
    nthPermutation(FactorialRepresentation.fromInt(n))

  def nthPermutation(n: FactorialRepresentation): PassivePermutation[E] =
    fromPermutation(Permutation.nthPermutation(n, degree))

  //***** Permutation Generators *****
  def allPermuted: Seq[Seq[E]] = Permutation.generatePermutations(indices.toVector)

  def allPermutations: Seq[PassivePermutation[E]] = allPermuted.map(apply)

  def allPermutedStrings: Seq[String] = allPermuted.map(_.mkString)

  //**** Other permutation generators *****
  def evenPermutations: Seq[PassivePermutation[E]] =
    Permutation.evenPermutations(degree).map(fromPermutation)

  def evenPermuted: Seq[Seq[E]] = evenPermutations.map(_.suffices)


  def oddPermutations: Seq[PassivePermutation[E]] =
    Permutation.oddPermutations(degree).map(fromPermutation)

  def oddPermuted: Seq[Seq[E]] = oddPermutations.map(_.suffices)


  def derangementPermutations: Seq[PassivePermutation[E]] =
    Permutation.derangements(degree).map(fromPermutation)

  def derangements: Seq[Seq[E]] = derangementPermutations.map(_.suffices)
}