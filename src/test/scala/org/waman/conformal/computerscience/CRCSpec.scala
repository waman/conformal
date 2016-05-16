package org.waman.conformal.computerscience

import java.nio.file.{Files, Paths}
import java_algorithm.computerscience.{check => jalgo}

import org.waman.conformal.ConformalCustomSpec

class CRCSpec extends ConformalCustomSpec{

  "sumToChecksum reference" ignore {
    println(Integer.toBinaryString(-50))
    println(Integer.toBinaryString(((-50) ^ CRC16.mask) ^ CRC16.mask))
    println(Integer.toBinaryString(~(~(-50) & CRC16.mask)))
  }

  "CRC object" - {

    "getMask() method should" - {

      "return mask" in {
        val conversions =
          Table(
            ("bit", "expected"),
            (8 , 0xFF),
            (16, 0xFFFF),
            (24, 0xFFFFFF),
            (32, 0xFFFFFFFF)
          )

        forAll(conversions){ (bit: Int, expected: Int) =>
          __Exercise__
          val sut = CRC.getMask(bit)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "getDivisor() method should" - {

      "create divisor Int value from Seq[Int]" in {
        val conversions =
          Table(
            ("powers", "expected"),
            (Seq(16, 12, 5, 0), 0x1021),
            (Seq(32, 26, 23, 22, 16, 12, 11, 10, 8, 7, 5, 4, 2, 1, 0), 0x04C11DB7)
          )

        forAll(conversions){ (powers: Seq[Int], expected: Int) =>
          __Exercise__
          val sut = CRC.getDivisor(powers)
          __Verify__
          sut should equal (expected)
        }
      }
    }

    "reverseBitOrder() method should" - {

      "return the binary-reversed integer" in {
        val conversions =
          Table(
            ("arg", "expected", "bit"),
            (0x1021, 0x8408, 16),
            (0x8408, 0x1021, 16),
            (0x04C11DB7, 0xEDB88320, 32),
            (0xEDB88320, 0x04C11DB7, 32)
          )

        forAll(conversions) { (arg: Int, expected: Int, bit: Int) =>
          __Exercise__
          val sut = CRC.reverseBits(arg, bit)
          __Verify__
          sut should equal(expected)
        }
      }
    }

    "toInt(Byte, Byte)" in{
      __SetUp__
      val b0: Byte = 'a'
      val b1: Byte = 'b'
      val expected = Integer.valueOf("0110000101100010", 2)
      __Exercise__
      val sut = CRC.toInt(Seq(b0, b1))
      println(sut)
      println(Integer.toBinaryString(sut))
      __Verify__
      sut should equal (expected)
    }
  }

  "The objects implementing CRC trait should" - {

    "have the proper value of the divisor" in {
      val conversions =
        Table(
          ("crc" , "divisor"),
          (CRC16 , 0x1021),
          (CRC16R, 0x8408),
          (CRC32 , 0x04C11DB7),
          (CRC32R, 0xEDB88320)
        )

      forAll(conversions){ (crc: CRC, divisor: Int) =>
        __Exercise__
        val sut = crc.divisor
        __Verify__
        sut should equal (divisor)
      }
    }
  }

  val input = ('a' to 'z').map(_.toByte)

  "16bit" - {

    "CRC16" - {

      val conversions =
        Table("CRC16", CRC16, CRC16T)

      "calculateChecksum(Seq[Byte]) method should" - {

        "calculate the CRC16 checksum" in {
          __SetUp__
          val expected = jalgo.CRC16.method1(input.toArray)
          // = 44061 = 0xAC1D = "10101100_00011101"

          forAll(conversions){ crc16: CRC with Bit16 with NormalBitOrder =>
            __Exercise__
            val sut = crc16.calculateChecksum(input)
            __Verify__
            sut should equal (expected)
          }
        }
      }

      "check(Seq[Byte], Int) method should" - {

        "calculate CRC16 checksum by the first arg and evaluate equality to the second arg" in {
          __SetUp__
          val checksum = jalgo.CRC16.method1(input.toArray)

          forAll(conversions){ crc16: CRC with Bit16 with NormalBitOrder =>
            __Exercise__
            val sut = crc16.check(input, checksum)
            __Verify__
            sut should equal (true)
          }
        }
      }

      "check(Seq[Byte]) method should" - {

        "calculate CRC16 checksum by 0-to-(n-2) init and evaluate equality to the last 2 bytes as checksum" in {
          __SetUp__
          val checkedInput: Seq[Byte] = input.toVector :+ 0xAC.toByte :+ 0x1D.toByte

          forAll(conversions){ crc16: CRC with Bit16 with NormalBitOrder =>
            __Exercise__
            val sut = crc16.check(checkedInput)
            __Verify__
            sut should equal (true)
          }
        }
      }
    }

    "CRC16R" - {

      val conversions = Table("CRC16R", CRC16R, CRC16RT)

      "calculateChecksum(Seq[Byte]) method should" - {

        "calculate the CRC16 checksum with reversed bit order" in {
          __SetUp__
          val expected = jalgo.CRC16.method2(input.toArray)
          println(Integer.toHexString(expected))
          // = 3395 = 0x0D43 = "00001101_01000011"

          forAll(conversions){ crc16r: CRC with Bit16 with ReversedBitOrder =>
            __Exercise__
            val sut = crc16r.calculateChecksum(input)
            __Verify__
            sut should equal (expected)
          }
        }
      }

      "check(Seq[Byte], int) method should" - {

        "check with the CRC16R checksum" in {
          __SetUp__
          val checksum = jalgo.CRC16.method2(input.toArray)

          forAll(conversions){ crc16r: CRC with Bit16 with ReversedBitOrder =>
            __Exercise__
            val sut = crc16r.check(input, checksum)
            __Verify__
            sut should equal (true)
          }
        }
      }

      "check(Seq[Byte]) method should" - {

        "calculate CRC16R checksum by 0-to-(n-2) init and evaluate equality to the last 2 bytes as checksum" in {
          __SetUp__
          val checkedInput: Seq[Byte] = input.toVector :+ 0x43.toByte :+ 0x0D.toByte

          forAll(conversions){ crc16r: CRC with Bit16 with ReversedBitOrder =>
            __Exercise__
            val sut = crc16r.check(checkedInput)
            __Verify__
            sut should equal (true)
          }
        }
      }
    }
  }

  "32bit" - {

    "CRC32" - {

      val conversions = Table("CRC32", CRC32, CRC32T)

      "calculateChecksum(Seq[Byte]) method should" - {

        "calculate the CRC32 checksum" in {
          __SetUp__
          val expected = jalgo.CRC32.method1(input.toArray)
          // = 2009043862 = 0x77BF9396 = "01110111_10111111_10010011_10010110"

          forAll(conversions){ crc32: CRC with Bit32 with NormalBitOrder =>
            __Exercise__
            val sut = crc32.calculateChecksum(input)
            __Verify__
            sut should equal (expected)
          }
        }
      }

      "check(Seq[Byte]) method should" - {

        "check with the CRC32 checksum" in {
          __SetUp__
          val checksum = jalgo.CRC32.method1(input.toArray)

          forAll(conversions){ crc32: CRC with Bit32 with NormalBitOrder =>
            __Exercise__
            val sut = crc32.check(input, checksum)
            __Verify__
            sut should equal (true)
          }
        }
      }

      "check(Seq[Byte]) method should" - {

        "calculate CRC32 checksum by 0-to-(n-2) init and evaluate equality to the last 2 bytes as checksum" in {
          __SetUp__
          val checkedInput: Seq[Byte] =
            input.toVector :+ 0x77.toByte :+ 0xBF.toByte :+ 0x93.toByte :+ 0x96.toByte

          forAll(conversions){ crc32: CRC with Bit32 with NormalBitOrder =>
            __Exercise__
            val sut = crc32.check(checkedInput)
            __Verify__
            sut should equal (true)
          }
        }
      }
    }

    "CRC32R" - {

      val conversions = Table("CRC32R", CRC32R, CRC32RT)

      "calculateChecksum(Seq[Byte]) method should" - {

        "calculate the CRC32 checksum with reversed bit order" in {
          __SetUp__
          val expected = jalgo.CRC32.method2(input.toArray)
          // = 1277644989 = 0x4C2750BD = "01001100_00100111_01010000_10111101"
          println(Integer.toHexString(expected))

          forAll(conversions){ crc32r: CRC with Bit32 with ReversedBitOrder =>
            __Exercise__
            val sut = crc32r.calculateChecksum(input)
            __Verify__
            sut should equal (expected)
          }
        }
      }

      "check(Seq[Byte]) method should" - {

        "check with the CRC32R checksum" in {
          __SetUp__
          val checksum = jalgo.CRC32.method2(input.toArray)

          forAll(conversions){ crc32r: CRC with Bit32 with ReversedBitOrder =>
            __Exercise__
            val sut = crc32r.check(input, checksum)
            __Verify__
            sut should equal (true)
          }
        }
      }

      "check(Seq[Byte]) method should" - {

        "calculate CRC32R checksum by 0-to-(n-2) init and evaluate equality to the last 4 bytes as checksum" in {
          __SetUp__
          val checkedInput: Seq[Byte] =
            input.toVector :+ 0xBD.toByte :+ 0x50.toByte :+ 0x27.toByte :+ 0x4C.toByte

          forAll(conversions){ crc32r: CRC with Bit32 with ReversedBitOrder =>
            __Exercise__
            val sut = crc32r.check(checkedInput)
            __Verify__
            sut should equal (true)
          }
        }
      }

      "calculateChecksum(Path) method should" - {

        "calculate the CRC32 checksum with reversed bit order and table cache" in {
          __SetUp__
          val fileName = "build.sbt"
          val path = Paths.get(fileName)

          val expected1 = jalgo.GetCRC.ofFile(fileName)

          val bytes = Files.readAllBytes(Paths.get(fileName))
          val crc32 = new java.util.zip.CRC32()
          crc32.update(bytes)
          val expected2 = crc32.getValue.toInt

          forAll(conversions){ crc32r: CRC with Bit32 with ReversedBitOrder =>
            __Exercise__
            val sut = crc32r.calculateChecksum(path)
            __Verify__
            sut should equal(expected1)
            sut should equal(expected2)
          }
        }
      }
    }
  }
}
