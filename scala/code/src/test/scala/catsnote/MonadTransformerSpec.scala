package catsnote

import cats.Functor
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scalaz.ListT

class MonadTransformerSpec extends AnyWordSpec with Matchers {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scalaz._, Scalaz._
  "map list" should {
    "list concat" in {
      val a = Future.successful(IList(1, 2))
      val b = Future.successful(IList(3, 4))
      val run1: Future[IList[Int]] = (ListT(a) ++ ListT(b)).run
      println(Await.result(run1, Duration.Inf))
    }
  }
}
