package catsnote

import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/**
  * Created by Liam.M on 2018. 04. 11..
  */
object ParallelTest extends App {

  import cats.implicits._
  val xs = (1 to 100).toList

  def toStr1(x: Int): Future[String] = Future {
    Thread.sleep(1000)
    println(s"toString 1 ${Instant.now}")
    x.toString
  }

//  def toStr2(x: Int): Task[String] = Task {
//    Thread.sleep(1000)
//    println(s"toString 2 ${Instant.now}" )
//    x.toString
//  }

//  val result1: Future[List[String]] = xs.traverse(toStr1)
//  Await.result(result1, Duration.Inf)

//  val result2: Task[List[String]] = xs.traverse(toStr2)
//  Await.result(result2.runAsync, Duration.Inf)
//  val result1: Future[List[String]] = xs.parTraverse(toStr1)
//  Await.result(result1, Duration.Inf)
//  val result2: Task[List[String]] = xs.parTraverse(toStr2)
//  Await.result(result2.runToFuture, Duration.Inf)

}
