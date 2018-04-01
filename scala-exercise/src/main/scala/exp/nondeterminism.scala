package exp

import java.time.LocalDateTime

import parallelExperiment.withTS

import scala.concurrent.Await
import scala.concurrent.duration._


object parallelExperiment {


  def main(args: Array[String]): Unit = {
    println("###### monix task ap - sequence")
    monixtask.runWithAp() // sequence
    println("###### monix task zip - parallel")
    monixtask.runWithZip() // parallel
    println("###### monix task nondeterminism ap - parallel")
    monixtask.runWithNondeterminismAp() // parallel

    println("###### scalaz task ap - sequence")
    scalaztask.runAp() // sequence
    println("###### scalaz task nondeterminsm - parallel")
    scalaztask.runNondeterminism() // parallel

    println("###### cats effect io ap")
    catseffect.runAp()
    println("###### cats effect io map2")
    catseffect.runMap2()

    println("###### cats effect io parMapN")
    catseffect.runParMapN()

    println("###### cats effect io monix instance")
    catseffect.runMonixInstance()
  }


  def withTS(block: => Unit) = {
    println(s"# Start  - ${LocalDateTime.now()}")
    block
    println(s"# Finish - ${LocalDateTime.now()}")
  }
}

/**
  * Created by ikhoon on 20/08/2017.
  */
object scalaztask {

  import scalaz.Applicative
  import scalaz.Nondeterminism
  import scalaz.concurrent.Task

  val ta: Task[Int] = scalaz.concurrent.Task {
    Thread.sleep(1000)
    1
  }
  val tb: Task[Int] = scalaz.concurrent.Task {
    Thread.sleep(1000)
    2
  }

  // sequence
  def runAp(): Unit = {
    withTS {
      val ap2 = Applicative[Task].apply2(ta, tb)(_ + _)
      ap2.unsafePerformSync
    }
  }

  // parallel
  def runNondeterminism(): Unit = {
    withTS {
      val both = Nondeterminism[Task].mapBoth(ta, tb)(_ + _)
      both.unsafePerformSync
    }
  }

}

object monixtask {

  import cats.Applicative
  import monix.eval.Task
  import monix.execution.Scheduler.Implicits.global
  val ta: Task[Int] = Task {
    Thread.sleep(1000)
    1
  }
  val tb: Task[Int] = Task {
    Thread.sleep(1000)
    2
  }

  // parallel
  def runWithZip() = {
    withTS {
      val tc : Task[Int] = ta.zipMap(tb)(_ + _)
      Await.result(tc.runAsync, 10 seconds)
    }
  }

  // sequence
  def runWithAp() = {
    withTS {
      val tc : Task[Int] = Applicative[Task].map2(ta, tb)(_ + _)
      Await.result(tc.runAsync, 10 seconds)
    }
  }

  // parallel
  def runWithNondeterminismAp() = {
    import monix.execution.Scheduler.Implicits.global
    import cats.implicits._
    import monix.eval.Task
    withTS {
      val tc : Task[Int] = Applicative[Task].map2(ta, tb)(_ + _)
      Await.result(tc.runAsync, 10 seconds)
    }
  }
}

object catseffect {

  import cats.effect.IO
//  import cats.effect.implicits._
  import cats.implicits._
  import cats.Applicative

  implicitly[Applicative[IO]]

  val ta = IO.async[Int] { cb =>
    Thread.sleep(1000)
    cb(Right(1))
  }
  val tb = IO.async[Int] { cb =>
    Thread.sleep(1000)
    cb(Right(2))
  }


  def runAp(): Unit = {
    withTS {
      val tc = (ta |@| tb).map(_ + _)
      tc.unsafeRunSync()
    }
  }

  def runMap2(): Unit = {
    withTS {
      val tc = ta.map2(tb)(_ + _)
      tc.unsafeRunSync()
    }
  }


  def runParMapN(): Unit = {
    withTS {
      val tc = (ta, tb).parMapN(_ + _)
      tc.unsafeRunSync()
    }
  }

  def runMonixInstance(): Unit = {
//    implicit val a = monix.eval.instances.ParallelApplicative
    withTS {
      val tc = ta.map2(tb)(_ + _)
      tc.unsafeRunSync()
    }

  }
}

