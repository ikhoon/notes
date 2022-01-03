package hackerrank
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by Liam.M on 2019. 01. 18..
  */
class SwapNodesTest extends AnyFunSuite with Matchers {

  test("""
      |3
      |2 3
      |-1 -1
      |-1 -1
      |2
      |1
      |1
    """.stripMargin) {
    val indexes: Array[Array[Int]] = Array(
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

    val xxs: Array[Array[Int]] = SwapNodes.swapNodes(indexes, queries)
    val res = xxs.map(_.mkString(" ")).mkString("\n")
    println(res)
  }

  test(" 5 2 3 -1 4 -1 5 -1 -1 -1 -1 1 2 ") {
    val indexes = Array(
      Array(2, 3),
      Array(-1, 4),
      Array(-1, 5),
      Array(-1, -1),
      Array(-1, -1),
    )
    val queries: Array[Int] = Array(
      2
    )

    val xxs: Array[Array[Int]] = SwapNodes.swapNodes(indexes, queries)
    val res = xxs.map(_.mkString(" ")).mkString("\n")
    println(res)

  }

  test("2 3\n4 -1\n5 -1\n6 -1\n7 8\n-1 9\n-1 -1\n10 11\n-1 -1\n-1 -1\n-1 -1") {
    val indexes = Array(
      Array(2, 3),
      Array(4, -1),
      Array(5, -1),
      Array(6, -1),
      Array(7, 8),
      Array(-1, 9),
      Array(-1, -1),
      Array(10, 11),
      Array(-1, -1),
      Array(-1, -1),
      Array(-1, -1),
    )
    val queries = Array(
      2,
      4
    )

    val xxs: Array[Array[Int]] = SwapNodes.swapNodes(indexes, queries)
    val res = xxs.map(_.mkString(" ")).mkString("\n")
    println(res)
  }
  test("dfs") {
    val indexes: Array[Array[Int]] = Array(
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
