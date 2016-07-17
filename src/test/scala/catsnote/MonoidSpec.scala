package catsnote

import cats._
import org.scalatest.{Matchers, WordSpec}
import cats.std.all._
import cats.syntax.all._

/**
  * Created by ikhoon on 2016. 7. 18..
  */
class MonoidSpec extends WordSpec with Matchers {

  "monoid extends semigroup and add empty" should {
    "empty and combineAll" in {

      Monoid[String].empty shouldBe ""
      Monoid[String].combineAll(List("a", "b", "c")) shouldBe "abc"
      Monoid[String].combineAll(List()) shouldBe ""
    }

    "advantage of monoid is that handle complex types" in {
      Monoid[Map[String, Int]].combineAll(List(Map("a" -> 1, "b" -> 2), Map("a" -> 3))) shouldBe Map("a" -> 4, "b" -> 2)
      Monoid[Map[String, Int]].combineAll(List()) shouldBe Map()
    }

    "foldMap" in {
      val l = List(1,2,3,4,5)
      l.foldMap(identity) shouldBe 15
      l.foldMap(_.toString) shouldBe "12345"
    }

    "foldMap tuple" in {
      val l = List(1,2,3,4,5)
      l.foldMap(i => (i, i.toString)) shouldBe (15, "12345")
    }

    "custom monoid" in {
      case class Foo(a: Int, b: String)
      implicit def monoidFoo: Monoid[Foo] = new Monoid[Foo] {
          override def empty: Foo = Foo(0, "")
          override def combine(x: Foo, y: Foo): Foo = Foo(x.a |+| y.a, x.b |+| y.b)
        }

      val l = List(Foo(1, "a"), Foo(2, "b"), Foo(3, "c"), Foo(4, "d"), Foo(5, "e"))
      println(l.foldMap(identity))
    }

  }


}
