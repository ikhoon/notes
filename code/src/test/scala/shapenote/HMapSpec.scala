package shapenote

import org.scalatest.{Matchers, WordSpec}
import shapeless.{HMap, HNil}

/**
  * Created by ikhoon on 2016. 8. 14..
  */
class HMapSpec extends WordSpec with Matchers {

  "Heterogenous maps" should {

    class BiMapIS[K, V]
    implicit val intToString = new BiMapIS[Int, String]
    implicit val stringToInt = new BiMapIS[String, Int]

    val hm = HMap[BiMapIS](23 -> "foo", "bar" -> 13)

    // does not compile
    // val hm2 = HMap[BiMapIS](23 -> "foo", 22 -> 1)

    "arbitrary key and corresponding value type" in {

      hm.get(23) shouldBe Option("foo")
      hm.get("bar") shouldBe Option(13)
    }

    "map" in {
      import hm._
      val l = 23 :: "bar" :: HNil
      val m = l map hm
      m shouldBe "foo" :: 13 :: HNil

    }


  }

}
