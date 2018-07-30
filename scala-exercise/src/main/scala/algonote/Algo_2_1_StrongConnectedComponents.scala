package algonote

/**
  * Created by ikhoon on 30/07/2018.
  */
object Algo_2_1_StrongConnectedComponents {

  type Graph = Map[Int, Vector[Int]]
  def build(lines: Array[String]): Graph = {
    lines
      .map(_.replaceFirst("^\\s+", ""))
      .filterNot(_.isEmpty)
      .map(_.split("\\s+"))
      .map(_.map(_.toInt).toList)
      .map { case x :: y :: Nil => (x, y) }
      .foldLeft(Map.empty: Graph) { case (acc, (vertex, edge)) =>
         modify[Int, Vector[Int]](acc, vertex, Vector.empty, _ :+ edge)
      }
  }

  def rev(graph: Graph): Graph = {
    graph.foldLeft(Map.empty: Graph) { case (acc, (vertex, edges)) =>
        edges.foldLeft(acc) { case (newGraph, edge) =>
          modify[Int, Vector[Int]](newGraph, edge, Vector.empty, _ :+ vertex)
        }
    }
  }

  def dfsLoop(graph: Graph) = {
    var explored = Set.empty[Int]
    var leader = Map.empty[Int, Int]
    var f = Map.empty[Int, Int]
    def isExplored(node: Int): Boolean = explored.contains(node)

    var t = 0
    val keys: Vector[Int] = graph.keys.toVector
    def dfs(node: Int, start: Int): () = {
      explored += node
      leader += node -> start
      graph(node).foreach(edge => {
        if(!isExplored(edge)) {
          dfs(edge, start)
        }
      })
      t += 1
      f += node -> t
    }

    keys
      .sortWith(_ > _)
      .foreach(node => {
        if(!isExplored(node)) {
          dfs(node, node)
        }
      })
    (leader, f)
  }


  def modify[K, V](map: Map[K, V], key: K, default: V, f: V => V): Map[K, V] = {
    val value = map.getOrElse(key, default)
    map.updated(key, f(value))
  }

}
