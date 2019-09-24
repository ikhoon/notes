package catsnote

import org.scalatest.{Matchers, WordSpec}
import cats.Semigroup
import cats.implicits._

/**
  * Created by ikhoon on 2016. 7. 18..
  */
class SemigroupSpec extends WordSpec with Matchers{

  "semigroup" should {
    "combine (a, b)" in {

      Semigroup[Int].combine(1, 2) shouldBe 3
      Semigroup[List[Int]].combine(List(1,2,3), List(4,5,6)) shouldBe List(1,2,3,4,5,6)
      Semigroup[Option[Int]].combine(Option(1), Option(2)) shouldBe Option(3)
      Semigroup[Option[Int]].combine(Option(1), None) shouldBe Option(1)
      Semigroup[Int => Int].combine({ x: Int => x + 10}, { x: Int => x * 10})(10) shouldBe 120
    }
    "implicits" in {
      1.combine(2) shouldBe 3
      List(1,2,3).combine(List(4,5,6)) shouldBe List(1,2,3,4,5,6)
      Option(1).combine(Option(2)) shouldBe Option(3)
      Option(1).combine(None) shouldBe Option(1)
      ({ (x: Int) => x + 10}).combine({ x: Int => x * 10})(10) shouldBe 120
    }

    "useful" in {
      Map("foo" -> Map("bar" -> 1)).combine(Map("foo" -> Map("bar" -> 2), "baz" -> Map())) shouldBe
        Map("foo" -> Map("bar" -> 3), "baz" -> Map())
      Map("foo" -> List(1,2)).combine(Map("foo" -> List(3,4), "bar" -> List(42))) shouldBe
        Map("foo" -> List(1,2,3,4), "bar" -> List(42))
    }

    "inline" in {
      1 |+| 2 shouldBe 3
      Option(1) |+| Option(3) shouldBe Option(4)
      Option(1) |+| None shouldBe Option(1)
      List(1,2,3) |+| List(4,5,6) shouldBe List(1,2,3,4,5,6)
    }
  }
}
