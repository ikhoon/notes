package catsnote

import cats._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

/**
  * Created by ikhoon on 2016. 7. 20..
  */
class ApplicativeSpec extends AnyWordSpec with Matchers {

  "Applicative extends Apply and added `pure`" should {

    import cats.instances.option._
    import cats.instances.list._

    "pure" in {
      Applicative[Option].pure(1) shouldBe Option(1)
      Applicative[List].pure(2) shouldBe List(2)
    }

    "compose" in {
      Applicative[List].compose(Applicative[Option]).pure(1) shouldBe List(Option(1))
    }

    "applicative is generalization of monad" in {
      Applicative[Option].pure(1) shouldBe Monad[Option].pure(1)
    }

    "applicative instance" in {

      // F[A] ==> F[B]
      val optionInt = Option[Int](1)
      val toStr1: Int => String = (a: Int) => a.toString
      val toStr2: Option[Int => String] = Option((a: Int) => a.toString)

      /////

      val toStr3: Int => Option[String] = { (a: Int) =>
        {
          if (a < 10) {
            Some(a.toString)
          } else {
            None
          }
        }
      }

      /*
      // A :Int

      // B : Option[String]
      // fa : Option[Int]
      // F = Option

      // F[B] = Option[Option[String]]
      // Functor.map(A => B)(fa: F[A]) : F[B]
      val maybeOptionOption: Option[Option[String]] = optionInt.map(toStr3)
      val mayBe: Option[String] = maybeOptionOption.flatten
      val maybeString: Option[String] = optionInt.flatMap(toStr3)

      /////////////

      val optionStr: Option[String] = optionInt.map(toStr1)
      import cats.syntax.all._
      import cats.implicits._
      val optionStr2: Option[String] = Apply[Option].ap(toStr2)(optionInt)

      // Apply.ap(F[A => B])(fa: F[A]) : F[B]
      // Monad.flatMap(A => F[B])(fa: F[A]) : F[B]

      case class Foo[T](a: T)

      implicit val fooApplicativeInstance = new Applicative[Foo] {
        def pure[A](x: A): Foo[A] = Foo[A](x)
        def ap[A, B](ff: Foo[A => B])(fa: Foo[A]): Foo[B] = {
          val f: A => B = ff.a // 함수를 끄집어 내야 됩니다.
          val a: A = fa.a // 끄집어낸 값
          val b: B = f(a) // 함수를 적용, 변환 A => B
          val fb: Foo[B] = Foo(b) // context 씌워줌
          fb
        }
      }

      val foo1 = Foo(1)
      val foo2 = Foo("a")

      Applicative[Foo].ap[Int, String](Foo((x: Int) => x.toString))(foo1) shouldBe Foo("1")
      fooApplicativeInstance.ap[Int, String](Foo((x: Int) => x.toString))(foo1) shouldBe Foo("1")

      import cats.syntax.apply._

      Foo((x: String) => x.toUpperCase).ap(foo2) shouldBe Foo("A")
     */
    }

  }
}
