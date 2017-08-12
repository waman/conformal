package org.waman.conformal.random


import spire.math._
import spire.implicits._
import org.waman.conformal.ConformalCustomSpec

class RandomSpec extends ConformalCustomSpec{

  "Random points on sphere" - {

    case class Summary(n: Int, s: Seq[Double], s2: Seq[Double]){
      def append(data: Seq[Double]): Summary =
        Summary(n+1, add(s, data), add(s2, data.map(d => d*d)))

      private def add(xs: Seq[Double], ys: Seq[Double]): Seq[Double] =
        (xs zip ys).map{ case (x, y) => x + y }

      lazy val means: Seq[Double] = s.map(_/n)
      lazy val variances: Seq[Double] = (s2 zip means).map{
        case (e2, e1) => e2/n - e1*e1
      }

      override def toString: String = {
        "Mean:\n  " + means.mkString("\n  ") +
        "\n\nVariance:\n  " + variances.mkString("\n  ")
      }
    }

    "test means and variances of components" in {
      val nSamples = 10000
      val n = 3
      val zero: Seq[Double] = Seq.fill(n)(0.0)
      val init = Summary(0, zero, zero)

      val samples: Seq[Seq[Double]] = Stream.fill(nSamples)(Random.newPointOnSphere[Double](n))
      val result: Summary = samples.foldLeft(init)( (s, data) => s.append(data) )

      println(result)
    }

    "test" in {

      case class Vector(elements: Seq[Double]){
        def -(w: Vector): Vector =
          Vector((elements zip w.elements).map{ case (x, y) => x - y })

        def norm: Double = sqrt(elements.map(_**2).sum)
      }

      val nSample = 1000
      val n = 3
      val dim = 2**n

      val zero = Seq.fill(dim)(0.0)
      val basisVectors: Seq[Vector] = (0 until dim).map{ i => Vector(zero.updated(i, 1.0)) }

      def dif(v: Vector): Double = basisVectors.map(basis => (v - basis).norm).sum

      val stream: Stream[Vector] =
        Stream.fill(nSample)(Random.newPointOnSphere[Double](dim)).map(Vector)
      val min = stream.map(dif).min
      println(min)
      println()

      println( "a_j = 1  : " + ((dim-1)*sqrt(2)) )
      println( "a_j = a_k: " + (sqrt(2) * dim * sqrt(1 - 1/(sqrt(2)**n))) )
    }
  }
}
