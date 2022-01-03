package catsnote

import cats.Functor
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

/**
  * Created by ikhoon on 2016. 7. 18..
  */
class FunctorSpec extends AnyWordSpec with Matchers {

  "functor has a map" should {

    "many scala stdlib has a map" in {
      Option(1).map(_ + 1) shouldBe Option(2)
      List(1, 2, 3).map(_ + 2) shouldBe List(3, 4, 5)
      Vector(1, 2, 3).map(_.toString) shouldBe Vector("1", "2", "3")
    }

    "create functor instance" in {
      // implicit 이 없다면?
      implicit def optionFunctor: Functor[Option] = new Functor[Option] {
        override def map[A, B](fa: Option[A])(f: (A) => B): Option[B] = fa.map(f)
      }
      implicit def listFunctor: Functor[List] = new Functor[List] {
        override def map[A, B](fa: List[A])(f: (A) => B): List[B] = fa.map(f)
      }
      case class Foo[A](a: A)
      implicit def footFunctor: Functor[Foo] = new Functor[Foo] {
        override def map[A, B](fa: Foo[A])(f: (A) => B): Foo[B] = Foo(f(fa.a))
      }

      Functor[List].map(List(1, 2, 3))(_ * 10) shouldBe List(10, 20, 30)

      Functor[Option].map(Option(1))(_ * 100) shouldBe Option(100)

      Functor[Foo].map(Foo("ab"))(_.toUpperCase) shouldBe Foo("AB")
    }

    "type lambda with kind-projector" in {

      implicit def function1Functor[In]: Functor[Function1[In, *]] = new Functor[Function1[In, *]] {
        override def map[A, B](fa: (In) => A)(f: (A) => B): (In) => B = fa.andThen(f)
      }

      def a(x: Int): Int = x + 10
      def b(y: Int): String = y.toString
      Functor[Function1[Int, *]].map(a)(b)(200) shouldBe "210"
    }

    "only type lambda function functor" in {
      implicit def f1Functor[I]: Functor[({ type L[A] = Function1[I, A] })#L] =
        new Functor[({ type L[A] = Function1[I, A] })#L] {
          override def map[A, B](fa: (I) => A)(f: (A) => B): (I) => B = fa.andThen(f)
        }

      Functor[({ type L[A] = Function1[Int, A] })#L]
        .map((x: Int) => x + 10)((y: Int) => y.toString)(200) shouldBe "210"
    }

    "using map in cats functor instances" in {
      import cats.implicits._
      Functor[List].map(List("hello", "world!"))(_.length) shouldBe List(5, 6)
      Functor[Option].map(Option("Hello"))(_.length) shouldBe Option(5)
      Functor[Option].map(None: Option[String])(_.length) shouldBe None
    }

    "lift only accept map function and return lifted function" in {
      import cats.implicits._
      val lengthOfOption = Functor[Option].lift[String, Int](_.length)
      lengthOfOption(Some("abcdefg")) shouldBe Some(7)
    }

    "fproduct produce pair of origal value and mapped value" in {
      import cats.implicits._
      val source = List("My Cat", "is", "Sarang.")
      Functor[List].fproduct(source)(_.length) shouldBe List(("My Cat", 6), ("is", 2), ("Sarang.", 7))
    }

    "compose two functor" in {
      import cats.implicits.catsStdInstancesForList
      import cats.implicits.catsStdInstancesForOption
      val listOpt = Functor[List].compose(Functor[Option])
      listOpt.map(List(Some(1), None, Some(3)))(_ + 1) shouldBe List(Some(2), None, Some(4))
    }
  }

}
