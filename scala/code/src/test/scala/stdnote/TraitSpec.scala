package stdnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by Liam.M(엄익훈) on 11/14/16.
  */
class TraitSpec extends AnyFunSuite with Matchers {

  test("trait!!") {
    // java interface 유사하다.

    trait Foo {
      def bar: String
      def toInt(int: Int): String
    }

    class FooImpl extends Foo {
      override def bar: String = "barbar"
      override def toInt(int: Int): String = int.toString
    }

    val foo = new FooImpl

    println(foo.bar)
    println(foo.toInt(10000))

    val foo2 = new Foo {
      override def bar: String = "barbarbar"
      override def toInt(int: Int): String = int.toString + "!!"
    }

    //

    println(foo2.bar)
    println(foo2.toInt(10000))

    // 두번째 특징 : 함수의 구현체를 가질수 있다. class, abstract class

    trait Bar {
      def bar: String = "barbar"
      def toInt(int: Int): String
    }

    class BarImpl extends Bar {
      override def toInt(int: Int): String = ???
    }

    // 자바는 다중상속이 되지 않아유 그런데 scala는 됨. <>

    // 세번째 특징 : 변수를 가질수 있다.

    trait Bar2 {
      val a = 10
      def bar: String = "barbar"
      def toInt(int: Int): String
    }

    // 네번째 특징 : 생성자가 없다.

    // 퀴즈
    // 두개의 trait을 만들어봅니다.
    // 두개의 A, B
    // A안에는 a라는 함수가 구현되어 있구요.
    // B안에는 a라는 함수가 구현되어 있어요.

    // AB... extends A with B => a
    // BA... extends B with A => a

    trait A {
      println("hello a")
      def a: String = "a"
    }

    trait B {
      println("hello b")
      def a: String = "b"
    }

    class C {
      println("hello c")
      def a: String = "c"
    }

    class ABC extends C with A with B {
      println("hello abc")
      override def a: String = super.a
    }
    println((new ABC).a)

  }
}
