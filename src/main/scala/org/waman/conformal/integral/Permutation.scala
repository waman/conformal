package org.waman.conformal.integral

case class Permutation(to: List[Int]) {

  def this(to: Int*) = this(to.toList)

  val degree: Int = to.length

  def apply[E](objs: List[E]): List[E] = {
    require(objs.size == to.size)

    def construct(n: Int): List[E] = n match {
      case this.degree => Nil
      case _ => objs(to(n)) :: construct(n+1)
    }

    construct(0)
  }

  lazy val sgn: Int = sign

  lazy val sign: Int = {  // (021453)
    def calculateSign(list: List[Int], n: Int): Int = list match {
      case _ :: Nil => 1
      case _ if list.head == n => calculateSign(list.tail, n+1)
      case _ =>  // args: list = List(2, 1, 4, 5, 3), n = 1
        val i = list.indexOf(n)  // i = 1
        val newList = list.slice(1, i) ::: list.head :: list.slice(i+1, list.length)
          // newList = List(1, 2, 4, 5, 3) ( = List(1) ::: 2 :: List(4, 5, 3))
        -calculateSign(newList, n+1)
    }

    calculateSign(to, 0)
  }

  override def toString: String =
    to.map(_.toString).map{
      case s if s.length == 1 => s
      case s if s.length >= 2 => "["+s+"]"
    }.mkString("(", "", ")")
}
