package catsnote

import cats.data.Kleisli
import cats.effect.IO

import scala.concurrent.{Await, Future}
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by ikhoon on 31/03/2018.
  */
object KleisliExample extends App {

  // 합성
  val a : Int => String = ???
  val b : String => Boolean = ???
//  val c: Int => Boolean = a.andThen(b)

  val a1: Int => Future[String] = x => (x.toString).pure[Future]
  val a2: String => Future[Boolean] = x => (true).pure[Future]


//  a1.andThen(a2)


  /*
  val i = 10
  val a = for {
    str <- a1(10)
    bool <- a2(str)
  } yield bool


 val a3: Kleisli[Future, Int, Boolean] =  Kleisli(a1).andThen(a2)

 val a4: Int => Future[Boolean] = a3.run


  // 10 => a, b, c




  val foo: Int => Future[Int] = x => (x + 10).pure[Future]
  val bar: Int => IO[Int] = x => (x + 10).pure[IO]

  // 컴파일 에러
  // foo andThen(foo)
  // 합성이 되지 않는다.

  // 함수에 변수를 바인딩해서 flatMap으로 합성해야한다.
  // FP는 함수의 합성이라는데...
  def compose(x: Int): Future[Int] =
  for {
    a <- foo(x)
    b <- foo(a)
    c <- foo(b)
  } yield c

  // 합수를 합성하고 나중에 변수를 바인딩하려면?
  // Point free
  val res = Kleisli(foo) andThen foo andThen foo
  println(Await.result(res.run(10), Duration.Inf))

  // 병렬로 실행하고 싶으면?

  val value: Future[Int] = (Kleisli(foo), Kleisli(foo), Kleisli(foo)).mapN { _ + _ + _ }.run(10)
  println(Await.result(value, Duration.Inf))
  val value1: IO[Int] = (Kleisli(bar), Kleisli(bar), Kleisli(bar)).parMapN{ _ + _ + _ }.run(10)
  println(value1.unsafeRunSync())


  type Config = String
  def abc(a: Int): Kleisli[Future, Config, Boolean] =
    Kleisli[Future, Config, Boolean]((config: Config) =>
      ???
    )

*/

}
