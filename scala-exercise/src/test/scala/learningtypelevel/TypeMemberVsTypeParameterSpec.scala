package learningtypelevel

import org.scalatest.{Matchers, WordSpec}

import scala.reflect.runtime.universe._


/**
  * Created by ikhoon on 2016. 8. 28..
  */
class TypeMemberVsTypeParameterSpec extends WordSpec with Matchers {

  "type member vs type parameter" should {

    def show[T : WeakTypeTag](any: T): Unit = println(s"${weakTypeOf[T]} : $any")

    "type member in tail" in {
      import MList._

      val nums = MCons(2, MCons(3, MNil())) : MCons { type T = Int }
      show(nums.head) // nums.T : 2

      val hd = nums.head
      show(hd + hd) // Int : 4

      val thd = nums.tail.uncons.map(_.head)
      show(thd) // Option[Int] : Some(3)

      show(thd.map(_ - 2)) // Option[Int] : Some(1)

    }

    "remove type member in tail" in {
      import MList2._
      val nums = MCons2(2, MCons2(3, MNil2())) : MCons2 { type T = Int }
      show(nums.head) // nums.T : 2

      val hd = nums.head
      show(hd + hd) // Int : 4

      val thd = nums.tail.uncons.map(_.head)
      show(thd) // Option[Int] : Some(3)

      // MCons의 버전과는 달리 MCons2의 버전은 컴파일 되지 않는다. tail에서 head를 읽어올때 { type T = Int }의 타입정보를 잃어버렸다.
      assertDoesNotCompile(
        """
          | show(thd.map(_ - 2))
        """.stripMargin) // Error:(44, 22) value - is not a member of nums.tail.T
    }

    "existential method" in {
      import Existential._
      import MList2._
      val mnums = MCons2(2, MCons2(3, MNil2())) : MCons2 { type T = Int }
      mlength(mnums) shouldBe 2

      val pnums = PCons(1, PCons(2, PCons(3, PNil())))
      plengthT(pnums) shouldBe 3
      plengthE(pnums) shouldBe 3
    }
  }

}
