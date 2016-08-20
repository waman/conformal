package org.waman.conformal.integral.combinatorial

import org.waman.conformal._
import org.waman.conformal.integral.{FactorialRepresentation, _}
import spire.implicits._
import spire.math.Integral

import scala.annotation.tailrec
import scala.collection.immutable.{IndexedSeq, LinearSeq}
/**
  * Permutation trait represents permutations.
  *
  * Example
  *   degree: 4
  *
  * / 0, 1, 2, 3 \  <- indices (in this order)
  * \ 3, 1, 0, 2 /  <- suffices (in this order)
  *
  * = / 2, 1, 3, 0 \  <- proper indices (in this order)
  *   \ 0, 1, 2, 3 /
  *
  *   apply(0) == 3, apply(2) == 0, apply(100) is not defined (maybe throws an exception)
  *   indexOf(3) == 0, indexOf(0) == 2, indexOf(100) is not defined (maybe throws an exception)
  *
  *  Note:
  *   apply(Seq(0, 1, 2, 3)) == Seq(2, 1, 3, 0)
  *
  *   The result is the same not as the suffices of the Permutation,
  *   but is the proper indices (see Wikipedia).
  */
trait Permutation extends Combinatorial[Int] with Ordered[Permutation]{

  override def rank = degree

  def properIndices: Seq[Int] = indices.map(indexOf)
  def suffices: Seq[Int] = indices.map(apply)

  def indexOf(i: Int): Int

  override def apply[E](seq: Seq[E]): Seq[E] = properIndices.map(seq)

  def *(that: Permutation): Permutation
  def andThen(that: Permutation): Permutation = that * this
  def inverse: Permutation

  def sign: Int = {  // (024153)
    @tailrec
    def calculateSign(sign: Int, suffices: Seq[Int], n: Int): Int = suffices match {
      case _ +: Nil => sign
      case head +: _ if head == n => calculateSign(sign, suffices.tail, n+1)
      case _ =>  // case for args: suffices = Seq(2, 4, 1, 5, 3), n = 1
        val i = suffices.indexOf(n)  // i = 2
  
        // val swapped = swap(suffices, 0, i) // swapped = Seq(1, 4, 2, 5, 3)
        // val newProperIndices = swapped.tail
        val newProperIndices = suffices.updated(i, suffices.head).tail
        // newProperIndices = Seq(4, 2, 5, 3)
  
        calculateSign(-sign, newProperIndices, n+1)
    }

    calculateSign(1, suffices.toVector, 0)
  }

  final def sgn = sign

  //***** Order related *****
  override def compare(that: Permutation): Int = {
    Combinatorial.validateComparablity(this, that)

    suffices.zip(that.suffices).find(p => p._1 != p._2) match {
      case None => 0
      case Some((x, y)) => if (x < y) -1 else 1
    }
  }

  /**
    * Return the sequence number in the lexicographic order.
    *
    * Returned object is a FactorialRepresentation one,
    * so if you want it as Int value, call 'toInt' method of it
    * or import 'org.waman.conformal.integral._' to convert type implicitly.
    */
  def sequenceNumber: FactorialRepresentation = {
    @tailrec
    def sequenceNumber(fact: List[Int], suffices: Vector[Int]): List[Int] =
      suffices.isEmpty match {
        case true  => fact
        case false =>
          val (init, last) = (suffices.init, suffices.last)
          val n = last - init.count(_ < last)
          sequenceNumber(n::fact, init)
      }

    val fact = sequenceNumber(Nil, suffices.toVector.init)  // The last place (place of 0!) is removed

    //    // another implementation
    //    @tailrec
    //    def sequenceNumber(fact: Vector[Int], suffices: List[Int]): List[Int] =
    //      suffices.length match {
    //        case 1 => fact  // The last place (place of 0!) is ignored
    //        case _ =>
    //          val a = suffices.head
    //          val list = suffices.map(i => if(i > a) i-1 else i)
    //          sequenceNumber(fact :: list.head, list.tail)
    //      }
    //
    //    val fact = sequenceNumber(Vector(), suffices).toList

    FactorialRepresentation(fact)
  }

