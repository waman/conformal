package org.waman.conformal.integral.combinatorial

import scala.annotation.tailrec
import scala.collection.immutable.{IndexedSeq, LinearSeq}
import spire.math.Integral
import spire.implicits._

import org.waman.conformal.integral.FactorialRepresentation
import org.waman.conformal._
import org.waman.conformal.integral._

/**
  * Permutation defined in an active way,
  * that is a permutation of integers
  */
trait Permutation extends PassivePermutation[Int]
  with IntCombinatorial[Int] with Ordered[PassivePermutation[Int]]{

  override def *(that: PassivePermutation[Int]) =
    *(that.toPermutation: Permutation)

  def *(that: Permutation): Permutation
  def andThen(that: Permutation): Permutation = that * this
  override def inverse: Permutation

  override def sign: Int = {  // (024153)
  @tailrec
  def calculateSign(sign: Int, suffices: Seq[Int], n: Int): Int = suffices match {
      case _ +: Nil => sign
      case head +: _ if head == n => calculateSign(sign, suffices.tail, n+1)
      case _ =>  // case for args: suffices = Seq(2, 4, 1, 5, 3), n = 1
        val i = suffices.indexOf(n)  // i = 2

        //          val swapped = swap(suffices, 0, i) // swapped = Seq(1, 4, 2, 5, 3)
        //          val newSuffices = swapped.tail
        val newSuffices = suffices.updated(i, suffices.head).tail
        // newSuffices = Seq(4, 2, 5, 3)

        calculateSign(-sign, newSuffices, n+1)
    }

    calculateSign(1, suffices.toVector, 0)
  }

  //***** Order related *****
  override def compare(that: PassivePermutation[Int]): Int = {
    require(degree == that.degree,
      s"""Two Permutations can be compared when these degrees are the same values:
        this degree: $degree
        that degree: ${that.degree}""")

    suffices.zip(that.suffices).find(p => p._1 != p._2) match {
      case None => 0
      case Some((x, y)) =>
        if (x < y) -1
        else 1
    }
  }
  /**
    * Return the sequence number in the lexicographic order.
    *
    * Returned object is a FactorialRepresentation one,
    * so if you want it as Int value, call 'toInt' method of it
    * or import 'org.waman.conformal.integral._' to convert type implicitly.
    */
  override def sequenceNumber: FactorialRepresentation = {
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

  override def next: Option[Permutation] = degree match {
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
        val newSuf = first ++ second.reverse  // newSuf = Seq(0, 2, 4, 3, 1, 5)
          Some(new Permutation.SeqPermutation(newSuf))
      }
  }

  //***** Type Conversions *****
  override def toPermutation: Permutation = this
}

object Permutation{

  //***** n! and nPr *****
  def permutationCount[I: Integral](n: I): I = factorial(n)
  def permutationCount[I: Integral](n: I, r: I): I = {
    @tailrec
    def permutationCount(prod: I, n: I, r: I): I = r match {
      case 0 => prod
      case _ => permutationCount(prod * n, n-1, r-1)
    }

    permutationCount(1, n, r)
  }

  def factory[E](indices: Seq[E]): PassivePermutationFactory[E] =
    new PassivePermutationFactory(indices)

  def factory(indices: String): PassivePermutationFactory[Char] =
    factory(indices: Seq[Char])

  def passive[E](indices: E*): PassivePermutationFactory[E] =
    factory(indices.toVector)

  //***** Subclasses of IntPermutation *****
  private[integral]
  abstract class PermutationAdapter extends Permutation{

    override def indices = 0 until degree

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

  private[integral]
  abstract class AbstractSeqPermutation(override val suffices: Seq[Int])
    extends PermutationAdapter{

    override def degree = suffices.length
    override def indexOf(i: Int) = suffices.indexOf(i)
    override def apply(i: Int) = suffices(i)
  }

  /**
    * The constructor of this class is private not to validate argument Seq.
    * The validation is done in apply() factory methods.
    */
  private[integral]
  class SeqPermutation(suffices: Seq[Int])
    extends AbstractSeqPermutation(suffices) {

    override lazy val sign = super.sign
  }

  def identity(deg: Int): Permutation = new PermutationAdapter{
    override def degree = deg
    override def indexOf(i: Int) = i
    override def apply(i: Int) = i
  }

  //***** apply() factory methods *****
  // apply constructors validate the arguments as well as instantiation
  def apply(suffices: LinearSeq[Int]): Permutation = {
    validateSuffices(suffices)
    new SeqPermutation(suffices)
  }

  def apply(suffices: IndexedSeq[Int]): Permutation = {
    validateSuffices(suffices)
    new SeqPermutation(suffices)
  }

  private def validateSuffices(suffices: Seq[Int]): Unit = {
    val degree = suffices.length

    require(degree > 0, s"The degree of permutation must be positive: $degree")
    suffices.indices.foreach { i =>
      require(suffices.count(_ == i) == 1,
        s"Suffices of permutation must contain all integers once from 0 until $degree")
    }
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
    validateSuffices(suffices)
    new SeqPermutation(suffices)
  }

  //***** Some implementations of generating all permutations *****
  private def signed(suffices: Seq[Int], signOfPermutation: Int): Permutation =
    new AbstractSeqPermutation(suffices){
      override def sign: Int = signOfPermutation
    }

  private lazy val allPermutationsOfOrder1 = Seq(identity(1))
  private lazy val allPermutationsOfOrder2 = Seq(identity(2), signed(Vector(1, 0), -1))
  private lazy val allPermutationsOfOrder3 = Seq(
    identity(3),
    signed(Vector(0, 2, 1), -1),
    signed(Vector(1, 0, 2), -1),
    signed(Vector(1, 2, 0),  1),
    signed(Vector(2, 0, 1),  1),
    signed(Vector(2, 1, 0), -1))

