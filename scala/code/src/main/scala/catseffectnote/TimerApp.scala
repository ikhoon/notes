package catseffectnote

import java.util.Date

import cats.effect.IO
import cats.effect._

import scala.concurrent.duration._

object TimerApp extends IOApp {

  println(new Date())
  def run(args: List[String]): IO[ExitCode] = {
    println(new Date())
    val ioC = IO { println(new Date()) }
    for {
      _ <- IO.sleep(1.seconds)
      a <- IO.pure(10)
      c <- ioC
      d <- ioC
      b <- IO.pure(ExitCode.Success)
    } yield b
  }
}
