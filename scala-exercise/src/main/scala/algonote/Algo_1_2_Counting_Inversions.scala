package algonote

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

/**
  * Created by ikhoon on 01/07/2018.
  *
  * This file contains all of the 100,000 integers between 1 and 100,000 (inclusive) in some order, with no integer repeated.
  *
  * Your task is to compute the number of inversions in the file given, where the i^{th}i * th
  * row of the file indicates the i^{th}i * th * entry of an array.
  */
object Algo_1_2_Counting_Inversions {


  def sortAndCount(xs: Vector[Int]): (Vector[Int], Long) = {
    val n = xs.size
    if(n == 1) (xs, 0)
    else {
      val (left, right) = xs.splitAt(n / 2)
      val (b, x) = sortAndCount(left)
      val (c, y) = sortAndCount(right)
      val (d, z) = mergeAndCountSplitInv(b, c)
      (d, x + y + z)
    }

  }

  def mergeAndCountSplitInv(xs: Vector[Int], ys: Vector[Int]): (Vector[Int], Long) = {
    val nx = xs.size
    val ny = ys.size
    val nz = nx + ny
    var i = 0
    var j = 0
    var count = 0L
    val zs = ArrayBuffer.empty[Int]
    for (_ <- 1 to nz if i < nx && j < ny) {
      val x = xs(i)
      val y = ys(j)
      if(x <= y) {
        zs += x
        i += 1
      }
      else {
        zs += y
        j += 1
        count += nx - i
      }
    }
    if(i < nx) {
      zs ++= xs.drop(i)
    } else if (j < ny) {
      zs ++= ys.drop(j)
    }

    (zs.toVector, count)
  }
}
