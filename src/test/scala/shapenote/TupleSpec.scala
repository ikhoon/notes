package shapenote

import org.scalatest.{Matchers, WordSpec}

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

  }

}