  def next: Option[Permutation] = degree match {
    case 1 => None
    case _ =>
      val suf = suffices
      suf.sliding(2).toSeq.lastIndexWhere(p => p.head < p(1)) match {
        case -1 => None
        case i =>  //  case for args: suf = Seq(0, 2, 4, 1, 5, 3), i = 3 (1 < 5)
          val a = suf(i)  // a = 1
          val j = suf.lastIndexWhere(p => a < p)  // j = 5 (1 < 3)
          val swapped = swap(suf, i, j)  // swapped = Seq(0, 2, 4, 3, 5, 1)

          val (first, second) = swapped.splitAt(i+1)  // first = Seq(0, 2, 4, 3), second = Seq(5, 1)
          val newProperIndices = first ++ second.reverse  // newProperIndices = Seq(0, 2, 4, 3, 1, 5)
          Some(new Permutation.SeqPermutation(newProperIndices))
      }
  }

  //***** Cycles *****
  lazy val cycles: Seq[Cycle] = {
    @tailrec
    def extractCycles(accum: Seq[Cycle], rest: Seq[Int]): Seq[Cycle] =
      rest.isEmpty match {
        case true  => accum
        case false =>
          @tailrec
          def extractCycle(c: Vector[Int], x: Int, rest: Seq[Int]): (Cycle, Seq[Int]) =
            if(c.isEmpty || x != c.head)
              extractCycle(c :+ x, apply(x), rest.filter(_ != x))
            else
              (new Cycle.SeqCycle(c), rest)

          val (c, r) = extractCycle(Vector(), rest.head, rest.tail)
          extractCycles(accum :+ c, r)
      }

    extractCycles(Vector(), suffices)
  }

  def toCycleNotation: String =
    cycles.filter(_.length > 1).map(_.toString).mkString
  
  //***** Type Conversions *****
  def toMap: Map[Int, Int] = indices.map(i => (i, apply(i))).toMap

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: Permutation =>
      that.canEqual(this) &&
        degree == that.degree &&
        indices.forall(i => apply(i) == that.apply(i))
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Permutation]

  // a call of asInstanceOf method is for suppressing warning
  override def hashCode: Int = (degree +: suffices.asInstanceOf[Seq[Any]]).hashCode

  override def toString: String = suffices.mkString("[", " ", "]")
}

object Permutation{

  //***** n! and nPr *****
  def permutationCount[I: Integral](degree: I): I = factorial(degree)

  def permutationCount[I: Integral](degree: I, rank: I): I =
    Permutation.permutationCount(degree, rank)

  //***** Subclasses of IntPermutation *****
  private[combinatorial]
  abstract class PermutationAdapter extends Permutation{

    override def *(that: Permutation) = {
      val thiz = this
      new PermutationAdapter{
        override def degree: Int = thiz.degree
        override def indexOf(i: Int) = that.indexOf(thiz.indexOf(i))
        override def apply(i: Int) = thiz(that(i))
      }
    }

    override def inverse: Permutation = {
      val thiz = this
      new PermutationAdapter{
        override def degree: Int = thiz.degree
        override def indexOf(i: Int) = thiz.apply(i)
        override def apply(i: Int) = thiz.indexOf(i)
      }
    }
  }

  /**
    * The constructor of this class is private not to validate argument Seq.
    * The validation is done in apply() factory methods.
    */
  private[combinatorial]
  class SeqPermutation(override val suffices: Seq[Int])
      extends PermutationAdapter{

    override def degree = suffices.length
    override def indexOf(i: Int) = suffices.indexOf(i)
    override def apply(i: Int) = suffices(i)
  }
  
