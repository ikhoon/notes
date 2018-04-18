package catsnote

import cats.Monad

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by Liam.M on 2018. 03. 20..
  */
object MonadExample extends App {

  val ten = 10

  import cats.implicits._
  ten.pure[Future]

  for {
    a <- ten.pure[Future]
    b <- ten.pure[Future]
    c <- ten.pure[Future]
  } yield a + b + c

  val a: Either[Throwable, Int] = Right(10)
  def f(x: Int): Either[Throwable, String] = Right(x.toString)
  def g(ex: Throwable): Either[Exception, String] = Left(new Exception(ex.getMessage))
  val b: Either[Throwable, String] = a.flatMap(f)
  val c: Either[Exception, String] = b.left.flatMap(g)
  b.leftFlatMap(g _)
  1.some


  // Either monad instance
  implicit def eitherInstance[L]: Monad[({type Lambd[A] = Either[L, A]})#Lambd] = new Monad[Either[L, ?]] {
    def pure[A](x: A): Either[L, A] = ???

    def flatMap[A, B](fa: Either[L, A])(f: A => Either[L, B]): Either[L, B] = ???

    def tailRecM[A, B](a: A)(f: A => Either[L, Either[A, B]]): Either[L, B] = ???

  }






  // foo monad instance
  case class Foo[A](a: A)












}
