package stdnote

import org.scalatest.{FunSuite, Matchers}

/**
 * Created by Liam.M(엄익훈) on 8/17/16.
 */
class MapSpec extends FunSuite with Matchers {

  test("Map") {
    // mutable, immutable
    // 1. Map (A,B)
    // 2. A -> B
    val map1 = Map[Int, String]((10, "십"), (1, "일"))
    val map2 = Map((10, "십"), (1, "일"))
    val map3 = Map(10 -> "십", 1 -> "일")

    map1 shouldBe map3

    val map4 = map1 + (2 -> "이")

    // map1 (1, 2, 10)
//    map1 shouldBe map4

    println(map1)
    println(map4)

    map4.get(10) shouldBe Some("십")
    map4.get(3) shouldBe None
    map4.getOrElse(4, "사") shouldBe "사"
    map1 ++ map4
  }


}
