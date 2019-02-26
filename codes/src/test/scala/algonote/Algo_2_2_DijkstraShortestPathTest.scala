package algonote

/**
import algonote.Algo_2_2_DijkstraShortestPath.Graph
import Algo_2_2_DijkstraShortestPath.Node
import org.scalatest.{FunSuite, Matchers}

/**
  * Created by ikhoon on 12/08/2018.
  */
class Algo_2_2_DijkstraShortestPathTest extends FunSuite with Matchers {

  val dijkstra = new Algo_2_2_DijkstraShortestPath()

  test("transpose") {
    val graph: Graph = Map(
      1 -> Vector(Node(2, 1), Node(3, 4)),
      2 -> Vector(Node(3, 2), Node(4, 6)),
      3 -> Vector(Node(4, 6)),
      4 -> Vector.empty
    )

    val trans = dijkstra.transpose(graph)
    trans.foreach(println)
  }

  /**
    * 1 - (1) -> 2
    * |     /   |
    *(4)  (2)  (6)
    * |  /      |
    * ⌄⌞        ⌄
    * 3 - (3) -> 4
    */
  test("simple shortest path") {
    val graph: Graph = Map(
      1 -> Vector(Node(2, 1), Node(3, 4)),
      2 -> Vector(Node(3, 2), Node(4, 6)),
      3 -> Vector(Node(4, 3)),
      4 -> Vector.empty
    )

    val (distance, path) = dijkstra.shortestPath(graph, 1)
    val expectDistance = Map(
      1 -> 0,
      2 -> 1,
      3 -> 3,
      4 -> 6
    )
    val expectedPath = Map(
      1 -> Vector(1),
      2 -> Vector(1, 2),
      3 -> Vector(1, 2, 3),
      4 -> Vector(1, 2, 3, 4)
    )

    distance shouldBe expectDistance
    path shouldBe expectedPath
  }

  test("wiki example") {
    val text =
      """1 2,4 3,2
        |2 3,5 4,10
        |3 5,3
        |4 6,11
        |5 4,4
      """.stripMargin
    val inputs = text.split("\n")
    val graph = dijkstra.build(inputs)
    val (distance, path) = dijkstra.shortestPath(graph, 1)
    println(distance)
    println("path")
    path.foreach(println)
    path(6) shouldBe Vector(1, 3, 5, 4, 6)
  }

  test("submit") {
    val inputs = scala.io.Source
      .fromResource("dijkstraData.txt")
      .getLines()

    val graph = dijkstra.build(inputs.toArray)
    val (distance, path) = dijkstra.shortestPath(graph, 1)

    val destination = List(7, 37, 59, 82, 99, 115, 133, 165, 188, 197)

    val results = destination.map(dest => {
      distance.getOrElse(dest, 1000000)
    })
    println(results)
    println(results.mkString(","))

    // failed
    // 3024,3684,2947,2660,2367,2399,3879,2442,2610,5130
  }
}
*/