  def allPermutations(degree: Int, calculateSign: Boolean = false): Seq[Permutation] =
    degree match {
      case 1 => allPermutationsOfOrder1
      case 2 => allPermutationsOfOrder2
      case 3 => allPermutationsOfOrder3
      case _ =>
        if(calculateSign)
          generatePermutationsWithSign(degree)
        else
          generatePermutations(degree)
    }

  def allPartialPermutations(degree: Int, rank: Int): Seq[Seq[Int]] =
    generatePermutations(degree, rank)

  trait PermutationBuilder[E, B <: PermutationBuilder[E, B]]
      extends CombinatorialBuilder[E, B]{

    def suffices: Seq[E]
  }

  /**
    * Generate all (partial) permutations of any seq in lexicographic order
    * (if the input seq is ascendant).
    */
  private[integral]
  def generatePermutations[E](suffices: Vector[E], rank: Int): Seq[Vector[E]] = {

    case class Builder(suffices: Vector[E], available: Vector[E])
      extends PermutationBuilder[E, Builder]{

      override def nextGeneration: Seq[Builder] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder], i: Int): Seq[Builder] =
          i match {
            case -1 => accum
            case _  =>
              val newProperIndices = suffices :+ available(i)
              val newAvailable = removeAt(available, i)
              val newBuilder = Builder(newProperIndices, newAvailable)
              nextGeneration(newBuilder +: accum, i-1)
          }

        nextGeneration(Nil, available.length-1)
      }
    }

    val init = Builder(Vector(), suffices)
    generateCombinatorial(init, rank).map(_.suffices)
  }

  private[integral]
  def generatePermutations(degree: Int, rank: Int): Seq[Seq[Int]] =
    generatePermutations((0 until degree).toVector, rank)

  private[integral]
  def generatePermutations[E](suffices: Vector[E]): Seq[Vector[E]] =
    generatePermutations(suffices, suffices.length)

  private[integral]
  def generatePermutations(degree: Int): Seq[Permutation] =
    generatePermutations((0 until degree).toVector).map(new SeqPermutation(_))

  /**
    * Generate IntPermutation objects in lexicographic order with permutation sign pre-calculated
    */
  private[integral]
  def generatePermutationsWithSign(degree: Int): Seq[Permutation] = {
    require(degree > 0)

    case class Builder(suffices: Vector[Int], available: Vector[Int], sign: Int)
      extends PermutationBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder], i: Int): Seq[Builder] =
          i match {
            case -1 => accum
            case _  =>
              val newSuffices = suffices :+ available(i)
              val newAvailable = removeAt(available, i)
              val newSign = if(i % 2 == 0) sign else -sign
              val newBuilder = Builder(newSuffices, newAvailable, newSign)
              nextGeneration(newBuilder +: accum, i-1)
          }

        nextGeneration(Nil, available.length-1)
      }

      def toPermutation: Permutation = signed(suffices, sign)
    }

    val init = Builder(Vector(), (0 until degree).toVector, 1)
    generateCombinatorial(init, degree).map(_.toPermutation)
  }

  //**** Other permutation generators *****
  private def parityPermutations(degree: Int, parity: Int): Seq[Permutation] = {
    require(degree > 1)

    case class Builder(suffices: Vector[Int], available: Vector[Int], sign: Int)
      extends PermutationBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder], i: Int): Seq[Builder] =
          i match {
            case -1 => accum
            case _  =>
              val newSuffices = suffices :+ available(i)
              val newAvailable = removeAt(available, i)
              val newSign = if(i % 2 == 0) sign else -sign
              val newBuilder = Builder(newSuffices, newAvailable, newSign)
              nextGeneration(newBuilder +: accum, i-1)
          }

        nextGeneration(Nil, available.length-1)
      }
    }

    val init = Builder(Vector(), (0 until degree).toVector, 1)

    generateCombinatorial(init, degree-2).map{
      case b if b.sign == parity =>
        b.suffices ++: b.available
      case b =>
        b.suffices ++: b.available.reverse
    }.map(signed(_, parity))
  }

  def evenPermutations(degree: Int): Seq[Permutation] = degree match {
    case 1 => Seq(signed(Seq(0), 1))
    case _ => parityPermutations(degree, 1)
  }

  def oddPermutations(degree: Int): Seq[Permutation] = degree match {
    case 1 => Nil
    case _ => parityPermutations(degree, -1)
  }


  def derangements(degree: Int): Seq[Permutation] = {
    require(degree > 0)

    case class Builder(suffices: Vector[Int], available: Vector[Int], n: Int)
      extends PermutationBuilder[Int, Builder]{

      override def nextGeneration: Seq[Builder] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder], i: Int): Seq[Builder] = {
          i match {
            case -1 => accum
            case _ =>
              val entry = available(i)
              if(entry == n) {
                nextGeneration(accum, i-1)
              }else{
                val newSuffices = suffices :+ entry
                val newAvailable = removeAt(available, i)
                val newBuilder = Builder(newSuffices, newAvailable, n+1)
                nextGeneration(newBuilder +: accum, i-1)
              }
          }
        }

        nextGeneration(Nil, available.length-1)
      }
    }

    degree match {
      case 1 => Nil
      case _ =>
        val init = Builder(Vector(), (0 until degree).toVector, 0)
        generateCombinatorial(init, degree).map(b => new SeqPermutation(b.suffices))
    }
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
    //        val newSuffices = a :: suffices.map(i => if(i >= a) i+1 else i)
    //        nthPermutation(newSuffices, fact.init)
    //    }
    //
    //    val suffices = nthPermutation(Nil, n.coefficientsAsNthOrderInDescendant(degree-1).toVector :+ 0)

    new SeqPermutation(suffices)
  }
}