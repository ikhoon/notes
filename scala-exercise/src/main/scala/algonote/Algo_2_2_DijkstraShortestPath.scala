package algonote

import algonote.Algo_2_2_DijkstraShortestPath.{Graph, Node}

import scala.collection.immutable

/**
  * Created by ikhoon on 06/08/2018.
  */
class Algo_2_2_DijkstraShortestPath {

  def dijkstraShortestPath(graph: Graph, start: Int): Vector[Int] = {
    val A = Map(start -> 0)
    val B = Vector.empty[Int]

    val reversed = transpose(graph)

    ???
  }

  def transpose(graph: Graph): Map[Int, Vector[Node]] =
    graph.foldLeft(Map.empty[Int, Vector[Node]]) {
      case (rev, (vertex, adj: Vector[Node])) =>
        adj.foldLeft(rev) {
          case (rev1, edge) =>
            modify[Int, Vector[Node]](rev1,
                                      edge.vertex,
                                      Vector.empty[Node],
                                      _ :+ Node(vertex, edge.weight))
        }
    }

  def modify[K, V](map: Map[K, V], key: K, default: V, f: V => V): Map[K, V] = {
    val value = map.getOrElse(key, default)
    map.updated(key, f(value))
  }
}

object Algo_2_2_DijkstraShortestPath {
  case class Node(vertex: Int, weight: Int)
  type Graph = Map[Int, Vector[Node]]
}
