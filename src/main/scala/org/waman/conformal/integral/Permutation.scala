package org.waman.conformal.integral

import org.waman.conformal._
import spire.math.Integral

import scala.annotation.tailrec
import scala.collection.immutable.LinearSeq

trait Permutation extends PartialFunction[Int, Int] with Ordered[Permutation]{

  def degree: Int
  protected def indices: Range = 0 until degree

  override def isDefinedAt(i: Int): Boolean = indices.contains(i)

  override def apply(i: Int): Int

  /** This method should be overridden by subclasses.  */
  def indexOf(i: Int): Int = indices.find(apply(_) == i).get

  def apply[E](seq: Seq[E]): Seq[E] = {
    require(seq.length == degree,
      "The argument Seq must have the same length as the degree of this permutation.")
    indices.map(indexOf).map(seq(_))
  }

  def apply[E](list: List[E]): List[E] = apply(list: Seq[E]).toList

  def apply(s: String): String = apply(s: Seq[Char]).mkString

  def *(p: Permutation): Permutation = {
    require(degree == p.degree)
    val th = this
    new Permutation {
      override def degree: Int = th.degree
      override def apply(i: Int): Int = th(p(i))
      override def sgn: Int = th.sgn * p.sgn
    }
  }

  def andThen(p: Permutation): Permutation = p * this

  def inverse: Permutation = {
    val th = this
    new Permutation {
      override def degree: Int = th.degree
      override def apply(i: Int): Int = th.indexOf(i)
      override def indexOf(i: Int): Int = th.apply(i)
      override def inverse: Permutation = th
      override def sgn: Int = th.sgn
    }
  }

  def sgn: Int

  //***** Order related *****
  override def compare(that: Permutation): Int =
    indices.map(i => (apply(i), that(i))).find(p => p._1 != p._2) match {
      case None => 0
      case Some((x, y)) =>
        if(x < y) -1
        else       1
    }

  def next: Option[Permutation] = degree match {
    case 1 => None
    case _ =>
      val to = toSeq
      to.sliding(2).toSeq.lastIndexWhere(p => p.head < p(1)) match {
        case -1 => None
        case i =>  //  case for args: to = Seq(0, 2, 4, 1, 5, 3), i = 3 (1 < 5)
          val a = apply(i)  // a = 1
          val j = to.lastIndexWhere(p => a < p)  // j = 5 (1 < 3)
          val swapped = swap(to, i, j)  // swapped = Seq(0, 2, 4, 3, 5, 1)

          val (first, second) = swapped.splitAt(i+1)  // first = Seq(0, 2, 4, 3), second = Seq(5, 1)
          val newTo = first ++ second.reverse  // newTo = Seq(0, 2, 4, 3, 1, 5)
          Some(new Permutation.SeqPermutation(newTo))
      }
  }

  //***** Methods of Order *****
  /** Return the sequence number in the lexicographic order */
  def sequenceNumber: Int = sequenceNumberInFactorialRepresentation.toInt

  def sequenceNumberInFactorialRepresentation: FactorialRepresentation = {
    @tailrec
    def sequenceNumber(fact: List[Int], permuted: Vector[Int]): List[Int] =
      permuted.isEmpty match {
        case true  => fact
        case false =>
          val (init, last) = (permuted.init, permuted.last)
          val n = last - init.count(_ < last)
          sequenceNumber(n::fact, init)
      }

    val fact = sequenceNumber(Nil, toSeq.toVector.init)  // The last place (place of 0!) is removed

//    // another implementation
//    @tailrec
//    def sequenceNumber(fact: Vector[Int], permuted: List[Int]): List[Int] =
//      permuted.length match {
//        case 1 => fact  // The last place (place of 0!) is ignored
//        case _ =>
//          val a = permuted.head
//          val list = permuted.map(i => if(i > a) i-1 else i)
//          sequenceNumber(fact :: list.head, list.tail)
//      }
//
//    val fact = sequenceNumber(Vector(), toSeq).toList

    FactorialRepresentation(fact)
  }

