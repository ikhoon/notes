package fpinsnote

import scala.annotation.tailrec

/**
 * Created by Liam.M(엄익훈) on 9/19/16.
 */
object Recursion {


  // 피보나치 수열
  //        1
  //       1  1
  //      1  2  1
  //     1  3  3  1
  //

  // 팩토리얼
  // 0! => 1
  // n! => 1 * 2 * 3 .... n - 1 * n
  // (n + 1)! = n! * (n + 1)
  // 입력값은 양의 정수

  // tailrec => stack : while
  def factorial(n: Int): Int = {
    // 1. 기본값에 구현
    if(n == 0) {
      1
    }
    // 2. 반복부분에 대한 구현
    else {
      factorial(n - 1)
    }
  }

  // tailrec => stack : while
  def factorial1(n: Int): Int = {
    @tailrec
    def fact(n1: Int, acc: Int) : Int = {
      // 1. 기본값에 구현
      if(n1 == 0) {
        acc
      }
      // 2. 반복부분에 대한 구현
      else {
        fact(n1 - 1, acc * n1)
      }
    }
    fact(n, 1)
  }

  // factorial loop
  def factorial2(n: Int): Int = {
    var acc : Int = 1
    var n1: Int = n
    while(n1 != 0) {
      acc = acc * n1
      n1 = n1 - 1
    }
    acc
  }


}
