package learningtypelevel

import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by ikhoon on 2016. 8. 29..
  */
class MethodEquivalenceSpec extends WordSpec with Matchers {

  "method equivalence" should {
    "equivalent" in {
      import MethodEquivalence._
      val buf = ArrayBuffer(1, 2, 3, 4)
      copyToZeroE(buf)
      println(buf)
      buf shouldBe ArrayBuffer(1, 2, 3, 4, 1)
    }

    "drop" in {
      import MList._
      import MethodEquivalence._
      val nums = MCons(2, MCons(4, MNil())) : MCons { type T = Int }
      mdropFirstE(nums).uncons.get.head shouldBe 4
      mdropFirstEUsingP(nums).uncons.get.head shouldBe 3
    }

    "insanity" in {
      import MethodEquivalence._
      println(goshWhatIsThis1(10))
    }
  }

}
