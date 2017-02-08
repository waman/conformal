package org.waman.conformal.random

import spire.math._
import spire.random.{Dist, Generator}
import spire.implicits._
import org.waman.conformal.number.nroot
import spire.algebra.Trig

object Random {

  // Note that the returned value is always positive.
  private def cosFrom[A: Fractional](s: A): A = sqrt(1 - s*s)
  private def sinFrom[A: Fractional](c: A): A = cosFrom(c)

  private def nextPhi[A](implicit a: Fractional[A],
                         trig: Trig[A],
                         rng: Generator,
                         next: Dist[A]): A = 2 * pi * rng.next(next)

  def newPointOnSphere[A](n: Int, radius: A)
                         (implicit a: Fractional[A],
                          trig: Trig[A],
                          rng: Generator,
                          next: Dist[A]): Seq[A] = newPointOnSphere(n).map(_ * radius)

  /**
    * Return a random point on (n-1)-dimensional sphere S^{n-1}^.
    * The returned Seq has length n.
    */
  def newPointOnSphere[A](n: Int)(implicit a: Fractional[A],
                                  trig: Trig[A],
                                  rng: Generator,
                                  next: Dist[A]): Seq[A] =
    n match {
      case 1 => if(rng.nextBoolean) Seq(1) else Seq(-1)
      case 2 => newPointOnCircumference
      case 3 => newPointOnSphere
      case _ => newPointOnHyperSphere(n)
    }

  private def newPointOnCircumference[A](implicit a: Fractional[A],
                                         trig: Trig[A],
                                         rng: Generator,
                                         next: Dist[A]): Seq[A] = {
    val phi = nextPhi
    Seq(cos(phi), sin(phi))
  }

  def newPointOnSphere[A](implicit a: Fractional[A],
                          trig: Trig[A],
                          rng: Generator,
                          next: Dist[A]): Seq[A] = {
    val cosTheta = 2 * rng.next - 1
    val sinTheta = sinFrom(cosTheta)
    val phi = nextPhi
    Seq(sinTheta * cos(phi), sinTheta * sin(phi), cosTheta)
  }

  def newPointOnHyperSphere[A](n: Int)(implicit a: Fractional[A],
                                       trig: Trig[A],
                                       rng: Generator,
                                       next: Dist[A]): Seq[A] = {
    val p = newPointOnSphere(n-2)

    val sinTheta = nroot(rng.next, n-2)
    val cosTheta = cosFrom(sinTheta)
    val phi = nextPhi

    (cosTheta*cos(phi)) +: (cosTheta*sin(phi)) +: p.map(_ * sinTheta)
  }

  def newPointInBall[A](n: Int, radius: A)(implicit a: Fractional[A],
                                           trig: Trig[A],
                                           rng: Generator,
                                           next: Dist[A]): Seq[A] =
    newPointInBall(n).map(_ * radius)

  /**
    * Return a random point in n-dimensional ball B^n^.
    * The returned Seq has length n.
    */
  def newPointInBall[A](n: Int)(implicit a: Fractional[A],
                                trig: Trig[A],
                                rng: Generator,
                                next: Dist[A]): Seq[A] = {
    val scale = nroot(rng.next, n)
    newPointOnSphere(n).map(_ * scale)
  }
}
