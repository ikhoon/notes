package algonote

import scala.collection.{SortedMap, mutable}

/**
  * Created by ikhoon on 02/07/2018.
  */
object Algo_1_3_QuickSort {

  def quicksort(xs: Array[Int]): (Array[Int], Int) = {
    val comparision = quicksort(xs, 0, xs.length -1, first)
    (xs, comparision)
  }


  /**
    *   3   5   2   1   4   6
    *   ^   ij
    *   3   5   2   1   4   6
    *   ^   ij
    *   3   5   2   1   4   6
    *   ^   i   j
    *   3   5   2   1   4   6
    *   ^   i   j
    *   3   2   5   1   4   6 (swap)
    *   ^   i   j
    *   3   2   5   1   4   6
    *   ^       i   j
    *
    **/

  def partition(xs: Array[Int], l: Int, r: Int, p: Int): Int = {
    swap(xs, l, p)
    val pivot = xs(l)
    var i = l + 1
    for (j <- l + 1 to r) {
      val x = xs(j)
      if(x < pivot) {
        swap(xs, i, j)
        i += 1
      }
    }
    swap(xs, l, i - 1)
    i - 1
  }



  def quicksort(xs: Array[Int], l: Int, r: Int, pivotGen: (Array[Int], Int, Int) => Int): Int = {
    if(l >= r) 0
    else {
      val pivot = pivotGen(xs, l, r)
      val part = partition(xs, l, r, pivot)
      val leftComp = quicksort(xs, l, part - 1, pivotGen)
      val rightComp = quicksort(xs, part + 1, r, pivotGen)
      leftComp + rightComp + r - l
    }
  }

  def quicksortFst(xs: Array[Int], l: Int, r: Int): Int =
    quicksort(xs, l, r, first)

  def quicksortLst(xs: Array[Int], l: Int, r: Int): Int =
    quicksort(xs, l, r, last)

  def quicksortM(xs: Array[Int], l: Int, r: Int): Int =
    quicksort(xs, l, r, middle)

  /** pivot selector */
  def first(xs: Array[Int], l: Int, r: Int): Int = l

  def last(xs: Array[Int], l: Int, r: Int): Int = r

  def middle(xs: Array[Int], l: Int, r: Int): Int = {
    val mid = (r + l) / 2
    val ys = SortedMap(xs(l) -> l, xs(mid) -> mid, xs(r) -> r).toList
    ys(1)._2
  }


  def swap(xs: Array[Int], x: Int, y: Int): Array[Int] = {
    val temp = xs(x)
    xs(x) = xs(y)
    xs(y) = temp
    xs
  }
}
