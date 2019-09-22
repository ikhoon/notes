package freemonad

import cats.Monad
import freemonad.Free._

/**
  * Created by ikhoon on 10/05/2017.
  * https://www.youtube.com/watch?v=aKUQUIHRGec
  * 여기에 나온 컨퍼런스 발표 따라해봄, 발표가 멋짐
  * 주된 내용은 Free Monad 만들어 직접 만들어 보면서 이해하기
  */

sealed trait ~>[F[_], G[_]] {
  def apply[A](fa: F[A]): G[A]
}

// 일단 프리모나드 부터 정의 하자
sealed trait Free[F[_], A] {
  def flatMap[B](f: A => Free[F, B]): Free[F, B] = Bind(this, f)

  def foldMap[G[_]: Monad](nt: F ~> G): G[A] = this match {
    case Pure(a) => Monad[G].pure(a)
    case Suspend(fa) => nt(fa)
  }

}

object Free {

  def pure[F[_], A](a: A): Free[F, A] = Pure(a)
  def liftM[F[_], A](fa: F[A]): Free[F, A] = Suspend(fa)


  final case class Pure[F[_], A](a: A) extends Free[F, A]
  final case class Suspend[F[_], A](fa: F[A]) extends Free[F, A]
  final case class Bind[F[_], E, A](target: Free[F, E], f: E => Free[F, A]) extends Free[F, A]
}



