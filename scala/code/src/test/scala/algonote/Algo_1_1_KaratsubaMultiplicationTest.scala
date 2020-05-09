package algonote

import algonote.Algo_1_1_KaratsubaMultiplication.{divide, karatsuba, split}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.Checkers

/**
  * Created by ikhoon on 30/06/2018.
  */
class Algo_1_1_KaratsubaMultiplicationTest extends AnyFunSuite with Matchers with Checkers {

  test("split a number using given n") {
    val x = 12159
    val (a, b) = split(x, 2)
    a shouldBe 121
    b shouldBe 59

    val (a1, b1) = split(x, 3)
    a1 shouldBe 12
    b1 shouldBe 159
  }

  test("divide two numbers") {
    val x = 123456
    val y = 987654

    val kara = divide(x, y)
    kara.a shouldBe 123
    kara.b shouldBe 456
    kara.c shouldBe 987
    kara.d shouldBe 654
  }

  test("divide different length two numbers ") {
    val x = 12345
    val y = 987654

    val kara = divide(x, y)
    kara.a shouldBe 123
    kara.b shouldBe 45
    kara.c shouldBe 9876
    kara.d shouldBe 54
  }

  test("karatsuba") {
    val x: BigInt = 2
    val y: BigInt = 9223372036854775807L
    val mul = karatsuba(x, y)
    mul shouldBe (x * y)
  }

  test("karatsuba forall") {
    check { (x: Long, y: Long) =>
      karatsuba(x, y) == BigInt(x) * BigInt(y)

    }
  }

  test("assignment") {
    val x: BigInt = BigInt("3141592653589793238462643383279502884197169399375105820974944592")
    val y: BigInt = BigInt("2718281828459045235360287471352662497757247093699959574966967627")
    val z = karatsuba(x, y)
    println(z)
    val z1 = x * y
    println(z1)
  }

}
