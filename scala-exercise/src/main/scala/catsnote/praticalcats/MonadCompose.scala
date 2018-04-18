package catsnote.praticalcats

import cats.StackSafeMonad

/**
  * Created by ikhoon on 05/04/2018.
  */
object MonadCompose {

  import cats.Monad
  import cats.syntax.applicative._

  import scala.language.higherKinds
  // Hypothetical example. This won't actually compile:
  def compose[M1[_]: Monad, M2[_]: Monad] = {
    type Composed[A] = M1[M2[A]]
    new Monad[Composed] with StackSafeMonad[Composed]{
      def pure[A](a: A): Composed[A] =
        a.pure[M2].pure[M1]
      def flatMap[A, B](fa: Composed[A])
                       (f: A => Composed[B]): Composed[B] = {
        // Problem! How do we write flatMap?
        Monad[M1].flatMap[M2[A], M2[B]](fa) {
          m2a =>
            val m2b: M2[B] = Monad[M2].flatMap[A, B](m2a){
            a =>
              val composedB: Composed[B] = f(a)
              // M2[B]를 반환해야한다
              // 함수를 적용하면 M1[M2[B]]가 나온다.
              // M1이나 M2의 정보를 알아야 M1을 없애고 M2를 만둘수 있을듯 하다.
              Monad[M2].pure[B](???)

            }
            Monad[M1].pure(m2b)
        }


        ???
      }
    }
  }
}