  private[combinatorial]
  class ProperIndicesBasedSeqPermutation(override val properIndices: Seq[Int])
      extends PermutationAdapter{
    
    override def degree = properIndices.length
    override def indexOf(i: Int) = properIndices(i)
    override def apply(i: Int) = properIndices.indexOf(i)
  }

  def identity(deg: Int): Permutation = new PermutationAdapter {
    override def degree = deg
    override def indexOf(i: Int) = i
    override def apply(i: Int) = i

    override def properIndices = indices
    override def suffices = indices
  }

  private def signed(suffices: Seq[Int], signOfPermutation: Int): Permutation =
    new SeqPermutation(suffices){
      override def sign: Int = signOfPermutation
    }

  private def signedPI(properIndices: Seq[Int], signOfPermutation: Int): Permutation =
    new ProperIndicesBasedSeqPermutation(properIndices){
      override def sign: Int = signOfPermutation
    }


  //***** apply() factory methods *****
  // apply constructors validate the arguments as well as instantiation
  def apply(suffices: LinearSeq[Int]): Permutation = {
    Combinatorial.validateArgument(suffices, "suffices")
    new SeqPermutation(suffices)
  }

  def apply(suffices: IndexedSeq[Int]): Permutation = {
    Combinatorial.validateArgument(suffices, "suffices")
    new SeqPermutation(suffices)
  }
  
  def byProperIndices(properIndices: Int*): Permutation = {
    Combinatorial.validateArgument(properIndices, "proper indices")
    new ProperIndicesBasedSeqPermutation(properIndices.toVector)
  }

  trait ConstructorVarargManager[E]{
    def apply(args: Seq[E]): Seq[Int]
  }

  /**
    * <code>
    *   Permutation(0, 3, 1, 2)
    *   Permutation(0 -> 0, 2 -> 1, 3 -> 2, 1 -> 3)
    * </code>
    *
    * @tparam E Int or (Int, Int)
    */
  def apply[E: ConstructorVarargManager](args: E*): Permutation = {
    val manager = implicitly[ConstructorVarargManager[E]]
    val suffices = manager(args)
    Combinatorial.validateArgument(suffices, "suffices")
    new SeqPermutation(suffices)
  }

  //***** Sequence number in lexicographic order *****
  def nthPermutation(n: Int, degree: Int): Permutation =
    nthPermutation(FactorialRepresentation.fromInt(n), degree)

  def nthPermutation(n: FactorialRepresentation, degree: Int): Permutation = {
    @tailrec
    def nthPermutation(suffices: Vector[Int], rest: Seq[Int], fact: Seq[Int]): Vector[Int] =
      fact match {
        case Nil => suffices :+ rest.head
        case 0 +: tail =>
          nthPermutation(suffices :+ rest.head, rest.tail, tail)
        case head +: tail =>
          // case for suffices = Vector(), rest = Seq(0, 1, 2, 3, 4, 5), fact = Seq(3, 1, 0, 2, 1)
          nthPermutation(suffices :+ rest(head), removeAt(rest, head), tail)
        // next call arg: suffices = Vector(3), rest = Seq(0, 1, 2, 4, 5), fact = Seq(1, 0, 2, 1)
      }

    val cs = n.coefficientsAsNthOrderInDescendant(degree-1)
    val suffices = nthPermutation(Vector(), 0 until degree, cs)

    //    @tailrec
    //    def nthPermutation(suffices: List[Int], fact: Vector[Int]): List[Int] = fact.isEmpty match {
    //      case true  => suffices
    //      case false =>
    //        val a = fact.last
    //        val newProperIndices = a :: suffices.map(i => if(i >= a) i+1 else i)
    //        nthPermutation(newProperIndices, fact.init)
    //    }
    //
    //    val suffices = nthPermutation(Nil, n.coefficientsAsNthOrderInDescendant(degree-1).toVector :+ 0)

    new SeqPermutation(suffices)
  }

