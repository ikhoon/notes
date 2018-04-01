package catseffectnote

import java.util.concurrent.{Executors, ScheduledExecutorService, ScheduledThreadPoolExecutor}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._

/**
  * Created by ikhoon on 19/03/2018.
  * 참조 : https://typelevel.org/cats-effect/datatypes/io.html
  * cats.effect.IO 직접 쓸일을 없을것 같다. monix.eval.Task를 사용하면 되지 않을까 싶지만
  * 그래도 컨셉이나 api 설계에 대해서 알아보자.
  */
object IOExample extends App {

  // Introduction
  // IO[A] 타입은 평가될때 계산이 된다. A 타입이 반환될때 effect가 수행될수 있다.
  // lazy하단 말씀

  // IO 값은 순수, 불변의 속성을 가지고 있음 fp할수 있게
  // IO는 사이드 이펙트 연산에 대한 표현을 나타내는 자료구조일 뿐이다.

  // IO 동기나 비동기 연산을 표현한다.
  // 1. 하나의 편가는 하나의 결과를 반환한다.
  // 2, 끝은 성공이나 실패로 끝난다. 실패의 경우에는 flatMap에 대해서는 짧게 반환한다.
  // 3. 취소할수 있다. 하지만 이건 유저가 취소로직을 제공해줄때만이다.. 그렇지 세상에 공짜는 없지....

  // Effect는 "end of the world"까지 평가하지 않는다.
  // 이말은 "unsafe" 함수가 사용될때 까지.
  // 결과는 cache되지 않는다. 이말은 leak이나 memory overhead가 최소화 된다는 말이다.
  // reference transparent 참조 투명성이 보장이된다.

  import cats.effect.IO
  val ioa = IO { println("hey!") }
  val program: IO[Unit] =
    for {
      _ <- ioa
      _ <- ioa
    } yield ()

  program.unsafeRunSync()
  // 두번 실행됨
  // hey!
  // hey!

  // Synchronous  Eager : A
  // Synchronous  Lazy  : () => A : Eval[A]
  // Asynchronous Eager : (A => Unit) => Unit : Future[A]
  // Asynchronous Lazy  : () => (A => Unit) => Unit : IO[A]

  // Future와 IO는 비동기 처리 결과를 얻기에 적합하다.
  // 하지만 순수함과 느긋함 때문에 IO는 더 컨트롤 가능한 평가 모델이고 더 예측가능하다.

  // 이코드는 Future와 IO가 같은 결과를 가지지만
  def addToGauge(x: Int): IO[Unit] = IO { println(s"add to guage $x") }
  for {
    _ <- addToGauge(32)
    _ <- addToGauge(32)
  } yield ()

  // 코드가 참조 투명성을 가지고 있다면 위코드는 아래 코드로 변환이 가능하다.
  // 이코드는 IO만 가능하다. Future로 구현하면 아래 코드와 같이 동작하지 않는다.
  val task = addToGauge(32)

  for {
    _ <- task
    _ <- task
  } yield ()



  // Stack safety
  // IO는 flatMap구현은 trampoline으로 구현되어 있다.
  // 그렇기 때문에 flatMap의 함수 호출은 stack safety하다.
  def fib(n: Int, a: Long = 0, b: Long = 1): IO[Long] =
    IO(a + b).flatMap(c =>
      if(n > 0)
        fib(n - 1, b, c)
      else
        IO.pure(c)
    )

  println(fib(100000).unsafeRunSync())

  // Describing Effects
  // IO는 다양한 종류의 effect를 효과적으로 표현하는 뛰어난 추상화 능력이 있다.

  // Pure Values - IO.pure & IO.unit
  // 순수값을 IO로 lift할수 있다.
  // 이값은 이미 평가된값이다.
  def pure[A](a: A): IO[A] = ???
  // 함수모양에서 보면 알수 있듯이 call by name이 아니라 call by value로 전달되어 진다.

  // 그래서 이렇게 하면 안된다.
  IO.pure(println("THIS IS WRONG!"))


  // Synchronous Effects — IO.apply
  // 이함수는 가장 많이 사용되는 builder일것이다. Sync[IO].delay와 같다.
  // IO의 행위가 바로 현재 thread의 call stack에서 바로 실행될수 있는다는걸 나타낸다.
  def apply[A](body: => A): IO[A] = ???

  // pure와 다른점은 parameter가 by name 형태로 전달되지 때문에 연산은 지연된다.

  // 하나의 예는 JVM위에서 blocking I/O로 console에서 read / write 하는거다.
  // 이것 바로 실행된다. 비동기가 아니다.

  def putStrLn(value: String) = IO(println(value))
  def readLn = IO(scala.io.StdIn.readLine)

  for {
    _ <- putStrLn("What's your name")
    n <- readLn
    _ <- putStrLn(s"Hello $n")
  } yield ()


  // Asynchronous Effects — IO.async & IO.cancelable
  // IO는 IO.async, IO.cancelable을 통해서 비동기 연산도 표현할수 있다.
  // IO.async는 Async#async의 laws와 함계 컴파일된다.
  // 그리고 간단한 취소될수 없는 비동기 연산을 표현한다.

  def async[A](k: (Either[Throwable, A] => Unit) => Unit): IO[A] = ???

  // 제공되는 함수는 성공의 결과 Right(a)나 실패 Left(error)를 callback으로 주입한다.
  // 유저는 asynchronous side effect에 대한 호출필요하면 어디서나 주입된 callback에다가 완료 신호를 보내면 된다.

  // async함수는 기존의 비동기 코드르 IO 타입으로 변경할때 사용하고
  // apply함수는 기존으 동기 코드를 IO타입으로 변경할때 사용하면 되는것 같다.
  // Future -> IO 로 바꾸어 보자
  // 이미 내장된 하뭇 IO.fromFuture가 있지만 그냥 만들어본다.

  def convert[A](fa: Future[A])(implicit ec: ExecutionContext): IO[A] =
    IO.async(cb =>
      fa.onComplete {
        case Success(v) => cb(Right(v))
        case Failure(ex) => cb(Left(ex))
      }
    )


  // Cancelable Processes
  // 취소할수 있는 IO task를 만들기 위해서는 IO.cancelable을 사용해서 만들면 된다.
  // 이건 Concurrent#cancelable에 부합한다. 함수 시크니쳐는 아래와 같다.
  def cancelable[A](k: (Either[Throwable, A] => Unit) => IO[Unit]): IO[A] = ???
  // async와 유사하다 하지만 등록하는 함수가 IO[Unit]를 제공받기를 기대한다.
  // 그게 요구된 취소로직을 간직한다.

  // cancelation은 IO task를 완료전에 중시할수 있기 때문에
  // 획득했던 리소를를 릴리즈하는게 가능하고 이는 leak을 방지하는데 유용하다.

  def delayedTick(d: FiniteDuration)(implicit sc: ScheduledExecutorService): IO[Unit] =
    IO.cancelable(cb => {
      val r = new Runnable { def run() = cb(Right(())) }
      val f = sc.schedule(r, d.length, d.unit)
      IO(f.cancel(false))
    })

  import cats.implicits._
  implicit val sc = new ScheduledThreadPoolExecutor(1, Executors.defaultThreadFactory())
  println("begin 5 seconds")
  val res = delayedTick(5 seconds) *> IO(println("after 5 seconds"))



}
