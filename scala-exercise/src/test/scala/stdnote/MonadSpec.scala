package stdnote

import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import cats.implicits._
import cats.syntax.all._
/**
  * Created by ikhoon on 2017. 3. 30..
  */
class MonadSpec extends FunSuite with Matchers {

  test("future monad") {
    val fa : Future[Int] = Future {
      println("fa run")
      1
    }

    def asyncIncrement(x: Int): Future[Int] = Future {
      println(s"async $x + 1")
      x + 1
    }
    def syncIncrement(x: Int): Int = {
      println(s"sync $x + 1")
      x + 1
    }

    // fa 를 1을 증가 해봅니다.
    // Future[Int]

    // fa = Future { 1 } => asyncIncrement =>  fb = Future { 2 }

    val fb : Future[Int] = fa.flatMap(int => asyncIncrement(int))

    println(Await.result(fb, Duration.Inf))

    val fc: Future[Future[Int]] = fa.map(int => asyncIncrement(int))
    val result: Int =
      Await.result(
        Await.result(fc, Duration.Inf),
        Duration.Inf
      )
    println(result)

    val fd: Future[Int] = fa.map(int => syncIncrement(int))

//    val fe: Future[Int] = fa.map(int => asyncIncrement(int)).flatten

    // flatMap => map + flatten
  }

  test("fake future") {

    import scala.collection.immutable.{ List => Future }
    val fa : Future[Int] = Future {
      println("fa run")
      1
    }

    def asyncIncrement(x: Int): Future[Int] = Future {
      println(s"async $x + 1")
      x + 1
    }
    def syncIncrement(x: Int): Int = {
      println(s"sync $x + 1")
      x + 1
    }
    val fc: Future[Future[Int]] = fa.map(int => asyncIncrement(int))
    val fb : Future[Int] = fa.flatMap(int => asyncIncrement(int))
    println(fb)
    println(fc)

  }


}
