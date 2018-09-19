package stdnote

import org.scalatest.{Matchers, WordSpec}

class ImplicitImportSpec extends WordSpec with Matchers {

  "implicit new" should {
    "타입만 가지고 초기화를 해보자" in {

      class Foo[A](implicit val a: A)

      object Foo {
        implicit def foo[A](implicit a: A) = new Foo[A]
      }

      implicit val int: Int = 1

      def bar(implicit foo: Foo[Int]) = foo

      println(bar.a)
      val baz :Foo[Int] = new Foo[Int]
      println(baz.a)
    }
  }

}
