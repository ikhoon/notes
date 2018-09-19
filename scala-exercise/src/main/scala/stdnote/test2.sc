import scala.collection.immutable
// 1번 typeclass를 정의 한다.
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

import Functor._
object Functor {
  // 2번 instance를 만든다.
  implicit val listFunctor = new Functor[List] {
    def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }
  implicit val cellFunctor = new Functor[Cell] {
    def map[A, B](fa: Cell[A])(f: A => B): Cell[B] = {
      val a: A = fa.a
      Cell(f(a))
    }
  }
  // 3번 factory를 만든다.
  def apply[F[_]](implicit F: Functor[F]): Functor[F] = F
  // 4번 문법적인 요소를 추가한다.
  implicit class FunctorSyntax[F[_], A](fa: F[A]) {
    def map[B](f: A => B)(implicit F: Functor[F]): F[B] = F.map(fa)(f)
  }
}
case class Cell[A](a: A)
val c = Cell(10)
//Functor[Cell].map(Cell(10))(_ * 10)
//c.map(_ * 10)

implicit class FooOps[A](cell: Cell[A]) {
  def foo() = cell.a
}
implicit class FooOps1[F[_], A](fa: F[A]) {
  def bar()(implicit functor: Functor[F]) = functor.map(fa)(_.toString)
}
implicit class FooOps2[F[_], A](fa: F[A]) {
  def map1[B](f: A => B)(implicit functor: Functor[F]) = functor.map(fa)(f)
}

c.foo()
c.bar()

c.map1(_.toString)

