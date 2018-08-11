package algonote

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
}
