package stdnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by Liam.M(엄익훈) on 8/3/16.
  */
class LambdaSpec extends AnyFunSuite with Matchers {

  test("lambda") {
    // method, function

    // function : 함수의 이름이 없을수 있습니다.
    // method : class, object

    //
    val f: (Int) => String = (x: Int) => x.toString
    val f2 = new Function1[Int, String] {
      override def apply(v1: Int): String = v1.toString
    }

    val f3 = new Function1[Int, String] {
      override def apply(v1: Int): String = v1.toString
    }

    def f4(a: Int): String = a.toString

    f2(10) shouldBe "10"
    f3(100) shouldBe "100"
    f4(1000) shouldBe "1000"

    def x = 10 + 1 // 연산
    val y = 10 + 1 // 값을 저장

    // def
    // val
  }

  test("function vs method") {
    // method
    def foo(a: String): String = {
      a + a
    }

    // function
    val foo1 = (x: String) => x + x

    val foo2 = new Function1[String, String] {
      override def apply(x: String): String = x + x
    }

    // int => string 인자로 받는다.
    def myFirstHigh(value: Int)(f: Int => String): String = {
      f(value)
    }

    myFirstHigh(10)(x => x.toString)

    // 1. 고계함수, 고차함수 API를 잘활용해보자.
    // map, fold, reduce, flatMap
    val list = List(1, 2, 3)

    // lodash, java8 stream, native javascript,
    // c#, ruby, python, scala

    // 1. map을 이용해서 + 10
    def plus10(i: Int) = i + 10
    list.map(plus10)
    list.map(x => x + 10)
    list.map(_ + 10)
    // 2. reduce를 이용해서 전체 합을 구하세요
    list.reduce((x, y) => x + y)
    list.reduce(_ + _)
    list.reduceLeft((x, y) => x + y)
    // 1, 2, 3
    // (x, y) ==> (1, 2) => 1 + 2
    // (3, 3) => 3 + 3 => 6
    list.sum

    // 3. fold를 이용해서 전체 곱을 구하세용.
    // 1, 2, 3
    list.foldLeft(0)((x, y) => x * y)
    list.foldLeft(0)(_ * _)
    // (1, 1) => 1 * 1
    // (1, 2) => 2
    // (2, 3) => 6

    // 0

    // 4. flatMap을 이용해서 List(11, 12, 22, 23, 33, 34)를 만들어 보시오.
    // `꽃` 함수형 언어에의 꽃
    // map + flatten
    list.flatMap { i =>
      List(i * 11, i * 11 + 1)
    }
    list.flatMap(i => List(i * 11, i * 11 + 1))
    list.map(i => List(i * 11, i * 11 + 1)).flatten

  }

}
/// top
