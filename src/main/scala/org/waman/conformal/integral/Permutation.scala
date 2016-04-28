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

  def andThen(p: Permutation): Permutation = {
    require(degree == p.degree)
    new Permutation(p(to))
  }

  def *(p: Permutation): Permutation = p andThen this

  lazy val sgn: Int = {  // (021453)
    def calculateSign(list: List[Int], n: Int): Int = list match {
      case _ :: Nil => 1
      case _ if list.head == n => calculateSign(list.tail, n+1)
      case _ =>  // args: list = List(2, 1, 4, 5, 3), n = 1
        val i = list.indexOf(n)  // i = 1

        val newList = exchangeHeadAndGetTail(list, i)
          // newList = List(1, 2, 4, 5, 3) ( = List(1) ::: 2 :: List(4, 5, 3))
        -calculateSign(newList, n+1)
    }

    def exchangeHeadAndGetTail(list: List[Int], i: Int): List[Int] =
      list.slice(1, i) ::: list.head :: list.slice(i+1, list.length)

    calculateSign(to, 0)
  }

  override def toString: String = to.mkString("(", " ", ")")
}