  //***** Type converters *****
  def toSeq: Seq[Int] = indices.map(apply(_))
  def toMap: Map[Int, Int] = indices.map{i => (i, apply(i))}.toMap

  //***** Methods of Any *****
  override def equals(other: Any): Boolean = other match {
    case that: Permutation =>
      that.canEqual(this) &&
        degree == that.degree &&
          toSeq == that.toSeq  // indices.forall(i => apply(i) == that.apply(i))
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Permutation]

  override def hashCode: Int = (degree +: apply(indices)).hashCode

  override def toString: String = toSeq.mkString("[", " ", "]")
}

object Permutation{
  //***** n! and nPr *****
  def permutationCount[I: Integral](n: I): I = factorial(n)
  def permutationCount[I: Integral](n: I, r: I): I = PartialPermutation.permutationCount(n, r)

  //***** Concrete class of Permutation *****
  private[Permutation]
  abstract class AbstractSeqPermutation(val permuted: Seq[Int]) extends Permutation{

    override def indices: Range = permuted.indices
    override def degree: Int = permuted.length

    override def apply(i: Int): Int = permuted(i)
    override def indexOf(i: Int): Int = permuted.indexOf(i)

    override def toSeq: Seq[Int] = permuted

    override def toString: String = permuted.mkString("[", " ", "]")
  }

  /**
    * The constructor of this class is private not to validate argument Seq.
    * The validation is done in apply() factory methods.
    */
  private[Permutation]
  class SeqPermutation(permuted: Seq[Int]) extends AbstractSeqPermutation(permuted){

    override lazy val sgn: Int = {  // (024153)
      @tailrec
      def calculateSign(sign: Int, permuted: Seq[Int], n: Int): Int = permuted match {
        case _ +: Nil => sign
        case head +: _ if head == n => calculateSign(sign, permuted.tail, n+1)
        case _ =>  // case for args: list = Seq(2, 4, 1, 5, 3), n = 1
          val i = permuted.indexOf(n)  // i = 2

//          val swapped = swap(permuted, 0, i) // swapped = Seq(1, 4, 2, 5, 3)
//          val newTowards = swapped.tail
          val newTowards = permuted.updated(i, permuted.head).tail  // newTowards = Seq(4, 2, 5, 3)

          calculateSign(-sign, newTowards, n+1)
      }

      calculateSign(1, permuted.toVector, 0)
    }
  }

  def identity(deg: Int): Permutation = new Permutation {
    override def degree: Int = deg
    override def toSeq: Seq[Int] = indices
    override def apply(i: Int): Int = i
    override def indexOf(i: Int): Int = i
    override def *(p: Permutation): Permutation = p
    override def inverse: Permutation = this
    override def sgn: Int = 1
    override def toString: String = indices.mkString("[", " ", "]")
  }

  //***** apply constructors *****
  // apply constructors validate the arguments as well as instantiation
  def apply(permuted: LinearSeq[Int]): Permutation = {
    validatePermutationArguments(permuted)
    new SeqPermutation(permuted)
  }

  def apply(permuted: IndexedSeq[Int]): Permutation = {
    validatePermutationArguments(permuted)
    new SeqPermutation(permuted)
  }

  private def validatePermutationArguments(permuted: Seq[Int]): Unit = {
    val degree = permuted.length

    require(degree > 0, "The degree of permutation must be positive: " + degree)
    require(permuted.indices.forall(i => permuted.count(_ == i) == 1),
      "The constructor arguments of permutation must contain all integers once from 0 until "+ degree)
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
    val permuted = manager(args)
    validatePermutationArguments(permuted)
    new SeqPermutation(permuted)
  }

