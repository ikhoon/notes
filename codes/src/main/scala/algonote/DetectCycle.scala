package algonote

/**
  * Created by ikhoon on 2019-02-25.
  */
import scala.collection.mutable
import scala.collection.mutable.Map


object Hello {
  var dp = Map[Int, Boolean]()
  var visited = Map[Int, Boolean]()

  type Graph = mutable.Map[Int, List[Int]]

  // Total Time Complexity O(V + E)
  def detectCycle(graph: Graph): Boolean =
    graph.foldLeft(false) {
      case (hasCycle, (node: Int, adjs)) =>
        val isVisited = visited.getOrElse(node, false)
        //
        val isCycle = if (isVisited) false else detectCycleEachNode(graph, node)
        hasCycle || isCycle
    }

  def detectCycleEachNode(graph: Graph, node: Int): Boolean = {
    // recode visiting node to check cycle
    dp = dp + (node -> true)
    // recode visited node to improve performance
    visited = visited + (node -> true)

    // get next node
    val nextNodes = graph.getOrElse(node, List.empty)
    nextNodes.exists { nextNode =>
      // except visited node, go detectCycleEachNode
      if (!visited.getOrElse(nextNode, false)) {
        detectCycleEachNode(graph, nextNode)
      } else if (dp.getOrElse(nextNode, false)) {
        // if node is visited, return true
        true
      } else {
        // finish searching node
        dp = dp + (node -> false)
        false
      }
    }
  }

  def main(args: Array[String]) {

    val ln = scala.io.StdIn.readInt()

    val graph = mutable.Map[Int, List[Int]]()

    var str = scala.io.StdIn.readLine()
    while (str != null && str.nonEmpty) {
      val line: List[Int] = str
        .split(" ")
        .toList
        .map(_.toInt)
      val nodes = graph.getOrElse(line(0), List())
      graph.update(line(0), line(1) :: nodes)
      str = scala.io.StdIn.readLine()
    }

    val hasCycle = detectCycle(graph)
    println(hasCycle)
  }
}
