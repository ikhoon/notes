package simulacrumnote

/**
  * Created by Liam.M(엄익훈) on 27/12/2016.
  */

case class Foo(a: Int, b: Int)
case class Foo1[A](a: A, b: A)

import simulacrum._
@typeclass trait Functor1[F[_]] {
  @op(">>") def map[A, B](fa: F[A])(f: A => B): F[B]
}



object Typeclass1 {

  implicit val fooFunctor: Functor1[Foo1] = new Functor1[Foo1] {
    def map[A, B](fa: Foo1[A])(f: (A) => B): Foo1[B] =
      Foo1(f(fa.a), f(fa.b))
  }

  import Functor1.ops._

  val foo2 = Foo1(10, 20)
  foo2 >> (_ + 1)

  // monoid => (a + b) + c == a + (b + c)

  // def unit ?
  // def flatmap


  // def empty
  // def append
  // val a: Int
  // val b: Int
  //

  // 1번 타입을 받는 trait을 만든다.
  trait Monoid[A] {
    def zero: A
    def append(a: A, b: A): A
  }

  // 2번 인스턴스를 만든다.
  implicit val intSumMonoid : Monoid[Int] = new Monoid[Int] {
    def zero: Int = 0
    def append(a: Int, b: Int): Int = a + b
  }

  // 3번째는 syntax 혹은 operator를 만든다.
  object Monoid {
    def append1[A](a: A, b: A)(implicit M: Monoid[A]): A =
      M.append(a, b)

    // context bound
    def append2[A: Monoid](a: A, b: A): A =
      implicitly[Monoid[A]].append(a, b)
  }

  val a: Int = 10
  val b: Int = 20
  Monoid.append1(a, b)

  val c : Boolean = true

  implicit val fooMonoidInstance: Monoid[Foo] = new Monoid[Foo] {
    def zero: Foo = Foo(0, 0)
    def append(x: Foo, y: Foo): Foo =
      Foo(Monoid.append1(x.a, y.a), Monoid.append1(x.b, y.b))
  }




















}
