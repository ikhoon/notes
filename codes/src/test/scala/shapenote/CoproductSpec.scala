package shapenote
import org.scalatest.{Matchers, WordSpec}
import shapeless.{:+:, CNil, Coproduct}
import shapenote.coproduct.ISB

/**
  * Created by ikhoon on 2016. 8. 15..
  */
class CoproductSpec extends WordSpec with Matchers {

  "Coproduct" should {
    val isb = Coproduct[ISB]("foo")
    "Coproduct type is a generalization of scala Either" in {
      isb.select[Int] shouldBe None
      isb.select[String] shouldBe Some("foo")
    }

    "Coproduct supports mapping given a polymorphic function" in {
      val m = isb map sizeM
      m.select[(String, Int)] shouldBe Option("foo", 3)
    }

    "Add labels to the elements of a Coproduct" in {
      import shapeless.record._
      import shapeless.union._
      import shapeless.syntax.singleton._

      type U = Union.`'i -> Int, 's -> String, 'b -> Boolean`.T

      val u = Coproduct[U]('s ->> "foo")

      u.get('i) shouldBe None
      u.get('s) shouldBe Option("foo")
      u.get('b) shouldBe None

    }
  }

}
