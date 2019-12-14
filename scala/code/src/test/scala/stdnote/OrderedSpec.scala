package stdnote

import org.scalatest.{FunSuite, Matchers}

/**
  * Created by ikhoon on 07/04/2018.
  */
class OrderedSpec extends FunSuite with Matchers {

  // 값을 비교하고 싶다.


  case class Version(major: Int, minor: Int, patch: Int) extends Ordered[Version] {
    def compare(that: Version): Int =
      if(major > that.major) 1
      else if (major == that.major && minor >  that.minor) 1
      else if (major == that.major && minor == that.minor && patch >  that.patch) 1
      else if (major == that.major && minor == that.minor && patch == that.patch) 0
      else -1
  }

  test("version check") {
    Version(1, 1, 1) < Version(1, 1, 1) shouldBe false
    Version(1, 10, 1) > Version(0, 0, 1) shouldBe true
    Version(10, 9, 3) <= Version(0, 0, 1) shouldBe false
    Version(10, 9, 3) >= Version(0, 0, 1) shouldBe true
  }

}
