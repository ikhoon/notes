package shapenote

import org.scalatest.{Matchers, WordSpec}
import shapeless.HNil

class HListSpec extends WordSpec with Matchers {
  "hlist" should {
    "trait" in {
      trait Foo[A] {
        def hello(a: A): A
      }
      new Foo[Int] {
        override def hello(a: Int): Int = ???
      }
      new Foo[String] {
        override def hello(a: String): String = ???
      }
    }

    def hello(b: Double): Int = b.toInt

    import shapeless._
    "hlist" in {
//      HList <=> tuple
//      HList <=> case class
      // reflection 지양
      // 퍼포먼스
      // 타입 safe하지 못하다
      // 런타임, method
      // meta 프로그래밍
      // setting?
      // 코드를 만드는 코드
      // meta => god
      val hlist = 1 :: "abc" :: Some(123) :: HNil

    }
  }
}
