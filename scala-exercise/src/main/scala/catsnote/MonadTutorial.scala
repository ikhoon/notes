package catsnote

import cats.implicits._
import cats.{Monad, Monoid, StackSafeMonad}

import scala.concurrent.Future

/**
  * Created by Liam.M on 2018. 03. 30..
  */
object MonadTutorial extends App {

  // 누가 모나드 인가?
  // higher kind type
//  List[Int]
//  List // Int => List[Int]
//  Option[Int]
  // 10: Int => Future[Int]
  val f1: Future[Int] = Future.successful(10)

  // instance
  case class Foo[A](a: A)
  case class Bar(a: Int)
  val foo = Foo(10)
  foo.a

  implicit val fooMonadInstance = new Monad[Foo] with StackSafeMonad[Foo] {
    def pure[A](a: A): Foo[A] = Foo(a)
    def flatMap[A, B](fa: Foo[A])(f: A => Foo[B]): Foo[B] = f(fa.a)
  }

  Monad[Foo]

  // Monad : flatMap interface
  // Monadic : Monad 인스턴스를 만들수 있는 자료 구조

  trait MyMonad[F[_]] {
    def pure[A](a: A): F[A]
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  }


  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
    def foreach[A](fa: F[A])(f: A => Unit): Unit
  }


  val a = Bar(10)
  val b = Bar(10)
  Bar(a.a + b.a)


  def sumAll(xs: List[Int]): Int = {
    xs.foldLeft(0) { _ + _ }
  }

  def sumAll1[A]
     (xs: List[A])
     (zero: A, adder: (A, A) => A): A = {
    xs.foldLeft(zero) { (x, y) => adder(x, y)}
  }
  def sumAll2[A]
    (xs: List[A])
    (implicit ev: Monoid[A]): A = {
    xs.foldLeft(ev.empty) { ev.combine }
  }

//  sumAll2[Int]

  // 언제 사용해야 하나?
  // > 곳곳에서

  // 어디서 배워야 하나?
  // > cats

  // 무슨 모나드를 사용해야 하나?
  // > 모나드가 전부가 아니다. 모나드 말고 시선을 넓혀 보자

  // 어떻게 사용해야 하나?
  import cats.implicits._
  implicit val monadInstance =  ???

  // 왜 알아야 하나?
  // > 우리가 사용하는 코드의 패턴의 모음이다.


}
