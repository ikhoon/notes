package fpmortals

import cats.data.NonEmptyList
import cats.{Comonad, Monad}

/**
  * Created by Liam.M on 2018. 03. 07..
  */
object ComonadExample {

  case class Foo[A](a: A)
  // Monad : pure A => F[A]
  val fooInstance = new Monad[Foo] {
    override def pure[A](x: A): Foo[A] = Foo(x)

    override def flatMap[A, B](fa: Foo[A])(f: A => Foo[B]): Foo[B] = ???

    override def tailRecM[A, B](a: A)(f: A => Foo[Either[A, B]]): Foo[B] = ???
  }

  // A => Foo[A]
  // Foo[A] => A
  val foo2Instance = new Comonad[Foo] {
    override def extract[A](x: Foo[A]): A = x.a

    override def coflatMap[A, B](fa: Foo[A])(f: Foo[A] => B): Foo[B] = ???

    override def map[A, B](fa: Foo[A])(f: A => B): Foo[B] = ???
  }
}
