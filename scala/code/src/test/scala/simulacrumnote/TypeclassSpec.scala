package simulacrumnote

import org.scalatest.{Matchers, WordSpec}

/**
  * Created by ikhoon on 21/12/2016.
  */
class TypeclassSpec extends WordSpec with Matchers {

  "typeclass" should {

    case class Foo(a: Int, b: Int)
    "semigroup" in {
      implicit val semigroupInt = new Semigroup[Int] {
        def append(x: Int, y: Int): Int = x + y
      }

      Semigroup[Int].append(10, 20) shouldBe 30
      import Semigroup.ops._
      10 |+| 20 shouldBe 30

      implicit val semigroupFoo = new Semigroup[Foo] {
        def append(x: Foo, y: Foo): Foo = Foo(x.a |+| y.a, x.b |+| y.b)
      }

      def add[A](x: A, y: A)(implicit A: Semigroup[A]) = A.append(x, y)
      val sum = add(Foo(1, 2), Foo(10, 20))
      sum shouldBe Foo(11, 22)
    }

    "monoid" in {
      implicit val monoidInt = new Monoid[Int] {
        def append(x: Int, y: Int): Int = x + y
        def zero = 0
      }

      import Monoid.ops._
      implicit val monoidFoo = new Monoid[Foo] {
        def append(x: Foo, y: Foo): Foo = Foo(x.a |+| y.a, x.b |+| y.b)
        def zero: Foo = Foo(0, 0)
      }

      val foos = List(Foo(1, 2), Foo(10, 20), Foo(100, 200))

      val fooSum = foos.foldLeft(Monoid[Foo].zero) {
        case (acc, x) => Monoid[Foo].append(acc, x)
      }
      fooSum shouldBe Foo(111, 222)
    }

    case class Bar[A](x: A, y: A)
    "functor" in {
      implicit val functorFoo = new Functor[Bar] {
        def map[A, B](fa: Bar[A])(f: A => B): Bar[B] = Bar(f(fa.x), f(fa.y))
      }

      import Functor.ops._
      Bar(1, 2).map(_ * 10) shouldBe Bar(10, 20)
    }


    "monad" in {
      implicit val monadFoo = new Monad[Bar] {
        def pure[A](a: A): Bar[A] = Bar(a, a)

        def flatMap[A, B](fa: Bar[A])(f: (A) => Bar[B]): Bar[B] = f(fa.x)
      }
      import Monad.ops.toAllMonadOps

      Bar(1, 3) flatMap { i => Bar(i * 10, i * 20) }  shouldBe Bar(10, 20)
    }

    "all" in {
      import simulacrumnote.test.allsyntax._

      implicit val semigroupInt = new Semigroup[Int] {
        def append(x: Int, y: Int): Int = x + y
      }
      implicit val monadFoo = new Monad[Bar] {
        def pure[A](a: A): Bar[A] = Bar(a, a)

        def flatMap[A, B](fa: Bar[A])(f: (A) => Bar[B]): Bar[B] = f(fa.x)
      }

      1 |+| 2 shouldBe 3
      Bar(1, 3) flatMap { i => Bar(i * 10, i * 20) }  shouldBe Bar(10, 20)
    }
  }
}
