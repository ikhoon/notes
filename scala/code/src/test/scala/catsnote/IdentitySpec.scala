package catsnote

import cats.{Applicative, Comonad, Id, Monad}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

/**
  * Created by ikhoon on 2016. 7. 28..
  */
class IdentitySpec extends AnyWordSpec with Matchers {

  "Identity" should {
    "Id[A] is just a synonym for A" in {
      import cats._
      val x: Id[Int] = 10
      val y: Int = x
      x shouldBe 10
    }
  }

  "Id[A]는 어따 써먹을까?" should {

    /**
      * 1. Monad는 F[A] 타입만 받을수 있다.
      * 2. 그러면 Monad[Int]는 불가능하다.
      * 3. 하지만 Monad[Id]는 가능하다! 그러면 Id[Int]를 통해서 Monad[Int]와 같은 효과를 낼수 있다.
      */
    "Monad[Id]" in {
      //안된다.
//       Monad[Int].map(1)(_ + 1)

      val one: Int = 1
      Monad[Id].map(one)(_ + 1) shouldBe 2
    }

    // Applicative도 F[A] 타입만 받을수 있다.
    "Applicative[Id]" in {
      Applicative[Id].pure(42) shouldBe 42
    }

    "Id[A]에겐 `flatMap`이나 `map`이나 같다." in {
      val one: Int = 1
      Monad[Id].map(one)(_ + 1) shouldBe Monad[Id].flatMap(one)(_ + 1)
    }

    "Id[A]에겐 `coflatMap`이나 `flatMap`이나" in {
      val one: Int = 1
      Comonad[Id].coflatMap(one)(_ + 1000) shouldBe Monad[Id].flatMap(one)(_ + 1000)

    }
  }

}
