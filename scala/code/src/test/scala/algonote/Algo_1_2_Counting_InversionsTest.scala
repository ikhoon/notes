package algonote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.io.Source

/**
  * Created by ikhoon on 01/07/2018.
  */
class Algo_1_2_Counting_InversionsTest extends AnyFunSuite with Matchers {

  import Algo_1_2_Counting_Inversions._
  test("sorted list") {
    val xs = Vector(1, 2, 3)
    val (sorted, count) = sortAndCount(xs)
    sorted shouldBe xs
    count shouldBe 0

  }

  test("reverse list") {
    val xs = Vector(3, 2, 1)
    val (sorted, count) = sortAndCount(xs)
    sorted shouldBe xs.reverse
    count shouldBe 3
  }

  test("1 3 5 2 4 6") {
    val xs = Vector(1, 3, 5, 2, 4, 6)
    val (sorted, count) = sortAndCount(xs)
    sorted shouldBe xs.sorted
    count shouldBe 3
  }

  test("1,5,3,2,4") {
    val xs = Vector(1, 5, 3, 2, 4)
    val (sorted, count) = sortAndCount(xs)
    sorted shouldBe xs.sorted
    count shouldBe 4
  }

  test("for loop") {
    var x = 1
    var y = 1
    for (_ <- 1 to 10 if x < 4 && y < 4) {
      println(x)
      x += 1
    }
  }

  test("problem set 2") {
    val xs = Source
      .fromResource("count_inverting_input.txt")
      .getLines()
      .map(_.toInt)
      .toVector

    val (_, count) = sortAndCount(xs)
    println(count)
  }

}
