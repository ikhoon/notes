package shapenote

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import shapeless.{HNil, Poly1}
import shapeless.PolyDefns.~>

/**
  * Created by ikhoon on 2016. 7. 28..
  */
class PolymorphicSpec extends AnyWordSpec with Matchers {

  "Function level polymorphism" should {
    "it is ok." in {
      def choose[T](s: Set[T]): Option[T] = s.headOption
      List(Set(1), Set(2), Set(3)).map(choose) shouldBe List(Option(1), Option(2), Option(3))
    }
    "it is ok too?" in {
      def choose[T](s: Set[T]): Option[T] = s.headOption
      val chooseFn = choose _
//      List(Set(1), Set(2), Set(3)) map chooseFn shouldBe List(Option(1), Option(2), Option(3))
    }

    "it is not ok too." in {
      def choose[T](s: Set[T]): Option[T] = s.headOption
      val hlist = Set(1) :: Set("2") :: Set(3L) :: HNil
//      hlist.map(choose) shouldBe Option(1) :: Option("2") :: Option(3L) :: HNil
    }
    "Set to Option with no type specific cases" in {
      object choose extends (Set ~> Option) {
        override def apply[T](f: Set[T]): Option[T] = f.headOption
      }

      import shapeless._
      import poly._
      choose(Set(1, 2, 3)) shouldBe Option(1)

      List(Set(1), Set(2), Set(3)).map(choose) shouldBe List(Option(1), Option(2), Option(3))

    }

    "more general than natural transformations" in {
      object size extends Poly1 {
        implicit def caseInt = at[Int](x => 1)
        implicit def caseString = at[String](_.length)
        implicit def caseTuple[T, U](implicit st: Case.Aux[T, Int], su: Case.Aux[U, Int]) =
          at[(T, U)](t => size(t._1) + size(t._2))
      }

      size(23) shouldBe 1
      size("foo") shouldBe 3
      size((23, "foo")) shouldBe 4
      size(((23, "foo"), 13)) shouldBe 5
    }
  }
}
