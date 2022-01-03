package catsnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by ikhoon on 2017. 4. 4..
  */
class Introduction extends AnyFunSuite with Matchers {

  test("cats") {
    // category theory
    import cats.implicits._
    import cats.syntax.all._

    val a = 10

    // type class, 타입에 대한 액션의 정의 <=> 구현, def,
    // instance
    // syntax

    trait Foo[A] {
      def bar: A
    }

    // 타입클래스에 대한 구현체

    implicit val fooInt = new Foo[Int] {
      override def bar: Int = 10
    }
    implicit val fooString = new Foo[String] {
      override def bar: String = "hello"
    }

    implicit val fooLong = new Foo[Long] {
      override def bar: Long = 1000L
    }

    // 타입은 컴파일러가 아주 유용하게 쓰는 정보
    def foo[B](implicit b: Foo[B]): B = b.bar

    println(foo[Int])
    println(foo[String])
    println(foo[Long])

    trait Increment[A] {
      def inc(a: A): A
    }

    implicit val intInc = new Increment[Int] {
      def inc(a: Int) = a + 1
    }

  }
}
