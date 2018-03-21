package catseffectnote

import java.util.Date

import scala.concurrent.Future

/**
  * Created by Liam.M on 2018. 03. 19..
  */
object Parallel extends App {

  import cats.implicits._
  import cats.effect.IO
  import cats.effect.Timer
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val ioTimer = Timer[IO]

  val ioA = Timer[IO].sleep(1.seconds) *> IO(println("Delayed!"))
  // ioA: cats.effect.IO[Unit] = <function1>

  val ioB = IO({
    Thread.sleep(5000)
    println("Running ioB")
  })
  // ioB: cats.effect.IO[Unit] = <function1>

  val ioC = IO({
    Thread.sleep(5000)
    println("Running ioC")
  })
  // ioC: cats.effect.IO[Unit] = <function1>

  val program = (ioA, ioB, ioC).parMapN { (_, _, _) => () }
  // program: cats.effect.IO[Unit] = <function1>

  println(s"${new Date()}")
  program.unsafeRunSync()
  println(s"${new Date()}")
  val a = Future { 1 }
}
