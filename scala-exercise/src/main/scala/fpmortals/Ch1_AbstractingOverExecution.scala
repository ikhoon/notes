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

}










