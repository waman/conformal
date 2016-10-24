package org.waman.conformal.number.integer.combinatorics

import scala.annotation.tailrec

import org.waman.conformal._

object PermutationNumber{

  def encode(p: Permutation): Long = {
    @tailrec
    def encode(code: Long, seq: Vector[Int], i: Int): Long = i match {
      case 0 => code
      case _ =>
        val j = indexOfMax(seq)
        encode(code * (i + 1L) + j, swap(seq, i, j).init, i-1)
    }

    encode(0L, p.suffices.toVector, p.degree-1)
  }

  def decode(code: Long, degree: Int): Permutation = {
    @tailrec
    def decode(suffices: Vector[Int], code: Long, i: Int): Vector[Int] = i match {
      case _ if i == degree => suffices
      case _ =>
        val div = i+1
        val j = code % div
        decode(swap(suffices, i, j.toInt), code / div, i+1)
    }

    val suffices = decode((0 until degree).toVector, code, 1)
    new Permutation.SeqPermutation(suffices)
  }
}