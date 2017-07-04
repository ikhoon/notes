package stdnote

import org.scalatest.{FunSuite, Matchers}

/**
  * Created by ikhoon on 2017. 3. 6..
  */

// 오브젝트를 만드는것
// 전수현: 함수를 포함할수 있음
// 줄리엣: companion object, class A, object A 특징 좀 있다 알아봄, private 에 접근 가능
// 김은미: 싱글톤 객체, 패턴
// 전수현: class + new, object 는 new가 필요 없음
// 김혜진: main, static void main()
// 리엄: constructor를 가질수 없다.


class Foo(a: Int) {

  private def bar: Int = 10
  Foo.b

  def main(args: Array[String]): Unit = {
    println("hello world")
  }
}

object Foo {

  private val b = 10
  val a = new Foo(10)

}

object HelloWorld {

  // static
  def main(args: Array[String]): Unit = {
    println("hello world")
  }

}

object HelloWorld2 extends App {
  println("hello world2")
}

class Bar(val bar: Int)
class Baz(val b: String)

object Baz {
  implicit def foo(a: Bar): Baz = new Baz(a.bar.toString)
}

class ObjectSpec extends FunSuite with Matchers {

  test("private") {
    val a = new Foo(10)
    val b = Foo

    b.a
  }


  test("implicit conversion") {
//    import Test.foo
    val bar = new Bar(10)
    val baz: Baz = bar
  }
}
