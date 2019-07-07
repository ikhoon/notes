package algonote

import java.util

import scala.collection.mutable
import scala.runtime.ScalaRunTime._

/**
  * https://leetcode.com/problems/course-schedule-iii/
  */
object CourseScheduleIII {

  def scheduleCourse(courses: Array[Array[Int]]): Int = {

    // 내가 포함이 되거나 내가 포함이 되지 않거나?
    // start, end
    val xs = maxCourses(courses.toList)
    xs.maxBy(_.size).size
  }
  def maxCourses(courses: List[Array[Int]]): List[List[Array[Int]]] = {
    println(s"courses = ${courses.map(x => stringOf(x))}")
    if(courses.length == 1) {
      List(courses)
    }
    else {
      var allCourses: mutable.Buffer[List[Array[Int]]] = mutable.Buffer()
      for (i <- courses.indices) {
        val buf = maxCourses(courses.take(i) ++ courses.drop(i + 1))
        println(s"buf = ${buf.map(_.map(stringOf)).mkString("\n")}")
        buf.foreach { cur =>
          if(!hasDuplicatedCourse(cur, courses(i))) {
            allCourses += courses(i) :: cur
          }
        }
      }
      println(s"allCourses = ${allCourses.map(_.map(stringOf)).mkString("\n")}")
      allCourses.toList
    }
  }

  def hasDuplicatedCourse(courses: List[Array[Int]], course: Array[Int]): Boolean = {
    // 시작시간이 나의 종료시간보다 작은거
    // 종료시간이 나의 시작시간보다 큰거
    //
    courses.exists(target => {
      target(1) > course(0) && target(1) < course(1) ||
      target(0) >= course(0) && target(1) <= course(1) ||
      target(0) > course(0) && target(0) < course(1) ||
      target(0) < course(0) && target(1) > course(1)
    })
  }
}
