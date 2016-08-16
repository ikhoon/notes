package shapenote

import org.scalatest.{Matchers, WordSpec}
import shapeless.{Generic, HNil, LabelledGeneric}

/**
  * Created by ikhoon on 2016. 8. 16..
  */
class GenericSpec extends WordSpec with Matchers {

  "Generic representation of case classes" should {

    "convert back and forth case class to their HList Generic representation" in {
      case class Foo(i: Int, s: String, b: Boolean)

      val fooGen = Generic[Foo]
      val foo = Foo(23, "foo", true)

      val l = fooGen.to(foo)
      l shouldBe 23 :: "foo" :: true :: HNil
      val r = 13 :: l.tail
      val newFoo = fooGen.from(r)
      newFoo shouldBe Foo(13, "foo", true)
    }

    "materialized using an implicit macro" in {
      import shapeless.everywhere
      import shapeless.poly._
      sealed trait Tree[T]
      case class Leaf[T](t: T) extends Tree[T]
      case class Node[T](left: Tree[T], right: Tree[T]) extends Tree[T]

      object inc extends -> ((i: Int) => i + 1)

      val tree: Tree[Int] =
        Node(
          Leaf(1),
          Node(
            Leaf(2),
            Leaf(3)
          )
        )

      everywhere(inc)(tree) shouldBe Node(
        Leaf(2),
        Node(
          Leaf(3),
          Leaf(4)
        )
      )

    }

    "labelled generic" in {
      import shapeless.record._
      case class Book(author: String, title: String, id: Int, price: Double)

      val bookGen = LabelledGeneric[Book]
      val tapl = Book("Benjamin Pierce", "Types and Programming languages", 262162091, 44.11)
      val rec = bookGen.to(tapl)
      rec('price) shouldBe 44.11
      rec(Symbol("price")) shouldBe 44.11

      import shapeless.syntax.singleton._
      val updatedBook = bookGen.from(rec.updateWith('price)(_ + 2.0))

      updatedBook.price shouldBe 46.11

      // TODO 이건 카레가 원하던거다.
      // cognac에 적용해보자.
      case class ExtendedBook(author: String, title: String, id: Int, price: Double, inPrint: Boolean)
      val bookExtGen = LabelledGeneric[ExtendedBook]

      val extendedBook = bookExtGen.from(rec + ('inPrint ->> true))
      extendedBook.inPrint shouldBe true
    }
  }
}
