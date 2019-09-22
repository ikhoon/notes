package catsnote.praticalcats

import scala.concurrent.Future

/**
  * Created by ikhoon on 07/04/2018.
  */
object MonadDerived extends App {

  trait MyMonad[F[_]] {
    def pure[A](a: A): F[A]
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

    def map[A, B](fa: F[A])(f: A => B): F[B] =
      flatMap(fa)(f andThen pure)

    def ap[A, B](ff: F[A => B])(fa: F[A]): F[B] =
      flatMap(fa)(a => map(ff)(f => f(a)))

    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
      ap(map(fa)(a => (b: B) => (a, b)))(fb)

    def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] =
      map(product(fa, fb))(f.tupled)
  }
  import cats.Monad

  import scala.reflect.runtime.universe._
  typeOf[Monad[Future]].members.map(show(_))

}
