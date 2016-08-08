package shapenote


import org.scalatest.{Matchers, WordSpec}
import shapeless._
import shapeless.poly._

object size extends Poly1 {
  implicit def caseInt = at[Int](x => 1)
  implicit def caseString = at[String](_.length)
  implicit def caseTuple[T, U](implicit st: Case.Aux[T, Int], su: Case.Aux[U, Int]) =
    at[(T, U)](t => size(t._1) + size(t._2))
}

object addSize extends Poly2 {
  implicit def default[T](implicit st: shapenote.size.Case.Aux[T, Int]): addSize.Case[Int, T] = {
    at[Int, T] { case (acc: Int, t) => acc + size(t) }
  }
}

/**
  * Created by ikhoon on 2016. 8. 8..
  */
class HeterogenousListSpec extends WordSpec with Matchers {

  "Heterogenous List" should {
    "map" in {
      object choose extends (Set ~> Option) {
        override def apply[T](f: Set[T]): Option[T] = f.headOption
      }

      val sets = Set(1) :: Set("foo") :: Set(true) :: HNil

      val opts = sets map choose

      opts shouldBe (Option(1) :: Option("foo") :: Option(true) :: HNil)

    }

    "flatMap" in {
      import shapeless.poly.identity
      val l = (1 :: "foo" :: HNil) :: HNil :: (true :: HNil) :: HNil
      l.flatMap(identity) shouldBe 1 :: "foo" :: true :: HNil
    }

    "fold" in {

      val l = 1 :: "foo" :: HNil
      l.foldLeft[Int](0)(addSize)
    }
  }

}
