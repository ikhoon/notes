package shapenote

import org.scalatest.{Matchers, WordSpec}
import shapeless.ops.tuple.FlatMapper
import shapeless.poly.~>
import shapeless.{HNil, Id, Poly1}

/**
  * Created by ikhoon on 2016. 8. 10..
  */
class TupleSpec extends WordSpec with Matchers {

  "hlist style operation on scala standard tuples" should {
    import shapeless.syntax.std.tuple._
    "head" in {
      (1, "foo", 3.0).head shouldBe 1
    }

    "tail" in {
      (2, "foo", true).tail shouldBe ("foo", true)
    }

    "drop" in {
      (2, "foo", true).drop(2) shouldBe Tuple1[Boolean](true)
    }

    "take" in {
      (2, "foo", true).take(2) shouldBe (2, "foo")
    }

    "split" in {
      (2, "foo", true).split(1) shouldBe (Tuple1(2), ("foo", true))
    }

    "prepend" in {
      val l = 23 +: ("foo", true)
      l.shouldBe (23, "foo", true)
    }

    "append" in {
      val l = (2, "foo") :+ true
      l.shouldBe (2, "foo", true)
    }

    "concatenate" in {
      val l = (2, "foo") ++ (true, 2.0)
      l.shouldBe (2, "foo", true, 2.0)
    }

    "map" in {
      object option extends (Id ~> Option) {
        override def apply[T](f: T): Option[T] = Option(f)
      }
      val l = (23, "foo", true) map option
      l shouldBe (Some(23), Some("foo"), Some(true))
    }


    "flatMap" in {
      import shapeless.poly.identity
      val l = ((22, "foo"), (), (true, 2.0)) flatMap identity
      l shouldBe (22, "foo", true, 2.0)
    }

    "fold" in {
       (23, "foo", (13, "wibble")).foldLeft(0)(addSize) shouldBe 11
    }

    "to hlist" in {
      (23, "foo", true).productElements shouldBe 23 :: "foo" :: true :: HNil
    }
    "to list" in {
      (23, "foo", true).toList shouldBe List(23, "foo", true)
    }

    "zipper" in {
      import shapeless.syntax.zipper._
      val l = (23, ("foo", true), 2.0).toZipper.right.down.put("bar").root.reify
      l shouldBe (23, ("bar", true), 2.0)
    }

  }

}