  //***** Some implementations of generating all permutations *****
  private[Permutation] def signed(permuted: Seq[Int], sign: Int): Permutation =
    new AbstractSeqPermutation(permuted){
      override def sgn: Int = sign
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

  def allPermutations[E](arg: Seq[E]): Seq[Seq[E]] = arg.length match {
    case 1 => Seq(arg)
    case 2 => Seq(arg, arg.reverse)
    case 3 => allPermutationsOfOrder3.map(_(arg))
    case _ => allPermutations1(arg)
  }

  def allPermutations(s: String): Seq[String] = allPermutations(s: Seq[Char]).map(_.mkString)

  def allPermutations(degree: Int, calculateSign: Boolean = false): Seq[Permutation] =
    degree match {
      case 1 => allPermutationsOfOrder1
      case 2 => allPermutationsOfOrder2
      case 3 => allPermutationsOfOrder3
      case _ =>
        if(calculateSign)
          allPermutationsWithSign1(degree)
        else
          allPermutations1(degree)
    }

  trait PermutationBuilder[E, B <: PermutationBuilder[E, B]]
      extends CombinatorialBuilder[E, B]{
    def permuted: Seq[E]
  }

  /**
    * Implementation 1.1 (generate permutations of any seq)
    * The result is in lexicographic order if the argument seq is ascendant.
    */
  def allPermutations1[E](arg: Seq[E]): Seq[Seq[E]] = {
    require(arg.nonEmpty)

    case class Builder11[F](permuted: Vector[F], available: Vector[F])
        extends PermutationBuilder[F, Builder11[F]]{

      override def nextGeneration: Seq[Builder11[F]] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder11[F]], i: Int): Seq[Builder11[F]] =
          i match {
            case -1 => accum
            case _  =>
              val newPermuted = permuted :+ available(i)
              val newAvailable = removeAt(available, i)
              val newBuilder = Builder11(newPermuted, newAvailable)
              nextGeneration(newBuilder +: accum, i-1)
          }

        nextGeneration(Nil, available.length-1)
      }
    }

    val init = Builder11(Vector(), arg.toVector)
    generateCombinatorial(init, arg.length).map(_.permuted)
  }

  /** Implementation 1.2 (generate Permutation objects in lexicographic order) */
  private[integral]
  def allPermutations1(degree: Int): Seq[Permutation] =
    allPermutations1(0 until degree).map(new SeqPermutation(_))
//  {
//    require(degree > 0)
//
//    case class Builder12(permuted: Vector[Int], available: Vector[Int])
//      extends PermutationBuilder[Int, Builder12]{
//
//      override def nextGeneration: Seq[Builder12] = {
//        @tailrec
//        def nextGeneration(accum: Seq[Builder12], i: Int): Seq[Builder12] =
//          i match {
//            case -1 => accum
//            case _  =>
//              val newPermuted = permuted :+ available(i)
//              val newAvailable = removeAt(available, i)
//              val newBuilder = Builder12(newPermuted, newAvailable)
//              nextGeneration(newBuilder +: accum, i-1)
//          }
//
//        nextGeneration(Nil, available.length-1)
//      }
//
//      def toPermutation = new SeqPermutation(permuted)
//    }
//
//    val init = Builder12(Vector(), (0 until degree).toVector)
//    generateCombinatorial(init, degree).map(_.toPermutation)
//  }

  /** Implementation 1.3 (generate Permutation objects in lexicographic order with permutation sign pre-calculated) */
  private[integral]
  def allPermutationsWithSign1(degree: Int): Seq[Permutation] = {
    require(degree > 0)

    case class Builder13(permuted: Vector[Int], available: Vector[Int], sign: Int)
        extends PermutationBuilder[Int, Builder13]{

      override def nextGeneration: Seq[Builder13] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder13], i: Int): Seq[Builder13] =
          i match {
            case -1 => accum
            case _  =>
              val newPermuted = permuted :+ available(i)
              val newAvailable = removeAt(available, i)
              val newSign = if(i % 2 == 0) sign else -sign
              val newBuilder = Builder13(newPermuted, newAvailable, newSign)
              nextGeneration(newBuilder +: accum, i-1)
          }

        nextGeneration(Nil, available.length-1)
      }

      def toPermutation = signed(permuted, sign)
    }

    val init = Builder13(Vector(), (0 until degree).toVector, 1)
    generateCombinatorial(init, degree).map(_.toPermutation)
  }

  /** Implementation 2.1 (generate permutations of any seq) */
  private[integral]
  def allPermutations2[E](arg: Seq[E]): Seq[Seq[E]] = {
    require(arg.nonEmpty)

    case class Builder21[F](permuted: Vector[F], available: Seq[F])
        extends PermutationBuilder[F, Builder21[F]]{

      override def nextGeneration: Seq[Builder21[F]] = {
        (permuted.length to 0 by -1).map{ i =>
          val newPermuted = insertAt(permuted, i, available.head)
          Builder21(newPermuted, available.tail)
        }
      }
    }

    val init = Builder21(Vector(), arg)
    generateCombinatorial(init, arg.length).map(_.permuted)
  }

  /** Implementation 2.2 (generate Permutation objects) */
  private[integral]
  def allPermutations2(degree: Int): Seq[Permutation] =
    allPermutations2(0 until degree).map(permuted => new SeqPermutation(permuted))
