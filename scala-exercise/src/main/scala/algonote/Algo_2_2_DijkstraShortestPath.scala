package algonote

import algonote.Algo_2_2_DijkstraShortestPath.{Graph, Node}

import scala.collection.immutable
import scala.collection.immutable.Queue

/**
  * Created by ikhoon on 06/08/2018.
  */
class Algo_2_2_DijkstraShortestPath {

  def shortestPath(graph: Graph, start: Int): (Map[Int, Int], Map[Int, Vector[Int]]) = {
    val A = Map(start -> 0)
    val B = Map(start -> Vector(1))

    val reversed = transpose(graph)

    bfs(graph, reversed, A, B, Queue(start), Set.empty)

  }

  def bfs(
    graph: Graph,
    reversed: Graph,
    A: Map[Int, Int],
    B: Map[Int, Vector[Int]],
    openedSet: Queue[Int],
    closedSet: Set[Int]
  ): (Map[Int, Int], Map[Int, Vector[Int]]) = {

    if (openedSet.isEmpty) (A, B)
    else {
      val (v, nextQueue) = openedSet.dequeue
      val adjacent = graph.getOrElse(v, Vector.empty)
      val newOpenedSet = adjacent
        .filterNot(node => closedSet.contains(node.vertex))
        .foldLeft(nextQueue) {
          case (acc, w) =>
            if (acc.contains(w.vertex)) acc
            else acc.enqueue(w.vertex)
        }

      val (nA, nB, nC, nO) = adjacent
        .foldLeft((A, B, closedSet, newOpenedSet)) {
          case ((accA, accB, accC, accO), w) =>
            val distanceV = accA.getOrElse(v, 0)
            val distanceVW = distanceV + w.weight
            val (distance, newB, newC, newO) = accA
              .get(w.vertex) match {
              case Some(distanceW) =>
                if (distanceVW < distanceW) {
                  val (newC, newO) =
                    if (accC.contains(w.vertex)) (accC - w.vertex, accO.enqueue(w.vertex))
                    else (accC, accO)
                  val pathV = accB.getOrElse(v, Vector.empty)
                  (distanceVW, accB.updated(w.vertex, pathV :+ w.vertex), newC, newO)
                } else {
                  (distanceW, accB, accC, accO)
                }
              case None => {
                val pathV = accB.getOrElse(v, Vector.empty)
                (distanceVW, accB.updated(w.vertex, pathV :+ w.vertex), accC, accO)
              }
            }
            val newA = accA.updated(w.vertex, distance)
            (newA, newB, newC, newO)
        }

      bfs(graph, reversed, nA, nB, nO, nC + v)

    }
  }

  def transpose(graph: Graph): Graph =
    graph.foldLeft(Map.empty[Int, Vector[Node]]) {
      case (rev, (vertex, adj: Vector[Node])) =>
        adj.foldLeft(rev) {
          case (rev1, edge) =>
            modify[Int, Vector[Node]](rev1, edge.vertex, Vector.empty[Node], _ :+ Node(vertex, edge.weight))
        }
    }

  def modify[K, V](map: Map[K, V], key: K, default: V, f: V => V): Map[K, V] = {
    val value = map.getOrElse(key, default)
    map.updated(key, f(value))
  }

  def build(lines: Array[String]): Graph = {
    lines.view
      .map(_.replaceFirst("^\\s+", ""))
      .filterNot(_.isEmpty)
      .map(_.split("\\s+"))
      .map(_.toList)
      .map {
        case x :: xs =>
          (x.toInt, xs.map { y =>
            val tokens = y.split(",")
            Node(tokens(0).toInt, tokens(1).toInt)
          }.toVector)
      }
      .toMap
  }
}

object Algo_2_2_DijkstraShortestPath {
  case class Node(vertex: Int, weight: Int)
  type Graph = Map[Int, Vector[Node]]
}
