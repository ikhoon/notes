package algonote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class AssignCookiesTest extends AnyFunSuite with Matchers {

  test("cookie 1") {
    val g = Array(1, 2, 3)
    val s = Array(1, 1)
    AssignCookies.findContentChildren(g, s) shouldBe 1
  }

  test("cookie 2") {
    val g = Array(10, 9, 8, 7)
    val s = Array(5, 6, 7, 8)
    AssignCookies.findContentChildren(g, s) shouldBe 2
  }

  test("cookie 3") {
    val g = Array(1, 2, 3)
    val s = Array(3)
    AssignCookies.findContentChildren(g, s) shouldBe 1

  }

}
