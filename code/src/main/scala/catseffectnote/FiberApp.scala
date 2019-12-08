package catseffectnote

import java.util.concurrent.Executors

import cats.effect._

import scala.concurrent.ExecutionContext
object FiberApp extends App {
  val ecOne = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
  val ecTwo = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  def infiniteIO(id: Int)(cs: ContextShift[IO]): IO[Fiber[IO, Unit]] = {
    def repeat: IO[Unit] = IO(println(id)).flatMap(_ => repeat)
    repeat.start(cs)
  }
}
