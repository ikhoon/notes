package stdnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.Seq

/**
  * Created by Liam.M(엄익훈) on 12/16/16.
  */
class InfixPostfixPrefixSpec extends AnyFunSuite with Matchers {

  test("single parameter") {
    val num = 4
    num + 1 shouldBe num.+(1)
  }

  test("two param") {}

  test("postfix") {
    val g: Int = 31
    g.toHexString
    g toHexString
  }

  test("unary") {
    class MyClass {
      def unary_+ : String = "on"
      def unary_- : String = "off"
    }

    val my = new MyClass()
    +my shouldBe "on"
    -my shouldBe "off"

  }

  // list ::

  //
  test("list concat") {
    val a = 9
    val b = 10
    val list = List(11, 12)

    // :: cons
    val sum: List[Int] = a :: b :: list

    val sum2 = a +: list
    list.::(b).::(a) shouldBe sum
    list.+:(a) shouldBe sum2

    // e -> a -> b -> c -> d -> Nil
    class MyList2 {
      def ::(str: String): List[String] = List(str, str)
    }

    val my2 = new MyList2
    "cheese" :: my2 shouldBe List("cheese", "cheese")

    object test {

      trait AB[A, B]

      type IntAndString = AB[Int, String]

      type IntAndString2 = Int AB String

      // natural transformation

      // F[A] === functor map ===> F[B]
      // F[A] === natural transformation ~> ===> G[A]
      val someInt: Option[Int] = Some(1)
      val someString: Option[String] = someInt.map(_.toString)
      val listInt: List[Int] = someInt.toList

      new AB[Int, String] {}

      trait ~>[F, G] {
        def apply(f: F): G
      }

      new (Int ~> String) {
        override def apply(f: Int): String = ???
      }
    }
  }

}
