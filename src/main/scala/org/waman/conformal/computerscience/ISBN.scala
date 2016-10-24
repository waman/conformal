package org.waman.conformal.computerscience

trait ISBN extends Checksum[Int, Int]{

  val modulus: Int
  val digitLength: Int
  val weight: Seq[Int]

  override protected def sum(isbn: Seq[Int]): Int =
    isbn.zip(weight).map(n => n._1 * n._2).sum

  override protected def sumToChecksum(sum: Int): Int = modulus - (sum % modulus)

  def completeISBN(isbn: String): String = {
    val s = sum(ISBN.toIntSeq(isbn)) match {
      case 10 => "X" // for ISBN-10
      case d  => d.toString
    }

    if(isbn.endsWith("-")) isbn + s
    else                   isbn + "-" + s
  }

  protected override def testSum(sum: Int): Boolean = sum % modulus == 0

  def check(isbn: String): Boolean = {
    val seq = ISBN.toIntSeq(isbn)
    require(seq.length == digitLength, "ISBN number must have the length " + digitLength)
    check(seq)
  }
}

object ISBN{

  def toIntSeq(isbn: String): Seq[Int] = {
    isbn.collect {
      case 'x' | 'X' => 10  // for ISBN-10
      case c if c.isDigit => c.toString.toInt
    }
  }
}

object ISBN10 extends ISBN{

  override val modulus: Int = 11
  override val digitLength: Int = 10

  override val weight: Seq[Int] = Seq.iterate(10, 10)(i => i-1)

//  override def sum(isbn: Seq[Int]): Int = {
//    @tailrec
//    def sumWithWeight(accum: Vector[Int], ints: Seq[Int]): Vector[Int] = ints.length match {
//      case 0 => accum
//      case _ if accum.isEmpty =>
//        sumWithWeight(accum :+ ints.head, ints.tail)
//      case _ =>
//        sumWithWeight(accum :+ (accum.last + ints.head), ints.tail)
//    }
//
//    val sum1 = sumWithWeight(Vector(), isbn)
//    val sum2 = sumWithWeight(Vector(), sum1)
//    sum2.last
//  }
}

object ISBN13 extends ISBN{

  override val modulus: Int = 10
  override val digitLength: Int = 13

  override val weight: Seq[Int] = Stream.continually(List(1, 3)).flatten.take(13)
}
