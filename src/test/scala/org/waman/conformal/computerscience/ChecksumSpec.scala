package org.waman.conformal.computerscience

import org.waman.conformal.ConformalCustomSpec

class ChecksumSpec extends ConformalCustomSpec{

  "SimpleChecksum" - {

    "appendCheckSum() method should" - {

      "calculate the checksum" in {
        __SetUp__
        val input = ('a' to 'z').map(_.toByte)
        __Exercise__
        val checked = SimpleChecksum.appendChecksum(input)
        __Verify__
        SimpleChecksum.check(input) should be (false)
        SimpleChecksum.check(checked) should be (true)
      }
    }
  }
}
