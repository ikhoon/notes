package monixnote

import monix.reactive.Observable

import scala.concurrent.duration._
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await

/**
  * Created by ikhoon on 06/08/2018.
  */
object MonixApp {
  def main(args: Array[String]): Unit = {
    val unit = Observable
      .interval(100.millis)
      .bufferTumbling(10)
      .foreach(println)
    Await.result(unit, Duration.Inf)

  }
}
