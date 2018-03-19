package catsnote

import cats.Functor

/**
  * Created by Liam.M on 2018. 03. 15..
  */
object FunctorInstance {
  import cats.implicits._
  import cats.syntax.list._

  val xs = List(1, 2, 3)
  xs.map(_ + 1)
  Functor[List].map(xs)(_ + 1)
  // fmap => map
  xs.fmap(_ + 1)

  case class MyId[A](a: A)
  implicit val myIdInstance = new Functor[MyId] {
    def map[A, B](fa: MyId[A])(f: A => B): MyId[B] = MyId(f(fa.a))
  }

  val id = MyId(10)
  id.fmap(_ + 1)

  Functor[List].compose[Option].map(List(Option(1)))(_ + 1)

}
/*
// partial unification
trait MyFunctor[F[_]] {
  // map
  def map[A, B](fa: F[A])(f: A => B): F[B]
  // compose
}

*/
