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

      // 함수, method
      def predicate1(a: Int, b: Int): Boolean = a < b

      // function
      val predicate2 : (Int, Int) => Boolean = (a: Int, b: Int) => a < b

      val predicate3 = (_: Int) < (_: Int)

      // Function2 trait
      val predicate4 : Function2[Int, Int, Boolean] =
        new Function2[Int, Int, Boolean] {
        override def apply(v1: Int, v2: Int): Boolean = v1 < v2
      }

      val predicate5 : Function2[Int, Int, Boolean] = (_: Int) < (_: Int)

      isSorted(sorted.toArray, predicate3) shouldBe true

      val unsorted = List(1, 2, 5, 4, 6)
      isSorted(unsorted.toArray, predicate3) shouldBe false
    }

    // 커링의 어원
    // 하스켈 커리
    // (A, B, C) => D
    // (A => (B => (C => D)))
    // A => B => C => D


    "2.3 curring" in {

      def curry[A, B, C, D, E](f: (A, B, C, D) => E): A => B => C => D => E =
        a => b => c => d => f(a, b, c, d)

      def uncurry[A, B, C](f: A => B => C): (A, B) => C =
        (a, b) => f(a)(b)


      def foo1(a:Int, b: Int) = a + b
      foo1(1, 2) shouldBe 3

      def foo2(a:Int)(b: Int) = a + b
      val addWithOne: (Int) => Int = foo2(1)
      addWithOne(10) == 11
      val addWithTwo : (Int) => Int = foo2(2)
      addWithTwo(10) == 12

      def foo3(a:Int): Int => Int = b => a + b





    }

  }

}
