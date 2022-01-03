package algonote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CourseScheduleIIITest extends AnyFunSuite with Matchers {

  test("courses 1") {
    val xs = Array(Array(100, 200), Array(200, 1300), Array(1000, 1250), Array(2000, 3200))
    CourseScheduleIII.scheduleCourse(xs) shouldBe 3
  }
}
