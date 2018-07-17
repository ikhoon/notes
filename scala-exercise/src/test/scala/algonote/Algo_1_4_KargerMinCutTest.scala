package algonote

import org.scalatest.{FunSuite, Matchers}

/**
  * Created by ikhoon on 15/07/2018.
  */
class Algo_1_4_KargerMinCutTest extends FunSuite with Matchers {

  import Algo_1_4_KargerMinCut._
  test("build graph") {
    val matrix =
      """
        |1 2 3
        |2 1 3 4
        |3 1 2 4
        |4 2 3
      """.stripMargin
        .split("\n")


    val karger = Algo_1_4_KargerMinCut.build(matrix)
    karger.graph.size shouldBe 4
    karger.graph(1) shouldBe Set(2, 3)
    karger.graph(3) shouldBe Set(1, 2, 4)

  }

  /**
    *  1 --- 2
    *  |  /  |
    *  | /   |
    *  3 --- 4
    */
  test("find min") {
    val matrix =
      """
        |1 2 3
        |2 1 3 4
        |3 1 2 4
        |4 2 3
      """.stripMargin
        .split("\n")


    val karger = Algo_1_4_KargerMinCut.build(matrix)
    (1 to 100).foreach { i =>
      val cuts = karger.findMinCut()
      println(s"$i cuts = ${cuts}")
    }
  }

  test("string split") {
    val str = "1 2  3 4"
    str.split("\\s+").toList shouldBe List("1", "2", "3", "4")
  }
}
