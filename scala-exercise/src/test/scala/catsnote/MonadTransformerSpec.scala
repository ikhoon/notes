package catsnote

import cats.Functor
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scalaz.ListT

class MonadTransformerSpec extends WordSpec with Matchers {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scalaz._, Scalaz._
  "map list" should {
    "list concat" in {
      val a = Future.successful(List(1, 2))
      val b = Future.successful(List(3, 4))
      val run1: Future[List[Int]] = (ListT(a) ++ ListT(b)).run
      println(Await.result(run1, Duration.Inf))
    }
  }
}