//  {
//    require(degree > 0)
//
//    case class Builder22(permuted: Seq[Int])
//      extends PermutationBuilder[Int, Builder22]{
//
//      override def nextGeneration: Seq[Builder22] = {
//        val degree = permuted.length
//        (degree to 0 by -1).map{ i =>
//          val newPermuted = insertAt(permuted, i, degree)
//          Builder22(newPermuted)
//        }
//      }
//
//      def toPermutation: Permutation = new SeqPermutation(permuted)
//    }
//
//    val init = Builder22(Vector())
//    generateCombinatorial(init, degree).map(_.toPermutation)
//  }

  /** Implementation 2.3 (generate Permutation objects with permutation sign pre-calculated) */
  private[integral]
  def allPermutationsWithSign2(degree: Int): Seq[Permutation] = {
    require(degree > 0)

    case class Builder23(permuted: Seq[Int], sign: Int)
        extends PermutationBuilder[Int, Builder23]{

      override def nextGeneration: Seq[Builder23] = {
        val degree = permuted.length
        (degree to 0 by -1).map{ i =>
          val newPermuted = insertAt(permuted, i, degree)
          val newSign = if((degree-i) % 2 == 0) sign else -sign
          Builder23(newPermuted, newSign)
        }
      }

      def toPermutation: Permutation = signed(permuted, sign)
    }

    val init = Builder23(Vector(), 1)
    generateCombinatorial(init, degree).map(_.toPermutation)
  }

  /** Implementation 3 (generate all permutation of any seq with swapping elements) */
  def allPermutations3[E](arg: Seq[E]): Seq[Seq[E]] = {
    require(arg.nonEmpty)

    @tailrec
    def generatePermutations(stream: Stream[Vector[E]], i: Int): Stream[Vector[E]] = i match {
      case -1 => stream
      case _ =>
        val newStream = stream.flatMap{ v =>
          @tailrec
          def nextGeneration(accum: Vector[Vector[E]], j: Int): Vector[Vector[E]] = j match {
            case -1 => accum
            case _  =>
              val next = swap(v, i, j)
              nextGeneration(accum :+ next, j-1)
          }

          nextGeneration(Vector(), i)
        }
        generatePermutations(newStream, i-1)
    }

    generatePermutations(Stream(arg.toVector), arg.length-1)
  }

  //***** other permutation generators *****
  def parityPermutations[E](arg: Seq[E], parity: Int): Seq[Seq[E]] = {
    require(arg.length > 1)

    case class Builder[F](permuted: Vector[F], available: Vector[F], sign: Int)
      extends PermutationBuilder[F, Builder[F]]{

      override def nextGeneration: Seq[Builder[F]] = {
        @tailrec
        def nextGeneration(accum: Seq[Builder[F]], i: Int): Seq[Builder[F]] =
          i match {
            case -1 => accum
            case _  =>
              val newPermuted = permuted :+ available(i)
              val newAvailable = removeAt(available, i)
              val newSign = if(i % 2 == 0) sign else -sign
              val newBuilder = Builder(newPermuted, newAvailable, newSign)
              nextGeneration(newBuilder +: accum, i-1)
          }

        nextGeneration(Nil, available.length-1)
      }
    }

    val init = Builder(Vector(), arg.toVector, 1)

    generateCombinatorial(init, arg.length-2).map{
      case b if b.sign == parity =>
        b.permuted ++: b.available
      case b =>
        b.permuted ++: b.available.reverse
    }
  }

  def evenPermutations[E](arg: Seq[E]): Seq[Seq[E]] = arg.length match {
    case 1 => Seq(arg)
    case _ => parityPermutations(arg, 1)
  }

  def evenPermutations(degree: Int): Seq[Permutation] =
    evenPermutations(0 until degree).map(signed(_, 1))

  def oddPermutations[E](arg: Seq[E]): Seq[Seq[E]] = arg.length match {
    case 1 => Nil
    case _ => parityPermutations (arg, -1)
  }

  def oddPermutations(degree: Int): Seq[Permutation] =
    oddPermutations(0 until degree).map(signed(_, -1))

  def derangements[E](arg: Seq[E]): Seq[Seq[E]] =
    derangements(arg.length).map(_(arg))

  def derangements(degree: Int): Seq[Permutation] = {
    require(degree > 0)

    case class Builder(permuted: Vector[Int], available: Vector[Int], n: Int)
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
                val newPermuted = permuted :+ entry
                val newAvailable = removeAt(available, i)
                val newBuilder = Builder(newPermuted, newAvailable, n+1)
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
        generateCombinatorial(init, degree).map(b => new SeqPermutation(b.permuted))
    }
  }

  //***** Sequence number in lexicographic order *****
  def nthPermutation(n: Int, degree: Int): Permutation =
    nthPermutation(FactorialRepresentation.fromInt(n), degree)

  def nthPermutation(n: FactorialRepresentation, degree: Int): Permutation = {
    @tailrec
    def nthPermutation(permuted: Vector[Int], rest: Seq[Int], fact: Seq[Int]): Vector[Int] = fact match {
      case Nil => permuted :+ rest.head
      case 0 +: tail =>
        nthPermutation(permuted :+ rest.head, rest.tail, tail)
      case head +: tail =>
        // case for permuted = Vector(), rest = Seq(0, 1, 2, 3, 4, 5), fact = Seq(3, 1, 0, 2, 1)
        nthPermutation(permuted :+ rest(head), removeAt(rest, head), tail)
          // next call arg: permuted = Vector(3), rest = Seq(0, 1, 2, 4, 5), fact = Seq(1, 0, 2, 1)
    }

    val cs = n.coefficientsAsNthOrderInDescendant(degree-1)
    val permuted = nthPermutation(Vector(), 0 until degree, cs).toList

//    @tailrec
//    def nthPermutation(permuted: List[Int], fact: Vector[Int]): List[Int] = fact.isEmpty match {
//      case true  => permuted
//      case false =>
//        val a = fact.last
//        val newTowards = a :: permuted.map(i => if(i >= a) i+1 else i)
//        nthPermutation(newTowards, fact.init)
//    }
//
//    val permuted = nthPermutation(Nil, n.coefficientsAsNthOrderInDescendant(degree-1).toVector :+ 0)

    new SeqPermutation(permuted)
  }
}