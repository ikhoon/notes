package shapenote

import org.scalatest.{Matchers, WordSpec}

/**
 * Created by Liam.M(엄익훈) on 8/20/16.
 */
class TypeclassDerivationSpec extends WordSpec with Matchers {

  "Auto Typeclass Derivation" should {
    "monoid for case class" in {
      case class Foo(i: Int, s: String)
      case class Bar(b: Boolean, s: String, d: Double)

      import MonoidSyntax._

      val fooCombined = Foo(1, "foo") |+| Foo(10, "bar")
      fooCombined shouldBe Foo(11, "foobar")

      val barCombined = Bar(true, "foo", 2.0) |+| Bar(false, "bar", 3.0)
      barCombined shouldBe Bar(true, "foobar", 5.0)


    }
  }

}
