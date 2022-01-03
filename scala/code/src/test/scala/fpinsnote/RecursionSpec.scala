package fpinsnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by Liam.M(엄익훈) on 9/19/16.
  */
class RecursionSpec extends AnyFunSuite with Matchers {

  test("factorial") {
    // 4! == 24
    // 1 * 2 * 3 * 4
    // 꼬리 재귀 함수
    Recursion.factorial1(4) == Recursion.factorial(4)
    Recursion.factorial2(4) == Recursion.factorial1(4)
  }

}
