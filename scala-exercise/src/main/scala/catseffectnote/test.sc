import java.time.LocalDateTime

import cats.data.Nested
import cats.implicits._
import monix.execution.Scheduler.Implicits.global
import cats.effect._
import cats._
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.Await
import scala.concurrent.duration.Duration

val xs = (1 to 10).toList
def slow[F[_]: Effect](x: Int): F[Int] = {
  implicitly[Effect[F]].delay {
    Thread.sleep(300)
    println(s"${LocalDateTime.now()} : x")
    x
  }
}

// 순차(당연하긴 함?!?)
val ys1 = xs.parTraverse(slow[IO])
ys1.unsafeRunSync()

// 순차
val ys2: IO[List[Int]] = IO.shift *> xs.parTraverse(slow[IO])
ys2.unsafeRunSync()

// 병렬, parallel
val ys3 = xs.parTraverse(IO.shift *> slow[IO](_))
ys3.unsafeRunSync()

val ys4 = xs.parTraverse(slow[Task])
Await.result(ys4.runAsync, Duration.Inf)

Scheduler.io().timer
val ys5 = Timer[Task] *> xs.parTraverse(slow[Task])
Await.result(ys5.runAsync, Duration.Inf)