  //***** Some implementations of generating all permutations *****
  private lazy val allPermutationsWithDegree1: Seq[Permutation] = Seq(identity(1))
  private lazy val allPermutationsWithDegree2: Seq[Permutation] = Seq(identity(2), signed(Seq(1, 0), -1))
  private lazy val allPermutationsWithDegree3: Seq[Permutation] = generatePermutationsWithSign(3)
  private lazy val allPermutationsWithDegree4: Seq[Permutation] = generatePermutationsWithSign(4)

  def allPermutations(degree: Int, calculateSign: Boolean = false): Seq[Permutation] = degree match {
    case 0 => Nil
    case 1 => allPermutationsWithDegree1
    case 2 => allPermutationsWithDegree2
    case 3 => allPermutationsWithDegree3
    case 4 => allPermutationsWithDegree4
    case _ =>
      if (calculateSign)
        generatePermutationsWithSign(degree)
      else
        generatePermutations(degree)
  }

  private[combinatorial]
  def generatePermutations(degree: Int): Seq[Permutation] =
    PartialPermutation.allPermutations(0 until degree, degree)
      .map(new SeqPermutation(_))

  /**
    * Generate IntPermutation objects in lexicographic order with permutation sign pre-calculated
    */
  private[combinatorial]
  def generatePermutationsWithSign(degree: Int): Seq[Permutation] = {
    require(degree > 0)

    case class Builder(suffices: Vector[Int], available: Seq[Int], sign: Int)
      extends CombinatorialBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] =
        available.zipWithIndex.map{ case (e, i) =>
          Builder(
            suffices :+ e,
            available.filter(_ != e),
            if(i % 2 == 0) sign else -sign)
        }

