package shapenote

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import shapeless.{HNil, Witness, WitnessWith}

/**
  * Created by ikhoon on 2016. 8. 14..
  */
class SingletonTypedSpec extends AnyWordSpec with Matchers {

  "Singleton-typed literals" should {

    "use Int literal index into HList and tuples" in {
      import shapeless.syntax.std.tuple._

      val l = 23 :: "foo" :: true :: HNil
      l(1) shouldBe "foo"

      val t = (23, "foo", true)
      t(1) shouldBe "foo"
    }

    "other" in {
      import shapeless._, syntax.singleton._

      Witness(23).value == 23 shouldBe true
      Witness("foo").value == "foo" shouldBe true
    }

    "witness" in {
      val (wTrue, wFalse) = (Witness(true), Witness(false))
      type True = wTrue.T
      type False = wFalse.T

      trait Select[B] { type Out }

      implicit val selInt = new Select[True] { type Out = Int }
      implicit val selString = new Select[False] { type Out = String }

      def select(b: WitnessWith[Select])(t: b.instance.Out) = t

      select(true)(12) shouldBe 12
      // select(true)("foo")  // 컴파일 안됨
      select(false)("foo") shouldBe "foo"
      // select(false)(12)    // 이것도 컴파일 안됨

    }
  }

}
