package catseffectnote

import java.util.concurrent.Executors

import cats.effect._

import scala.concurrent.ExecutionContext
object FiberApp extends App {
  val ecOne = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
  val ecTwo = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  def infiniteIO(id: Int): IO[Fiber[IO, Throwable, Unit]] = {
    def repeat: IO[Unit] = IO(println(id)).flatMap(_ => repeat)
    repeat.start
  }
}
