package org.waman.conformal.random

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

    "test" in {
      val nSamples = 10000
      val n = 3
      val zero: Seq[Double] = Seq.fill(n)(0.0)
      val init = Summary(0, zero, zero)

      val samples: Seq[Seq[Double]] = Stream.fill(nSamples)(Random.newPointOnSphere[Double](n))
      val result: Summary = samples.foldLeft(init)( (s, data) => s.append(data) )

      println(result)
    }
  }
}
