package stdnote

import org.scalatest.{FunSuite, Matchers}

/**
 * Created by Liam.M(엄익훈) on 8/29/16.
 */
class CaseClassSpec extends FunSuite with Matchers {

  val aOption = Option(1)

  test("case class") {
    // 클래스의 종류 1개입니다.
    // class, case class???


    val aValue = aOption match {
      case Some(value) => value
      case None => 0
    }
    println(aValue)

  }

  test("왜, 어디에 쓰이나?") {
    case class Foo(a: Int, b: Boolean)
    val foo = Foo(1, false)
    foo.a
    foo.b
    // @Data
    println(foo)
    // new x

    // 값비교를 잘한다.
    val foo1 = Foo(1, false)
    val foo2 = Foo(2, false)

    assert(foo == foo1)
    assert(foo != foo2)
    // DTO
    // Value

    // 상속....
    // case class는 상속이 된다? class (O) 안된다? case class(O)

    // 불변속성을 가지고 있습니다.

    val foo3 = foo.copy(a = 10, b = false)
    println(foo)
    println(foo3)

    // val(final), var
    var f = foo
    f = foo1

    case class Bar(a: Int, b: Int)  {
      def sum() = a + b
    }


    val bar = Bar(10, 20)
    println(bar.sum())

    class Bar1(val a: Int, var b: Int) {
      def sum() = a + b
    }
    val bar1 = new Bar1(10, 20)
    val bar2 = new Bar1(10, 20)

    assert(bar1 == bar2)

  }

}
