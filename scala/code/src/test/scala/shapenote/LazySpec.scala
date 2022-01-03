package shapenote

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

/**
  * Created by Liam.M(엄익훈) on 8/22/16.
  */
class LazySpec extends AnyWordSpec with Matchers {

  "First class lazy value tie implicit recursive knots" should {
    "Lazy[T] with Cons" in {
      import Show._
      val l = Cons(1, Cons(2, Cons(3, Nil)))
      show(l) shouldBe "Cons(1, Cons(2, Cons(3, Nil)))"
    }
  }
}
