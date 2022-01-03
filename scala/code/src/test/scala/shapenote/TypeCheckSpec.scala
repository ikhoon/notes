package shapenote

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Try

/**
  * Created by Liam.M(엄익훈) on 8/23/16.
  */
class TypeCheckSpec extends AnyWordSpec with Matchers {
  "Type Checking" should {
    "Testing for non-compilation" in {
      import shapeless.test.illTyped

      illTyped { """1 + 1 : Boolean""" }
//      illTyped { """1 + 1 : String"""}

      val matchedTypes = Try {
        assertTypeError("illTyped { \"val a: Int = 1\" }")
      }.isSuccess

      matchedTypes shouldBe true

      val misMatchedTypes = Try {
        assertTypeError("illTyped { \"val a: String = 1\" }")
      }.isSuccess

      misMatchedTypes shouldBe false

    }
  }

}
