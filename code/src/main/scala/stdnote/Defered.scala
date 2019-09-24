package stdnote

import java.util.{Timer, TimerTask}

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object Deferred {
  def sleep(howlong: Duration)(implicit timer: Timer): Future[Unit] = {
    if (howlong <= Duration.Zero) Future.unit
    else {
      val promise = Promise[Unit]()
      timer.schedule(new TimerTask {
        def run(): Unit = promise.success(())
      }, howlong.toMillis)
      promise.future
    }
  }
}

object DelayedApp {
  def main(args: Array[String]): Unit = {
    implicit val timer = new Timer("timer")
    val value = for {
      _ <- Deferred.sleep(10.seconds)
      x <- Future.successful("defer")
    } yield x
    Await.result(value, Duration.Inf)
    timer.cancel()
  }
}

