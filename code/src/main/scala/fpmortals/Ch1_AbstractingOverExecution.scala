package fpmortals

import scala.concurrent.Future

/**
  * Created by ikhoon on 15/11/2017.
  */
object Ch1_AbstractingOverExecution {

  // command line interface를 만든다 해보자
  // 읽을수도 있고 쓸수도 있다.

  { // 구림

  trait TerminalSync {
    def read(): String
    def write(t: String): Unit
  }

  trait TerminalAsync {
    def read(): Future[String]
    def write(t: String): Future[Unit]
  }

  }

  // 런타임의 구현에 따라 synchronously 혹은 asynchronously하게
  // 어떻게 하면 generic한 코드로 바꿀수 있나?

  // 자바 1.2에서 소개된 공통의 부모를 만드는 방법으로 문제를 푸는것을 시도해보자
  // 그리고 higher kinded types(HKT)라 불리는 기능을 사용
  // @see fpmortals.Ch1_HigherKindedTypes

  trait Terminal[C[_]] {
    def read: C[String]
    def write(t: String): C[Unit]
  }

  type Now[X] = X

  object TerminalSync extends Terminal[Now] {
    def read: String = ???
    def write(t: String) = ???
  }

  object TerminalAsync extends Terminal[Future] {
    def read: Future[String] = ???
    def write(t: String): Future[Unit] = ???
  }

  // C를 Context르 생각할수 있다.
  // 이유는 실행의 context를 지금(Now)할것인가? 나중에 할것인가(Future)인가를 이야기 해주기 때문이다..

  // 지금은 C[String]을 가지고 할수 있는게 없다..
  // C[_]을 감싸는 값이 필요하다.

  trait Execution[C[_]] {
    def doAndThen[A, B](c: C[A])(f: A => C[B]): C[B]
    def create[B](b: B): C[B]
  }

  // 그리고
  def echo[C[_]](t: Terminal[C], e: Execution[C]): C[String] = {
    e.doAndThen(t.read) {in : String=>
      e.doAndThen(t.write(in)) { _: Unit =>
        e.create(in)
      }
    }
  }

  // 이제 sync와 async사이에 구현을 공유 할수 있다.
  // Terminal[Now]의 Mock코드를 통해서 테스트 할수 있다.

  // 근데 echo 코드는 더럽다.

  // 코드를 좀 깔금하게 하고 싶다.
  object Execution {
    implicit class Ops[A, C[_]](c: C[A]) {
      def flatMap[B](f: A => C[B])(implicit e: Execution[C]): C[B] =
        e.doAndThen(c)(f)

      def map[B](f: A => B)(implicit e: Execution[C]): C[B] =
        e.doAndThen(c)(f andThen e.create)
    }
  }
  import Execution._

  class Cell[T]

  //그리고 코드는
  def echo2[C[_]](implicit t: Terminal[C], e: Execution[C]): C[String] =
    t.read.flatMap { in: String =>
      t.write(in).map { _: Unit =>
        in
      }
    }

  def echo3[C[_]](implicit t: Terminal[C], e: Execution[C]): C[String] =
    for {
      in <- t.read
      _ <- t.write(in)
    } yield in



  // 1.2 Pure Functional Programming
  // * Totally - 가능한 모든 입력값에 대해 return 값이 있다.
  // * Determinism - 같은 입력값에 대해서 같은 return 값이다.
  // * Purity - effect는 단지 return 값에 대한 연산이다.





}











