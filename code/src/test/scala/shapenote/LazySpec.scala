package shapenote

import org.scalatest.{Matchers, WordSpec}

/**
 * Created by Liam.M(엄익훈) on 8/22/16.
 */
class LazySpec extends WordSpec with Matchers {

  "First class lazy value tie implicit recursive knots" should {
    "Lazy[T] with Cons" in {
      import Show._
      val l = Cons(1, Cons(2, Cons(3, Nil)))
      show(l) shouldBe "Cons(1, Cons(2, Cons(3, Nil)))"
    }
  }
}
