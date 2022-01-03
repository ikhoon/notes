package catsnote

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import cats.implicits._
import cats.syntax._
import cats.data._

import scala.collection.immutable.Seq

/**
  * Created by ikhoon on 08/12/2016.
  */
class SetSpec extends AnyWordSpec with Matchers {

  def intersect[A, B](as: List[A], bs: List[B]): List[(A, B)] =
    for {
      a <- as
      b <- bs if a == b
    } yield (a, b)

  "set" should {
    "zip with intersect" in {
      val as = List(1, 2, 3)
      val bs = List(2, 3, 4, 5)
      val inter = intersect(as, bs)
      inter shouldBe List((2, 2), (3, 3))
    }

    def diff[A, B](as: List[A], bs: List[B]): List[(Option[A], Option[B])] =
      for {
        a <- as
        b = bs.find(_ == a) if b.isEmpty
      } yield (a.some, b)

    "zip with diff" in {
      val as = List(1, 2, 3)
      val bs = List(2, 3, 4, 5)
      val difference = diff(as, bs)
      difference shouldBe List((Some(1), None))
    }

    def union[A, B](as: List[A], bs: List[B]): List[(Option[A], Option[B])] =
      intersect(as, bs).map { case (a, b) => a.some -> b.some } ++ diff(as, bs) ++ diff(bs, as).map(_.swap)

    "zip with union" in {
      val as = List(1, 2, 3)
      val bs = List(2, 3, 4, 5)
      // intersect + (a - b) + (b - a)
      union(as, bs) shouldBe List(
        (Some(2), Some(2)),
        (Some(3), Some(3)),
        (Some(1), None),
        (None, Some(4)),
        (None, Some(5))
      )

    }
  }

}
