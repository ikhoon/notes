package hackerrank
import org.scalatest.{FunSuite, Matchers}

/**
 * Created by Liam.M on 2019. 01. 18..
 */

class SwapNodesTest extends FunSuite with Matchers {

  test(
    """
      |3
      |2 3
      |-1 -1
      |-1 -1
      |2
      |1
      |1
    """.stripMargin) {
    val indexes:Array[Array[Int]] = Array(
      Array(2, 3),
      Array(-1, -1),
      Array(-1, -1),
    )

    val queries: Array[Int] = Array(
      1,
      1
    )

    val expected = Array(
      Array(3, 1, 2),
      Array(2, 1, 3)
    )

    val xxs: List[List[Int]] = SwapNodes.swapNodes(indexes, queries)
    val res = xxs.map(_.mkString(" ")).mkString("\n")
    println(res)
  }

  test( " 5 2 3 -1 4 -1 5 -1 -1 -1 -1 1 2 ") {
    val indexes = Array(
      Array(2 , 3),
      Array(-1, 4),
      Array(-1, 5),
      Array(-1, -1),
      Array(-1, -1),
    )
    val queries: Array[Int] = Array(
      2
    )

    val xxs: List[List[Int]] = SwapNodes.swapNodes(indexes, queries)
    val res = xxs.map(_.mkString(" ")).mkString("\n")
    println(res)

  }
  test("dfs") {
    val indexes:Array[Array[Int]] = Array(
      Array(2, 3),
      Array(-1, -1),
      Array(-1, -1),
    )

//    SwapNodes.dfs(indexes)
    println("------")

    val indexes2 = Array(
      Array(2, 3),
      Array(-1, 4),
      Array(-1, 5),
      Array(-1, -1),
      Array(-1, -1),
    )
    println(SwapNodes.dfs(indexes2))

  }
}
