package algonote

import org.scalatest.{FunSuite, Matchers}

/**
  * Created by ikhoon on 30/07/2018.
  */
class Algo_2_1_StrongConnectedComponentsTest extends FunSuite with Matchers {

  import Algo_2_1_StrongConnectedComponents._

  /**
    * 1 → 2 → 5
    * ↓ ↘︎ ↓
    * 3 → 4
    */
  test("reverse graph") {
    val graph: Graph = Map(
      1 -> Vector(2, 3, 4),
      2 -> Vector(4, 5),
      3 -> Vector(4)
    )

    val revervsed: Graph = Map(
      2 -> Vector(1),
      3 -> Vector(1),
      4 -> Vector(1, 2, 3),
      5 -> Vector(2)
    )

    rev(graph) shouldBe revervsed
  }


  test("build") {
    val graph: Graph = Map(
      1 -> Vector(2, 3, 4),
      2 -> Vector(4, 5),
      3 -> Vector(4)
    )
    val inputs =
      """1 2
        |1 3
        |1 4
        |2 4
        |2 5
        |3 4
      """.stripMargin
    build(inputs.split("\n")) shouldBe graph
  }

  test("dfsLoop") {
    
  }
}
