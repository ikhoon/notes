package learningtypelevel

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

/**
  * Created by ikhoon on 2016. 8. 29..
  */
class AuxSpec extends AnyWordSpec with Matchers {

  "What happen when I forgot a refinement" should {
    "misspell" in {
      assertDoesNotCompile("mdropFirstE2(MNil[Int]())")
      assertDoesNotCompile("mdropFirstE2(MCons[Int](42, MNil[Int]()))")
    }
  }

}
