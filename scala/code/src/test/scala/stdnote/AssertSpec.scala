package stdnote

import org.scalatest.{FunSuite, Matchers}

/**
 * Created by Liam.M(엄익훈) on 7/27/16.
 */
class AssertSpec extends FunSuite with Matchers{

  test("true") {
    true shouldBe true
  }

  test("equality") {
    val v1 = 4
    v1 shouldEqual 4
  }

  test("assert") {
    assert(2 == 1 + 1)
  }
}
