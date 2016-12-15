package fpinsnote

import org.scalatest.{Matchers, WordSpec}

import scala.annotation.tailrec

/**
  * Created by ikhoon on 08/12/2016.
  */
class Chapter2Spec extends WordSpec with Matchers {

  "chapter2" should {

    "2.1 피보나치 수열" in {
      // (0), 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987
      def fib(n: Int): Int = {
        @tailrec
        def go(a: Int, b: Int, remain: Int): Int = {
          if (remain == 0) b
          else go(b, a + b, remain - 1)
        }

        go(0, 1, n - 1)
      }

      println(fib(1))
      println(fib(2))
      println(fib(3))
      println(fib(4))
      println(fib(5))
      println(fib(6))

      //
      fib(3) shouldBe 2
    }

    "2.2 is sorted" in {
      def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
        @tailrec
        def loop(xs: Array[A]): Boolean =
          if (xs.length < 2) true
          else if (ordered(xs(0), xs(1))) loop(xs.tail) else false

        loop(as)
      }

      val sorted = List(1, 2, 3, 4, 6)
      val predicate = (_: Int) < (_: Int)

      isSorted(sorted.toArray, predicate) shouldBe true

      val unsorted = List(1, 2, 5, 4, 6)
      isSorted(unsorted.toArray, predicate) shouldBe false
    }

    "2.3 curring" in {
      def curry[A, B, C](f: (A, B) => C): A => (B => C) = a => b => f(a, b)

      def foo(a:Int, b: Int) = a + b
      foo(10, 20) shouldBe curry(foo)(10)(20)


    }

  }

}
