package shapenote

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import shapeless.TypeCase

/**
  * Created by Liam.M(엄익훈) on 8/23/16.
  */
class TypeableSpec extends AnyWordSpec with Matchers {

  "Typeable type class which provides a type safe cast operation" should {
    "`cast` return `Option` of target type" in {
      import shapeless.syntax.typeable._
      // 타입 정보를 없애버린다.
      val l: Any = List(Vector("foo", "bar", "baz"), Vector("wibble"))

      l.cast[List[Vector[String]]] shouldBe Some(List(Vector("foo", "bar", "baz"), Vector("wibble")))
      l.cast[List[Vector[Int]]] shouldBe None
      l.cast[List[List[String]]] shouldBe None
    }

    "An extractor based on Typeable" in {
      val `List[String]` = TypeCase[List[String]]
      val `List[Int]` = TypeCase[List[Int]]

      val l = List(1, 2, 3)

      val result = (l: Any) match {
        case `List[String]`(List(s, _*)) => s.length
        case `List[Int]`(List(i, _*))    => i + 1
      }

      result shouldBe 2
    }
  }

}
