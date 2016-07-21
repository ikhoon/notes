package catsnote

import cats._
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by ikhoon on 2016. 7. 20..
  */
class ApplicativeSpec extends WordSpec with Matchers {

  "Applicative extends Apply and added `pure`" should {

    import std.option.optionInstance
    import std.list.listInstance

    "pure" in {
      Applicative[Option].pure(1) shouldBe Option(1)
      Applicative[List].pure(2) shouldBe List(2)
    }

    "compose" in {
      (Applicative[List] compose Applicative[Option]).pure(1) shouldBe List(Option(1))
    }

    "applicative is generalization of monad" in {
      Applicative[Option].pure(1) shouldBe Monad[Option].pure(1)
    }

    "applicative instance" in {
      case class Foo[T](a: T)

      implicit val fooApplicativeInstance = new Applicative[Foo] {
        override def pure[A](x: A): Foo[A] = Foo[A](x)
        override def ap[A, B](ff: Foo[(A) => B])(fa: Foo[A]): Foo[B] = Foo(ff.a(fa.a))
      }


      val foo1 = Foo(1)
      val foo2 = Foo("a")

      Applicative[Foo].ap[Int, String](Foo((x: Int) => x.toString))(foo1) shouldBe Foo("1")

      import cats.syntax.apply._

      Foo((x: String) => x.toUpperCase).ap(foo2) shouldBe Foo("A")
    }


  }
}
