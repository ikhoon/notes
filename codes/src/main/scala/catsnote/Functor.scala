package catsnote

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Liam.M on 2017. 12. 26..
  */

object Test1 {
  def map(xs: List[Int])(f: Int => String): List[String] = xs.map(f)

  def map2[A, B](xs: List[A])(f: A => B): List[B] = xs.map(f)


  val listFunctor = new MyFunctor[List] {
    def map[A, B](xs: List[A])(f: A => B): List[B] = xs.map(f)
  }
  val futureFunctor = new MyFunctor[Future] {
    def map[A, B](xs: Future[A])(f: A => B): Future[B] = xs.map(f)
  }

  def map4[M[_], A, B](xs: M[A])(f: A => B)(F: MyFunctor[M]): M[B] =
    F.map(xs)(f)


}
// List[Int], Option[Int], Future[Int]
// Map[Int, String]
trait MyFunctor[F[_]] {
  def map[A, B](xs: F[A])(f: A => B): F[B]
}




