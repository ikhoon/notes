package catsnote

import java.time.Instant

import monix.eval.Task

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by Liam.M on 2018. 04. 10..
  */
object LiveCodeMonad extends App {

  trait Monad[F[_]] {
    def pure[A](a: A): F[A]

    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

    def map[A, B](fa: F[A])(f: A => B): F[B] =
      flatMap(fa)(f andThen pure)

    def ap[A, B](fa: F[A])(ff: F[A => B]): F[B] = ???

    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = ???

  }
  import monix.execution.Scheduler.Implicits.global
  val task1 = Task {
    Thread.sleep(1000)
    println(s"xxxxx ${Instant.now}")
    1
  }

  val task2 = Task {
    Thread.sleep(1000)
    println(s"yyyy ${Instant.now}")
    2
  }

  import cats.implicits._
  val x =(task1, task2).parMapN((x, y) => {
    println(x + y)
  })
  println(s"wwww ${Instant.now}")
  val y = x.runAsync
  Await.result(y, Duration.Inf)
}
