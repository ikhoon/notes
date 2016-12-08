package fpinsnote

import org.scalatest.{Matchers, WordSpec}

import scala.annotation.tailrec

/**
  * Created by ikhoon on 08/12/2016.
  */
class Chapter2Spec extends WordSpec with Matchers {

  "chapter2" should {
    "is sorted" in {
      def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
        @tailrec
        def loop(xs: Array[A]) : Boolean =
          if(xs.length < 2) true
          else
            if(ordered(xs(0), xs(1))) loop(xs.tail) else false
        loop(as)
      }
      val sorted = List(1, 2, 3, 4, 6)
      val predicate = (_: Int) < (_: Int)

      isSorted(sorted.toArray, predicate) shouldBe true

      val unsorted = List(1, 2, 5, 4, 6)
      isSorted(unsorted.toArray, predicate) shouldBe false
    }
  }
}
