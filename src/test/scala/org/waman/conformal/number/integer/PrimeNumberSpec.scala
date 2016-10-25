package org.waman.conformal.number.integer

import org.scalactic.ConversionCheckedTripleEquals
import org.waman.conformal.ConformalCustomSpec

class PrimeNumberSpec extends ConformalCustomSpec with ConversionCheckedTripleEquals{

  val primes = Seq(
    2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97)

  val conversions = Table(
    "n", 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,19, 20, 21, 22, 23, 24)

  "isPrime(I) method should" - {

    "return whether the specific integer is prime or not" in {
      forAll(conversions){ n: Int =>
        __SetUp__
        val expected = primes.contains(n)
        __Exercise__
        val sut = PrimeNumber.isPrime(n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "apply(I) method should" - {

    "return the n-th prime number" in {
      forAll(conversions){ n: Int =>
        __SetUp__
        val expected = primes(n)
        __Exercise__
        val sut = PrimeNumber(n)
        __Verify__
        sut should equal (expected)
      }
    }
  }

  "stream method should" - {

    "return the sequence of prime numbers with ascending" in {
      __SetUp__
      val expected = primes
      __Exercise__
      val sut = PrimeNumber.stream.take(25)
      __Verify__
      sut should equal (expected)
    }

    "return the sequence of 25 prime numbers under 100" in {
      __SetUp__
      val expected = primes
      __Exercise__
      val sut = PrimeNumber.stream.takeWhile(_ < 100)
      __Verify__
      sut should equal (expected)
    }
  }

  "streamFrom(Int) method should" - {

    "return the sequence of prime numbers starting at the n-th integer with ascending" in {
      __SetUp__
      val expected = primes.drop(3)
      __Exercise__
      val sut = PrimeNumber.streamFrom(3).take(22)
      __Verify__
      sut should equal (expected)
    }
  }

  "streamOf[I] method should" - {

    "return the sequence of prime numbers with ascending" in {
      __SetUp__
      val expected = primes.map(_.toLong)
      __Exercise__
      val sut = PrimeNumber.streamOf[Long].take(25)
      __Verify__
      sut should === (expected)
    }

    "return the sequence of 25 prime numbers under 100" in {
      __SetUp__
      val expected = primes.map(_.toLong)
      __Exercise__
      val sut = PrimeNumber.streamOf[Long].takeWhile(_ < 100)
      __Verify__
      sut should === (expected)
    }
  }

  "stream(I) method should" - {

    "return the sequence of prime numbers starting at the n-th integer with ascending" in {
      __SetUp__
      val expected = primes.drop(3).map(_.toLong)
      __Exercise__
      val sut = PrimeNumber.streamNthFrom[Long](3).take(22)
      __Verify__
      sut should === (expected)
    }
  }
}