      def toPermutation: Permutation = signed(suffices, sign)
    }

    val start = Builder(Vector(), 0 until degree, 1)
    generateCombinatorials(start, degree).map(_.toPermutation)
  }

  def allPermutations[E](arg: Seq[E]): Seq[Seq[E]] =
    PartialPermutation.allPermutations(arg, arg.length)

  def allPermutations(s: String): Seq[String] = allPermutations(s: Seq[Char]).map(_.mkString)

  /* For implementation interest */
  private[combinatorial]
  def allPermutations2(degree: Int): Seq[Seq[Int]] = {

    case class Builder(seq: Vector[Int])
      extends CombinatorialBuilder[Int, Builder]{

      val length: Int = seq.length

      override def nextGeneration: Seq[Builder] =
        (length to 0 by -1).map(i => Builder(insertAt(seq, i, length)))
    }

    val start = Builder(Vector())
    generateCombinatorials(start, degree).map(_.seq)
  }

  /* For implementation interest */
  private[combinatorial]
  def allPermutations3[E](arg: Seq[E]): Seq[Seq[E]] = {

    case class Builder(seq: Vector[E], n: Int)
      extends CombinatorialBuilder[E, Builder]{

      override def nextGeneration: Seq[Builder] =
        (n to 0 by -1).map(i => Builder(swap(seq, i, n), n-1))
    }

    val start = Builder(arg.toVector, arg.length-1)
    generateCombinatorials(start, arg.length).map(_.seq)
  }

  /* For implementation interest */
  private[combinatorial]
  def allPermutations4[E](arg: Seq[E]): Seq[Seq[E]] = {
    if(arg.isEmpty)return Seq(Nil)

    val counters = factorialCounters(arg.length).drop(1)

    Stream.iterate((arg, counters)){
      case (a, Nil) => (Nil, Nil)
      case (a, fcs) =>
        val FactorialCounter(c, k) = fcs.head
        val i = if(k % 2 == 0) 0 else c(k)
        val newA = swap(a, i, k)
        (newA, fcs.tail)
    }.takeWhile(_._1.nonEmpty).map(_._1)
  }

  // For implementation interest
  private[combinatorial]
  case class FactorialCounter(c: Seq[Int], k: Int){
    def hasNext: Boolean = k != -1
    def next: FactorialCounter =
      c.indexWhere(_ != 0) match {
        case -1 => FactorialCounter(Nil, -1)
        case newK =>
          val s = c.slice(newK, c.length)
          val newC = (0 until newK) ++: (s.head-1) +: s.tail
          FactorialCounter(newC, newK)
      }
  }

  // For implementation interest
  private[combinatorial]
  def factorialCounters(degree: Int): Seq[FactorialCounter] = {
    val start = FactorialCounter(0 until degree, 1)
    Stream.iterate(start)(_.next).takeWhile(_.hasNext)
  }

  //**** Other permutation generators *****
  private def generateParityPermutations(degree: Int, parity: Int): Seq[Seq[Int]] = {
    require(degree > 0)

    if(degree == 1)
      return if(parity >= 0) Seq(Seq(0)) else Nil

    case class Builder(seq: Vector[Int], available: Seq[Int], sign: Int)
      extends CombinatorialBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] =
        available.zipWithIndex.map{ case (e, i) =>
          Builder(
            seq :+ e,
            available.filter(_ != e),
            if(i % 2 == 0) sign else -sign
          )
        }
    }

    val start = Builder(Vector(), 0 until degree, 1)

    generateCombinatorials(start, degree-2).map{
      case b if b.sign == parity =>
        b.seq ++: b.available
      case b =>
        b.seq ++: b.available.reverse
    }
  }

  def evenPermutations(degree: Int): Seq[Permutation] =
    generateParityPermutations(degree, 1).map(signed(_, 1))

  def evenPermutations[E](arg: Seq[E]): Seq[Seq[E]] =
    generateParityPermutations(arg.length, 1).map(_.map(arg))

  def evenPermutations(s: String): Seq[String] = evenPermutations(s: Seq[Char]).map(_.mkString)

  def oddPermutations(degree: Int): Seq[Permutation] =
    generateParityPermutations(degree, -1).map(signed(_, -1))

  def oddPermutations[E](arg: Seq[E]): Seq[Seq[E]] =
    generateParityPermutations(arg.length, -1).map(_.map(arg))

  def oddPermutations(s: String): Seq[String] = oddPermutations(s: Seq[Char]).map(_.mkString)


  def derangementCount[I: Integral](degree: I): I = degree match {
    case 1 => 0
    case _ =>
      @tailrec
      def derangementCount(current: I, previous: I, n: I, i: Int): I = i match {
        case 2 => current
        case _ =>
          val next = n * (current + previous)
          derangementCount(next, current, n+1, i-1)
      }

      derangementCount(1, 0, 2, degree.toInt)
  }

  // For implementation interest
  private[combinatorial]
  def derangementCount1[I: Integral](degree: I): I = {
    require(degree > 0, s"Degree of derangement must be positive: appear $degree")

    @tailrec
    def derangementCount(sum: I, k: I, sign: I): I = k match {
      case 1 => sum
      case _ =>
        derangementCount(sum + sign * permutationCount(degree, degree-k), k-1, -sign)
    }

    derangementCount(0, degree, (-1)**degree.toInt)
  }

  private def generateDerangements(degree: Int): Seq[Seq[Int]] = {
    require(degree > 0)

    case class Builder(seq: Vector[Int], available: Seq[Int], position: Int)
      extends CombinatorialBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] =
        available.filter(_ != position).map( e  =>
          Builder(
            seq :+ e,
            available.filter(_ != e),
            position+1)
        )
    }

    degree match {
      case 1 => Nil
      case _ =>
        val start = Builder(Vector(), 0 until degree, 0)
        generateCombinatorials(start, degree).map(_.seq)
    }
  }

  def derangements(degree: Int): Seq[Permutation] =
    generateDerangements(degree).map(new SeqPermutation(_))

  def derangements[E](arg: Seq[E]): Seq[Seq[E]] =
    generateDerangements(arg.length).map(_.map(arg))

  def derangements(s: String): Seq[String] = derangements(s: Seq[Char]).map(_.mkString)
}