package stdnote

import cats.Monoid

/**
  * Created by Liam.M on 2017. 12. 13..
  */
object Orderings {

  def main(args: Array[String]): Unit = {

    // 1. 행위에 대한 정의
    trait MyOrdering[A] { def compare(x: A, y: A): Int }
    case class Foo(x: Int)
    // 2. 타입에 대한 행위의 구현
    // instance
    object Foo {
      implicit val fooMyOrdering = new MyOrdering[Foo] {
        def compare(x: Foo, y: Foo): Int = x.x - y.x
      }
    }

    // 3. inject를 할 함수를 구현해준다.
    def max[A](xs: List[A])(implicit ord: MyOrdering[A]): A =
      xs.reduceLeft((x, y) => { if(ord.compare(x, y) > 0) x else y })
    // 타입에 대한 행위의 instance가 있을때만 실행된다.
    println(max(List(Foo(1), Foo(2), Foo(3))))

    /// 1 같은 scope
    /// 2. companion object
    List(1, 2, 3).max
//    List(Foo(1), Foo(2), Foo(3)).max



    // implicit의 용도
    // 1. 타입의 암묵적인 변경
    implicit def intToString(int: Int): String = int.toString

    val str: String = 1

    // 2. 문법 추가
    implicit class StringOps(str: String) {
      def to_i = str.toInt
    }
    str.to_i

    // 3. 기본값, context, 설정값같은 느낌
    def foo(implicit x: Int): Int = x
    implicit val int = 10
    foo
    class Bar(implicit x: Int) { }

    case class Quz(x: Int)(implicit y: String)


    //
//    def adder(x: Int, y: Int): Int = x + y
//    //
//    def adder[a](x: a, y: a)(f: (a, a) => a): a = f(x, y)
//    //
//    def adder[a](x: a, y: a)(implicit f: (a, a) => a): a = f(x, y)
    //
    implicit val fooMonoid = new Monoid[Foo] {
      def empty: Foo = Foo(0)
      def combine(x: Foo, y: Foo): Foo = Foo(x.x + y.x)
    }
    def adder[A](x: A, y: A)(implicit M: Monoid[A]): A =
      M.combine(x, y)
    adder(Foo(1), Foo(2))


  }
}
