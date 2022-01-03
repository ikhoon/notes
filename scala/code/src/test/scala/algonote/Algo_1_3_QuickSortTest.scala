package algonote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable

/**
  * Created by ikhoon on 02/07/2018.
  */
class Algo_1_3_QuickSortTest extends AnyFunSuite with Matchers {

  import Algo_1_3_QuickSort._
  test("partition 1, 2, 3, 4, 5, 6") {
    val xs = List(1, 2, 3, 4, 5, 6)
    val arr = xs.toArray
    val pivot = partition(arr, 0, xs.size - 1, 0)
    pivot shouldBe 0
    arr shouldBe xs.toArray
  }

  test("partition 4, 1, 3, 2, 6, 5") {
    val xs = Array(4, 1, 3, 2, 6, 5)
    val pivot = partition(xs, 0, xs.length - 1, 0)
    pivot shouldBe 3
    xs shouldBe Array(2, 1, 3, 4, 6, 5)
  }

  test("partition 6, 5, 4, 3, 2, 1") {
    val xs = Array(6, 5, 4, 3, 2, 1)
    val pivot = partition(xs, 0, xs.length - 1, 0)
    pivot shouldBe 5
    xs shouldBe Array(1, 5, 4, 3, 2, 6)
  }

  test("quicksort 1, 2, 3, 4, 5, 6") {
    val xs = List(1, 2, 3, 4, 5, 6)
    val arr = xs.toArray
    val (sorted, count) = quicksort(arr)
    sorted shouldBe xs.toArray
    count shouldBe (5 + 4 + 3 + 2 + 1)
  }

  test("quicksort 4, 1, 3, 2, 6, 5") {
    val xs = List(4, 1, 3, 2, 6, 5)
    val (sorted, count) = quicksort(xs.toArray)
    sorted shouldBe xs.sorted
    count shouldBe (5 + 2 + 1)
  }

  test("problem set 3-1") {
    val xs = scala.io.Source
      .fromResource("quick_sort_input.txt")
      .getLines()
      .map(_.toInt)
      .toArray
    val count = quicksortFst(xs, 0, xs.length - 1)
    println(count)
  }

  test("problem set 3-2") {
    val xs = scala.io.Source
      .fromResource("quick_sort_input.txt")
      .getLines()
      .map(_.toInt)
      .toArray
    val count = quicksortLst(xs, 0, xs.length - 1)
    println(count)
  }

  test("problem set 3-3") {
    val xs = scala.io.Source
      .fromResource("quick_sort_input.txt")
      .getLines()
      .map(_.toInt)
      .toArray
    val count = quicksortM(xs, 0, xs.length - 1)
    println(count)
  }

  test("quicksortM 8 2 4 5 7 1 ") {
    val xs = List(8, 2, 4, 5, 7, 1)
    val arr = xs.toArray
    quicksortM(arr, 0, 5)
    xs.sorted.toArray shouldBe arr

  }
}
