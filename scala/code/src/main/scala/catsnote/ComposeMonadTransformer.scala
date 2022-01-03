package catsnote

import cats.data.{OptionT, WriterT}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import cats.implicits._

/**
  * Created by ikhoon on 19/01/2018.
  */
object WriterTAndOptionT extends App {

  // WriterT monad
  val wt1: WriterT[Future, List[String], Int] = {
    for {
      a <- WriterT(Future((List("hello"), 10)))
      b <- WriterT.put[Future, List[String], Int](20)(List("world"))
      c <- 30.pure[WriterT[Future, List[String], *]]
      d <- WriterT.putT(Future(40))(List("good"))
      e <- WriterT.tell[Future, List[String]](List("morning"))
    } yield a + b + c + d
  }
  println(Await.result(wt1.run, Duration.Inf))

  // WriterT with OptionTFuture
  type WriterTOptionT[F[_], L, V] = WriterT[OptionT[F, *], L, V]
  type ListInt = List[Int]

  val wt2: WriterTOptionT[Future, List[String], Int] = {
    for {
      a <- WriterT[OptionT[Future, *], List[String], Int](OptionT(Future(Option(List("hello"), 1))))
      b <- WriterT.put[OptionT[Future, *], List[String], Int](20)(List("world"))
      c <- 30.pure[WriterTOptionT[Future, List[String], *]]
      d <- WriterT.putT(OptionT.pure[Future](40))(List("good"))
      e <- WriterT.tell[OptionT[Future, *], List[String]](List("morning"))
      f <- WriterT.liftF[OptionT[Future, *], List[String], Int](OptionT(Future(Option(50))))
    } yield a + b + c + d + f
  }
  println(Await.result(wt2.run.value, Duration.Inf))
}
