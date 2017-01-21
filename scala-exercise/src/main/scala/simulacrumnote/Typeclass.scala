package simulacrumnote

import simulacrum._
/**
  * Created by ikhoon on 21/12/2016.
  */

@typeclass trait Semigroup[A] {
  @op("|+|") def append(x: A, y: A): A
}


@typeclass trait Monoid[A] extends Semigroup[A] {
  def zero: A
}

@typeclass trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

@typeclass trait Monad[F[_]] {
  def pure[A](a: A): F[A]

  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def >>=[A, B](fa: F[A])(f: A => F[B]): F[B] = flatMap(fa)(f)
}

trait AllSyntax
  extends Semigroup.ToSemigroupOps
  with Monoid.ToMonoidOps
  with Functor.ToFunctorOps
  with Monad.ToMonadOps

object test {
  object allsyntax extends AllSyntax
}

