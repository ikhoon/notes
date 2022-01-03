package stdnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by Liam.M(엄익훈) on 9/7/16.
  */
class CurrySpec extends AnyFunSuite with Matchers {
  test("curry") {
    def foo(a: Int, b: Int, c: Int): Int = ???
    // (a, b, c) => d : 입력이 3개고 출력이 1개다.

    // 입력이 1개고 출력이 1개인 함수를 만들고 이들을 조합할것이다.

    // a => b => c => d
    // 1. (a) => ((b) => ((c) => (d)))
    // 1.에다가 f(a)
    // b => c => d
    // b를 적용시키면
    // c => d
    // c를 적용을 시키면 d가 반환이 됩니다.

    /*
      \ x /
    --- ---------
    | y = f(x)  |
    ------- ----
          / y \

     */

    def adder(x: Int, y: Int): Int = x + y

    val adder1: (Int) => (Int) => Int = (adder _).curried

    val addWithOne: (Int) => Int = adder1(1)

    println(addWithOne(10))
    println(addWithOne(20))
  }

  test("syntax 1") {
    def adder(x: Int)(y: Int): Int = x + y
    val addWithOne = adder(1) _

    println(addWithOne(10))
    println(addWithOne(20))
  }

  test("syntax 2") {
    def adder(x: Int) = (y: Int) => x + y
    val addWithOne: (Int) => Int = adder(1)
    println(addWithOne(10))
    println(addWithOne(20))
  }
  test("x + y + z = w, v1") {
    def adder(x: Int): Int => Int => Int =
      (y: Int) => (z: Int) => x + y + z

    val addWithOne = adder(1) // x = 1
    val addWithOneAndTwo = addWithOne(2) // y = 2
    println(addWithOneAndTwo(10)) // 13
    println(addWithOneAndTwo(20)) // 23

    val foldLeft = List(1, 2, 3).foldLeft(0) _
    val sum = foldLeft { case (x, y) => x + y }
    println(sum)
  }

  test("x + y + z v2") {
    def adder2(x: Int)(y: Int)(z: Int) = x + y + z
    val addWithOne = adder2(1) _
    val addWithOneAndTwo: (Int) => Int = addWithOne(2)
    println(addWithOneAndTwo(10)) // 13
    println(addWithOneAndTwo(20)) // 23
  }
}
