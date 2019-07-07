package algonote

import org.scalatest.{FunSuite, Matchers}

class CourseScheduleIIITest extends FunSuite with Matchers {

  test("courses 1") {
    val xs = Array(Array(100, 200), Array(200, 1300), Array(1000, 1250), Array(2000, 3200))
    CourseScheduleIII.scheduleCourse(xs) shouldBe 3
  }
}
