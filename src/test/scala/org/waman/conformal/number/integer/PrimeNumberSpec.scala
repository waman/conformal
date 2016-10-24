package org.waman.conformal.number.integer

import org.waman.conformal.ConformalCustomSpec

class PrimeNumberSpec extends ConformalCustomSpec{

  val primes = Seq(2, 3, 5, 7, 11, 13, 17, 19, 23)
  val conversions = Table("n", 0, 1, 2, 3, 4, 5, 6, 7, 8)

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
      val sut = PrimeNumber.stream[Int].take(9)
      __Verify__
      sut should equal (expected)
    }
  }

  "stream(I) method should" - {

    "return the sequence of prime numbers starting at the n-th integer with ascending" in {
      __SetUp__
      val expected = primes.drop(3)
      __Exercise__
      val sut = PrimeNumber.streamFrom[Int](3).take(6)
      __Verify__
      sut should equal (expected)
    }
  }
}
