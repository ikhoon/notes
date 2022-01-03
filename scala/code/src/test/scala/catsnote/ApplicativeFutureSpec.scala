package catsnote

import cats.Apply
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.{Await, Future}
import cats.syntax._
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by ikhoon on 12/12/2016.
  */
class ApplicativeFutureSpec extends AnyWordSpec with Matchers {

  "applicative future" should {

    "join future with map3" in {
      val a = Future { List(1) }
      val b = Future { List(2, 3) }
      val c = Future { List(4, 5, 6) }

      val abc = Apply[Future].map3(a, b, c) { _ ++ _ ++ _ }

      Await.result(abc, Duration.Inf) shouldBe List(1, 2, 3, 4, 5, 6)

    }

    "join future with ap3" in {
      val a = Future { List(1) }
      val b = Future { List(2, 3) }
      val c = Future { List(4, 5, 6) }

      val f = (x: List[Int], y: List[Int], z: List[Int]) => x ++ y ++ z
      val abc = Apply[Future].ap3(Future(f))(a, b, c)

      Await.result(abc, Duration.Inf) shouldBe List(1, 2, 3, 4, 5, 6)
    }

    "join futures with apply builder" in {

      val a = Future { List(1) }
      val b = Future { List(2, 3) }
      val c = Future { List(4, 5, 6) }

      val abc = (a, b, c).mapN { _ ++ _ ++ _ }

      Await.result(abc, Duration.Inf) shouldBe List(1, 2, 3, 4, 5, 6)

    }
  }
}
