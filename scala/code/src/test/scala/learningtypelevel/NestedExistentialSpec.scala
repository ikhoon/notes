package learningtypelevel

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

/**
  * Created by ikhoon on 2016. 9. 4..
  */
class NestedExistentialSpec extends AnyWordSpec with Matchers {

  "nested existential" should {

    "badlength" in {
      import NestedExistential._
      badLength(PNil()) // 이것만 동작한다.
      assertDoesNotCompile("badLength(PCons(1, PNil()))") // 이건 컴파일되지 않는다.
    }

    "mlenLengthE" in {
      import NestedExistential._
      // 이건되지만.
      mlenLengthE(elists) shouldBe 6
      assertDoesNotCompile("mlenLengthTP(elists)") // 이건 컴파일되지 않는다.
    }
  }

}
